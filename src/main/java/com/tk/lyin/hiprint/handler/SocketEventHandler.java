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


    @Override
    public AuthTokenResult getAuthTokenResult(Object authToken, SocketIOClient client) {
        log.info("auth token: {}", authToken);
        if (Objects.nonNull(authToken)) {
            String token = ((LinkedHashMap<String, String>) authToken).get("token");
            client.set("token", token);
            String sessionId = client.getSessionId().toString();
            if (StringUtils.equals(token, serverToken)) {
                String clientType = client.getHandshakeData().getSingleUrlParam("client");
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
                                        StringUtils.equals(c.getHandshakeData().getSingleUrlParam("client"), "electron-hiprint")
                        ).count())
                        .webClients(
                                client.getNamespace().getAllClients().stream().filter(
                                        c ->
                                                StringUtils.equals(token, c.get("token")) &&
                                                        StringUtils.equals(c.getHandshakeData().getSingleUrlParam("client"), "web-client")
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

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("on disconnected: {}", client);
        // 清理客户端
        Object tokenObj = client.get("token");
        if (tokenObj instanceof String) {
            String token = (String) tokenObj;
            CLIENTS.remove(token, client.getSessionId().toString());
        }
    }


    /**
     * 注册客户端信息
     *
     * @param client
     * @param clientInfo
     */
    @OnEvent("clientInfo")
    public void onClientInfo(SocketIOClient client, ClientInfo clientInfo) {
        log.info("clientInfo: {}", GsonUtils.toJson(clientInfo));
        HiPrintClient channel = getChannel(client);
        channel.setClient(clientInfo);
    }

    @OnEvent("printerList")
    public void getPrinterList(SocketIOClient client, List<Printer> printerList) {
        log.info("printerList: {}", GsonUtils.toJson(printerList));
        HiPrintClient channel = getChannel(client);
        channel.setPrinterList(printerList);
    }

    @OnEvent("getClients")
    public void getClients(SocketIOClient client) {

    }

    @OnEvent("refreshPrinterList")
    public void refreshPrinterList(SocketIOClient client) {
        client.getNamespace().getBroadcastOperations();
    }

    @OnEvent("address")
    public void getAddress(SocketIOClient client) {

    }

    @OnEvent("ippPrint")
    public void ippPrint(SocketIOClient client) {
        client.getNamespace();
    }

    @OnEvent("ippRequestCallback")
    public void ippRequestCallback(SocketIOClient client, ReplyPacket packet, Object data) {
        if (StringUtils.isNotEmpty(packet.getReplyId())) {
            client.getNamespace().getRoomOperations(packet.getReplyId()).sendEvent("ippRequestCallback", packet, data);
        }
    }

    private boolean isTest(SocketIOClient client) {
        String test = client.getHandshakeData().getSingleUrlParam("test");
        return StringUtils.equals(test, "true");
    }


    private HiPrintClient getChannel(SocketIOClient client) {
        return client.get("_channel");
    }

}
