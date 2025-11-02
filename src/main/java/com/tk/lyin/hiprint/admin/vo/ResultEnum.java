package com.tk.lyin.hiprint.admin.vo;

import lombok.Getter;

@Getter
public enum ResultEnum {
    // 这里是可以自己定义的，方便与前端交互即可
    SUCCESS(200, "success"),
    FAIL(500, "fail"),
    TEMPLATE_FAIL(10011, "template loading fail"),

    ;

    private final Integer code;
    private final String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
