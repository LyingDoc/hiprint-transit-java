package com.tk.lyin.hiprint.admin.vo;

import lombok.Getter;

@Getter
public enum ResultEnum {
    // 这里是可以自己定义的，方便与前端交互即可
    UNKNOWN_ERROR(-1, "未知错误"),
    REQUEST_ERROR(500, "请求异常"),
    SUCCESS(200, "success"),
    FAIL(500, "fail"),
    DATA_IS_NULL(1, "参数校验失败"),
    DATA_NULL(404, "暂无歌词"),

    LIMIT_ERROR(400, "limit参数只允许传递正整数"),
    LIMIT_EX(400, "limit参数错误不能超过100"),

    TITLE_NULL(400, "歌曲名称不能为空"),


    TOKEN_ERROR(3, "Token码错误"),
    TOKEN_NULL(5, "Token不能为空"),
    TOKEN_INVALID(101, "Token已失效"),
    TOKEN_EXIST(500, "Token已存在"),
    MOBILE_NULL(400, "手机号码为空"),
    MOBILE_FORMAT(700, "手机手机号码格式错误"),

    PARA_TIME(1101, "请求头缺少[X-timestamp]参数"),
    PARA_SIGN(1102, "请求头缺少[X-sign]参数"),
    PARA_KEY(1103, "请求头缺少[X-secretkey]参数"),
    PARA_NON(1104, "请求头缺少[X-nonce]参数"),

    API_KEY(12001, "未配置[lrc.api.secret-key]"),
    API_TIME(12002, "未配置[lrc.api.secret-time-error]"),
    API_TITLE_WEIGHT(12003, "未配置[lrc.api.title-weight]"),
    API_ARTISTS_WEIGHT(12004, "未配置[lrc.api.artists-weight]"),
    API_ALBUM_WEIGHT(12005, "未配置[lrc.api.album-weight]"),


    KEY_INVALID(1017, "[X-secretkey]参数秘钥不匹配"),

    TIME_INVALID(1007, "[X-timestamp]参数时间差超出允许的范围，请求无效"),
    NON_INVALID(1008, "[X-nonce]参数已被使用，请求无效"),
    SIGN_INVALID(100, "[X-sign]参数签名不匹配"),

    TIME_UNKNOWN(1001, "[X-timestamp]非法参数"),
    ;

    private final Integer code;
    private final String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
