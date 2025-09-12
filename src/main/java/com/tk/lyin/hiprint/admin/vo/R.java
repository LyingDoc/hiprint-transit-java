package com.tk.lyin.hiprint.admin.vo;

import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Getter
@Setter
public class R extends LinkedHashMap<String, Object> implements Serializable {

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";


    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";


    /**
     * 初始化一个新创建的 R 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public R(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(DATA_TAG, data);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static R success() {
        return R.success(null);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static R success(Object data) {
        return R.success(ResultEnum.SUCCESS.getMsg(), data);
    }

    /*    *//*
      返回成功消息

      @param msg 返回内容
     * @return 成功消息
     */

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static R success(String msg, Object data) {
        return new R(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static R error() {
        return R.error(ResultEnum.FAIL.getMsg());
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 错误消息
     */
    public static R error(String msg) {
        return R.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static R error(String msg, Object data) {
        return new R(ResultEnum.FAIL.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 错误消息
     */
    public static R error(int code, String msg) {
        return new R(code, msg, null);
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public R put(String key, Object value) {
        // 移除data项
        Object data = super.get(DATA_TAG);
        if (data == null) {
            super.remove(DATA_TAG);
        }
        super.put(key, value);
        return this;
    }

}
