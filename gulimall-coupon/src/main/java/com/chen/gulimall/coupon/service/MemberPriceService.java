package com.chen.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author chen
 * @email 
 * @date 2023-08-14 15:48:46
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

