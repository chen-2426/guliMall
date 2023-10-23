package com.chen.gulimall.order.TO;

import com.chen.gulimall.order.entity.OrderEntity;
import com.chen.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/17 23:08
 * @description
 */

@Data
public class OrderCreateTo {
    private OrderEntity order;
    private List<OrderItemEntity> orderItems;
    private BigDecimal payprice;
    private BigDecimal fare;
}
