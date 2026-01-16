package com.tk.lyin.hiprint;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.tk.lyin.hiprint.config.ServerConfig;
import com.tk.lyin.hiprint.handler.SocketEventHandler;

import java.util.Objects;


@Setter
@RequiredArgsConstructor
public class HiPrintServerFactoryBean extends AbstractFactoryBean<SocketIOServer> {

    private final ServerConfig serverConfig;

    private SocketEventHandler eventHandler;

    @Override
    public Class<?> getObjectType() {
        return SocketIOServer.class;
    }

    @Override
    @NonNull
    protected SocketIOServer createInstance() {
        int port = serverConfig.getPort();
        String host = serverConfig.getHost();
        boolean epoll = serverConfig.getEpoll();

        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        if (epoll) {
            config.setUseLinuxNativeEpoll(true);
        }
        config.getSocketConfig().setReuseAddress(true);
        config.setAllowCustomRequests(true);
        config.setEnableCors(true);
        config.setOrigin("*");
        config.setJsonSupport(new JacksonJsonSupport());
        SocketIOServer server = new SocketIOServer(config);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.addListeners(eventHandler);
        server.getAllNamespaces().forEach(namespace -> namespace.addAuthTokenListener(eventHandler));
        server.start();
        return server;
    }

    @Override
    protected void destroyInstance(SocketIOServer instance) {
        if (Objects.nonNull(instance) && instance.isStarted()) {
            instance.stop();
        }
    }
}
