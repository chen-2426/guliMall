package com.chen.gulimall.product.VO.Web;

import lombok.Data;

import java.util.List;

@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<String> attrValues;
}
