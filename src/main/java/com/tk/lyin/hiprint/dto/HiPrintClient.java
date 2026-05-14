package com.tk.lyin.hiprint.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socketio4j.socketio.SocketIOClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class HiPrintClient {
    @JsonIgnore
    private SocketIOClient channel;
    private List<Printer> printerList;
    private ClientInfo client;
    private String token;
}
