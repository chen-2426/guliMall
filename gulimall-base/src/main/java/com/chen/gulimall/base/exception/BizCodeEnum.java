package com.chen.gulimall.base.exception;

import lombok.AllArgsConstructor;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/20 22:54
 * @description
 */
@AllArgsConstructor
public enum BizCodeEnum {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002,"验证码获取频率过高"),
    TO_MANY_REQUEST(10003,"请求流量过大"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    USER_EXIST_EXCEPTION(15001, "用户已存在异常"),
    PHONE_EXIST_EXCEPTION(15002, "手机号已存在异常"),
    LOGINACCT_PASSWORD_INVAILD_EXCEPTION(15003, "账号/密码错误"),
    NO_STOCK_EXCEPTION(21000, "商品库存不足");


    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}