package com.tk.lyin.hiprint.dto;

import lombok.Data;

@Data
public class ReplyPacket {
    private String replyId;
    private IppPrint.Opt printer;
    private String type;
    private String message;
}
