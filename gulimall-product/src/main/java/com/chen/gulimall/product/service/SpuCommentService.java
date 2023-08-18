package com.chen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

