package com.tk.lyin.hiprint.admin.vo;

import lombok.Getter;

@Getter
public enum TemplateType {

    IMAGE("image"),
    PDF("pdf"),
    HTML("html"),
    ;

    private final String type;

    TemplateType(String type) {
        this.type = type;
    }
}
