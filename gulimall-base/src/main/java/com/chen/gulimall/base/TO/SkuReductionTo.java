package com.chen.gulimall.base.TO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/24 16:01
 * @description
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
