package com.chen.gulimall.base.Constant;

import lombok.AllArgsConstructor;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/25 16:16
 * @description
 */
public class WareConstant {
    @AllArgsConstructor
    public enum PURCHASE_STATUS{
        ASSIGNED(0,"已分配"),
        CREATED(1,"新建"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
        HASERROR(4,"异常");
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }
    @AllArgsConstructor
    public enum PURCHASE_DETAIL_STATUS{
        ASSIGNED(0,"已分配"),
        CREATED(1,"新建"),
        BUYING(2,"采购中"),
        FINISH(3,"已完成"),
        FAILED(4,"采购失败");
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }
}
