package com.chen.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author chen
 * @email 
 * @date 2023-08-14 16:11:45
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

