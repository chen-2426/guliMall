package com.chen.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:48:45
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

