package com.chen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.product.VO.AttrRespVO;
import com.chen.gulimall.product.VO.AttrVO;
import com.chen.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVO attr);

    AttrVO getAttrVoById(Long attrId);

    PageUtils queryBaseAttrPage(Long catelogId, Map<String, Object> params, String type);

    AttrRespVO getAttrInfo(Long attrId);

    void updateAttr(AttrVO attr);

    List<AttrEntity> getAttrRelationship(Long attrgroupId);

    PageUtils getNoAttrRelationship(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

