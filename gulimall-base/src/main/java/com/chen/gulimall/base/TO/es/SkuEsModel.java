package com.chen.gulimall.base.TO.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 13:19
 * @description
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> attrs;
    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }


}
