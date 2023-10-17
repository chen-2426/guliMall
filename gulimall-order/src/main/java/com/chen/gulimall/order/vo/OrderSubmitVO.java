package com.chen.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/17 22:09
 * @description
 */
@Data
public class OrderSubmitVO {
    private Long addrId;
    private Integer payType;
//    购买商品，去购物车再获取一遍
    private String orderToken;//防重
    private BigDecimal payPrice;//价格验证
    private String note;//订单备注
}
