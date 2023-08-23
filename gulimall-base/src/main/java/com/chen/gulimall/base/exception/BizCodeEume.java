package com.chen.gulimall.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/20 22:54
 * @description
 */
@AllArgsConstructor
public enum BizCodeEume {
    UKNOWN_EXCEPTION(1000,"未知异常"),
    VALID_EXCEPTION(10001,"参数格式校验失败");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
