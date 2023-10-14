package com.chen.gulimall.base.Constant;

import lombok.AllArgsConstructor;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/23 16:44
 * @description
 */

public class ProductConstant {
    @AllArgsConstructor
    public enum ATTR_TYPE {
        BASE(1, "base"), SALE(0, "sale");
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }

    public class SPU_STATUS {
        public static final int NEW_SPU = 0;
        public static final int SPU_UP = 1;
        public static final int SPU_DOWN = 2;
    }

}
