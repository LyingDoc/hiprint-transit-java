package com.tk.lyin.hiprint.dto;

import lombok.Data;


@Data
public class Result {
    private String msg;
    private String templateId;
    private String replyId;
    private byte[] buffer;
}
