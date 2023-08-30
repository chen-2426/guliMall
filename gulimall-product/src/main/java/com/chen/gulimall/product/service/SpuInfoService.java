package com.chen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.product.VO.SpuSaveVO;
import com.chen.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVO spuInfo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);

    void updateSpuStatus(Long spuId, int spuUp);
}

