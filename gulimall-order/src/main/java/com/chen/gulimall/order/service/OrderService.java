package com.chen.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.order.entity.OrderEntity;
import com.chen.gulimall.order.vo.OrderConfirmVo;
import com.chen.gulimall.order.vo.PayAsyncVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author chen
 * @email 
 * @date 2023-08-14 16:11:45
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    void handlerPayResult(PayAsyncVo payAsyncVo);

    void closeOrder(OrderEntity order);
}

