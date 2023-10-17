package com.chen.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/16 19:25
 * @description
 */
@Data
public class OrderItemVo {
    private Long skuId;
    private Boolean check = true;
    private List<String> skuAttr;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer num;
    private Boolean hasStock;
}
