package com.chen.gulimall.product.dao;

import com.chen.gulimall.product.VO.Web.SkuItemSaleAttrVo;
import com.chen.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSkuItemSaleAttrVoBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuItemSaleAttrValuesAsStringList(@Param("id") Long id);
}
