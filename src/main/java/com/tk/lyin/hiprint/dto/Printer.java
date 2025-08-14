package com.tk.lyin.hiprint.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Printer {
    private String name;
    private String displayName;
    private String description;
    private Integer status;
    private Boolean isDefault;
    private Map<String, Object> options;

}
