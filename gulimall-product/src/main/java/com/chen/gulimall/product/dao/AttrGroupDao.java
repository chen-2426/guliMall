package com.chen.gulimall.product.dao;

import com.chen.gulimall.product.VO.Web.SkuItemVO;
import com.chen.gulimall.product.VO.Web.SpuItemAttrGroupVo;
import com.chen.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {
    List<SpuItemAttrGroupVo>  getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
