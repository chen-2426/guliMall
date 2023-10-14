package com.chen.gulimall.product.VO.Web;

import com.chen.gulimall.product.entity.SkuImagesEntity;
import com.chen.gulimall.product.entity.SkuInfoEntity;
import com.chen.gulimall.product.entity.SpuInfoDescEntity;
import com.chen.gulimall.product.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/8 15:01
 * @description
 */
@Data
public class SkuItemVO {
    SkuInfoEntity info;
    List<SkuImagesEntity> images;
    List<SkuItemSaleAttrVo> saleAttr;
    SpuInfoDescEntity desc;
    List<SpuItemAttrGroupVo> groupAttrs;



}

