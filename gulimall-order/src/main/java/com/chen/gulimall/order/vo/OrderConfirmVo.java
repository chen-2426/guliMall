package com.chen.gulimall.order.vo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/16 19:26
 * @description
 */
@Data
public class OrderConfirmVo {
    //地址
    List<MemberAddressVo> addresses;
    //物品
    List<OrderItemVo> items;
    //优惠信息
    Integer integration;
    //订单总额
    BigDecimal total;
//    应付款
    BigDecimal payprice;
//    防重令牌
    String orderToken;

}
