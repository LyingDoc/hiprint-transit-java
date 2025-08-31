package com.tk.lyin.hiprint.handler;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.tk.lyin.hiprint.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ServerInfo;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import com.tk.lyin.hiprint.utils.gson.GsonUtils;
import com.tk.lyin.hiprint.utils.string.StringUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
public class SocketEventHandler implements AuthTokenListener {

    private final String serverToken;

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100));

    // guava table <token, sessionId, SocketIOClient>
    private static final Table<String, String, HiPrintClient> CLIENTS = Tables.newCustomTable(Maps.newConcurrentMap(), Maps::newConcurrentMap);

    @OnConnect
    @SneakyThrows
    public void onConnect(SocketIOClient client) {
        boolean testing = isTest(client);
        log.info("on connected: {} testing: {}", client, testing);
        if (testing) {
            log.info("ping {}", client.getSessionId());
            executor.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.disconnect();
                log.info("test ended {}", client.getSessionId());
            });
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public AuthTokenResult getAuthTokenResult(Object authToken, SocketIOClient client) {
        log.info("auth token: {}", authToken);
        if (Objects.nonNull(authToken)) {
            String token = ((LinkedHashMap<String, String>) authToken).get("token");
            client.set("token", token);
            String sessionId = client.getSessionId().toString();
            if (StringUtils.equals(token, serverToken)) {
                String clientType = getClient(client);
                if (StringUtils.equals(clientType, "electron-hiprint")) {
                    log.info("Client connected: {{}} | {{}} | (electron-hiprint)", sessionId, token);
                    client.joinRoom(token + "_electron-hiprint");
                } else {
                    log.info("Client connected: {{}} | {{}}  | (web-client)", sessionId, token);
                    client.joinRoom(token + "_web-client");

                    // Map<String, HiPrintClient>
                    List<Printer> printerList = Collections.emptyList();
                    ClientInfo clientInfo = new ClientInfo();
                    Map<String, HiPrintClient> clients = CLIENTS.row(token);
                    for (HiPrintClient hiPrintClient : clients.values()) {
                        // 使用每个客户端
                        printerList = hiPrintClient.getPrinterList();
                        clientInfo = hiPrintClient.getClient();

                    }
                    client.sendEvent("clients", clients);
                    client.sendEvent("clientInfo", clientInfo);
                    client.sendEvent("printerList", printerList);
                }

                ServerMetrics metrics = ServerMetrics.builder()
                        .version(ServerInfo.getServerInfo())
                        .currentClients(CLIENTS.size())
                        .allClients(client.getNamespace().getAllClients().stream().filter(
                                c ->
                                        StringUtils.equals(getClient(c), "electron-hiprint")
                        ).count())
                        .webClients(
                                client.getNamespace().getAllClients().stream().filter(
                                        c ->
                                                StringUtils.equals(token, c.get("token")) &&
                                                        StringUtils.equals(getClient(c), "web-client")
                                ).count()
                        )
                        .allWebClients(0L)
                        .totalMemory(Runtime.getRuntime().totalMemory())
                        .freeMemory(Runtime.getRuntime().freeMemory())
                        .build();

                client.sendEvent("serverInfo", metrics);
                HiPrintClient channel = HiPrintClient.builder()
                        .channel(client)
                        .token(token)
                        .build();
                client.set("_channel", channel);
                CLIENTS.put(token, sessionId, channel);
                return AuthTokenResult.AuthTokenResultSuccess;
            }
        }
        return new AuthTokenResult(false, "Authentication failed");
    }

    /**
     * 关闭客户端
     *
     * @param client electron客户端io
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        boolean testing = isTest(client);
        if (BooleanUtils.isFalse(testing)) {
            log.info("on disconnected: {} testing: {}", client, testing);
            // 清理客户端
            String sessionId = client.getSessionId().toString();
            String token = client.get("token");
            CLIENTS.remove(token, sessionId);
        }
    }

    /**
     * 注册客户端信息
     *
     * @param client     electron客户端io
     * @param clientInfo 客户端详细信息
     */
    @OnEvent("clientInfo")
    public void onClientInfo(SocketIOClient client, ClientInfo clientInfo) {
        log.info("clientInfo: {}", GsonUtils.toJson(clientInfo));
        HiPrintClient channel = getChannel(client);
        channel.setClient(clientInfo);
    }

    /**
     * 注册客户端打印机列表
     *
     * @param client      electron客户端io
     * @param printerList 打印机列表
     */
    @OnEvent("printerList")
    public void getPrinterList(SocketIOClient client, List<Printer> printerList) {
        log.info("printerList: {}", GsonUtils.toJson(printerList));
        HiPrintClient channel = getChannel(client);
        channel.setPrinterList(printerList);
    }

    /**
     * 注册客户端获取clients
     *
     * @param client web客户端io
     */
    @OnEvent("getClients")
    public void getClients(SocketIOClient client) {
        String token = client.get("token");
        Map<String, HiPrintClient> clients = CLIENTS.row(token);
        log.info("getClients: {}", GsonUtils.toJson(clients));
        client.getNamespace().getRoomOperations(token + "_web-client").sendEvent("clients", clients);
    }

    /**
     * 注册客户端打印机列表
     *
     * @param client  web客户端io
     */
    @OnEvent("getClientInfo")
    public void getClientInfo(SocketIOClient client) {
        log.info("getClientInfo: {}", "获取中...");
        String token = client.get("token");
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("getClientInfo");
    }

    /**
     * 注册客户端获取纸张信息
     *
     * @param client       web客户端io
     * @param printer     纸张参数
     */
    @OnEvent("getPaperSizeInfo")
    public void getPaperSizeInfo(SocketIOClient client, String printer) {
        log.info("getPaperSizeInfo：{}", printer);
        String token = client.get("token");
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("getPaperSizeInfo", printer);
    }

    /**
     * 注册客户端刷新打印机列表
     *
     * @param client  web客户端io
     */
    @OnEvent("refreshPrinterList")
    public void refreshPrinterList(SocketIOClient client) {
        log.info("refreshPrinterList: {}", "打印机刷新获取中...");
        String token = client.get("token");
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("refreshPrinterList");
    }

    /**
     * 注册客户端请求 address
     *
     * @param client  web客户端io
     * @param data    地址参数集合
     */
    @OnEvent("address")
    public void getAddress(SocketIOClient client, Object... data) {
        log.info("addressType：{}", data);
        String token = client.get("token");
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("address", data);
    }

    /**
     * 注册客户端请求 ipp打印
     *
     * @param client     web客户端io
     * @param ippPrint   ipp打印参数
     */
    @OnEvent("ippPrint")
    public void ippPrint(SocketIOClient client, IppPrint ippPrint) {
        String token = client.get("token");
        String sessionId = client.getSessionId().toString();
        ippPrint.setReplyId(sessionId);
        log.info("ippPrint: {}", GsonUtils.toJson(ippPrint));
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("ippPrint", ippPrint);
    }

    /**
     * 注册客户端请求 ipp请求打印
     *
     * @param client      web客户端io
     * @param ippRequest  ippRequest 打印参数
     */
    @OnEvent("ippRequest")
    public void ippRequest(SocketIOClient client, IppRequest ippRequest) {
        String sessionId = client.getSessionId().toString();
        ippRequest.setReplyId(sessionId);
        String token = client.get("token");
        log.info("ippRequest: {}", GsonUtils.toJson(ippRequest));
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("ippRequest", ippRequest);
    }

    /**
     * 注册客户端请求 ipp请求回调
     *
     * @param client  electron客户端io
     * @param packet 房间参数
     * @param data   回调参数
     */
    @OnEvent("ippRequestCallback")
    public void ippRequestCallback(SocketIOClient client, ReplyPacket packet, Object data) {
        String replyId = packet.getReplyId();
        if (StringUtils.isNotEmpty(replyId)) {
            client.getNamespace().getRoomOperations(replyId).sendEvent("ippRequestCallback", packet, data);
        }
    }

    /**
     * 注册客户端请求 news打印任务
     *
     * @param client web客户端io
     * @param data   news参数
     */
    @OnEvent("news")
    public void news(SocketIOClient client, Object data) {
        String token = client.get("token");
        log.info("news: {}", GsonUtils.toJson(data));
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("news", data);
    }

    /**
     * 注册客户端请求 render-print打印任务
     *
     * @param client web客户端io
     * @param data   render-print参数
     */
    @OnEvent("render-print")
    public void renderPrint(SocketIOClient client, Object data) {
        String token = client.get("token");
        log.info("renderPrint: {}", GsonUtils.toJson(data));
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("render-print", data);
    }

    // 获取客户端
    private static String getClient(SocketIOClient c) {
        return c.getHandshakeData().getSingleUrlParam("client");
    }

    /**
     * 注册客户端请求 render-jpeg打印任务
     *
     * @param client web客户端io
     * @param data   render-jpeg参数
     */
    @OnEvent("render-jpeg")
    public void renderJpeg(SocketIOClient client, Object data) {
        String token = client.get("token");
        log.info("renderJpeg: {}", GsonUtils.toJson(data));
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("render-jpeg", data);
    }


    // 检测是否为测试
    private boolean isTest(SocketIOClient client) {
        String test = client.getHandshakeData().getSingleUrlParam("test");
        return StringUtils.equals(test, "true");
    }

    /**
     * 注册客户端请求 render-pdf打印任务
     *
     * @param client web客户端io
     * @param data   render-pdf参数
     */
    @OnEvent("render-pdf")
    public void renderPdf(SocketIOClient client, Object data) {
        String token = client.get("token");
        log.info("renderPdf: {}", GsonUtils.toJson(data));
        client.getNamespace().getRoomOperations(token + "_electron-hiprint").sendEvent("render-pdf", data);
    }

    // 获取channel
    private HiPrintClient getChannel(SocketIOClient client) {
        return client.get("_channel");
    }

}
