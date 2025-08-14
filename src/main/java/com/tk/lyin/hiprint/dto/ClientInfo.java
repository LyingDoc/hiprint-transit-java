package com.tk.lyin.hiprint.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientInfo {
    private String hostname;
    private String version;
    private String platform;
    private String arch;
    private String mac;
    private String ip;
    private String ipv6;
    private String clientUrl;
    private String machineId;
}
