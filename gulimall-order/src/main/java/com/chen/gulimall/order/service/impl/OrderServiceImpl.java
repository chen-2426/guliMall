package com.chen.gulimall.order.service.impl;

import com.chen.gulimall.base.VO.MemberRespVo;
import com.chen.gulimall.order.feign.CartFeign;
import com.chen.gulimall.order.feign.MemberFeign;
import com.chen.gulimall.order.interceptor.LoginUserInterceptor;
import com.chen.gulimall.order.vo.MemberAddressVo;
import com.chen.gulimall.order.vo.OrderConfirmVo;
import com.chen.gulimall.order.vo.OrderItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.order.dao.OrderDao;
import com.chen.gulimall.order.entity.OrderEntity;
import com.chen.gulimall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    MemberFeign memberFeign;
    @Autowired
    CartFeign cartFeign;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> memberFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeign.getAddress(memberRespVo.getId());
            confirmVo.setAddresses(address);
        }, executor);
        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> currentUserCartItems = cartFeign.getCurrentUserCartItems();
            confirmVo.setItems(currentUserCartItems);
        }, executor);
        Integer integration = LoginUserInterceptor.loginUser.get().getIntegration();
        confirmVo.setIntegration(integration);
        //防重复令牌 todo
        CompletableFuture.allOf(memberFuture, cartFuture).get();
        return confirmVo;
    }


}