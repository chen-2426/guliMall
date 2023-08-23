package com.chen.gulimall.base.utils;

import lombok.AllArgsConstructor;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/23 16:44
 * @description
 */

public class ProductConstant {
    @AllArgsConstructor
    public enum ATTR_TYPE{
        BASE(1,"base"),SALE(0,"sale");
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
