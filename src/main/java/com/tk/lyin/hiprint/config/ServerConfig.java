package com.tk.lyin.hiprint.config;

import com.corundumstudio.socketio.BasicConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tk.lyin.hiprint.HiPrintServerFactoryBean;
import com.tk.lyin.hiprint.handler.SocketEventHandler;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(prefix = "hiprint")
@Getter
@Setter
public class ServerConfig {
    private String host;
    private Integer port;
    private Boolean epoll;
    private String authToken;
    private String active;

    @Bean
    @Primary
    public HiPrintServerFactoryBean server() {
        HiPrintServerFactoryBean factory = new HiPrintServerFactoryBean();
        factory.setEventHandler(new SocketEventHandler(authToken));
        return factory;
    }
}
