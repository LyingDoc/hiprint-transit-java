package com.tk.lyin.hiprint.core.exception;


import lombok.Getter;

/**
 * 基础异常
 *
 * @author Ling
 */
@Getter
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    public BaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(String message) {
        this(null, message);
    }


}
