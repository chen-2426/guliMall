package com.chen.gulimall.ware.dao;

import com.chen.gulimall.ware.VO.HasStockVo;
import com.chen.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author chen
 * @email 
 * @date 2023-08-14 16:19:02
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    List<HasStockVo> hasStock(@Param("skuids") List<Long> skuids);
}
