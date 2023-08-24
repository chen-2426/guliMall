package com.chen.gulimall.base.TO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/24 15:50
 * @description
 */
@Data
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
