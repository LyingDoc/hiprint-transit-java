package com.tk.lyin.hiprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Template {
    private String baseUrl;
    private String domId;
    private Map<String,Object> printData;
    private Map<String,Object> template;
}
