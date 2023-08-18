package com.chen.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:48:46
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

