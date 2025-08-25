package com.tk.lyin.hiprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class IppRequest {
    private String url;
    private String replyId;
    private Map<String, Object> data;
}
