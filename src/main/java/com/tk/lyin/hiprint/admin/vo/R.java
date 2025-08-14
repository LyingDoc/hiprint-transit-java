package com.tk.lyin.hiprint.admin.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class R<T> {
    private static final int SUCCESS = 200;
    @Builder.Default
    private int code = SUCCESS;
    private String msg;
    private T data;

    public static <T> R<T> success(T data) {
        return R.<T>builder().code(SUCCESS).data(data).build();
    }

    public boolean isSuccess() {
        return code == SUCCESS;
    }
}
