package com.chen.gulimall.order.vo;

import com.chen.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/17 22:22
 * @description
 */
@Data
public class SubmitOrderResponseVO {
    private OrderEntity order;
    private Integer code; //错误状态码
}
