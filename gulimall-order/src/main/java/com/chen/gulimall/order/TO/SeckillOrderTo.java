package com.chen.gulimall.order.TO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/23 12:58
 * @description
 */
@Data
public class SeckillOrderTo {
    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 会员ID
     */
    private Long memberId;

}
