package com.chen.gulimall.order.feign;

import com.chen.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/16 20:44
 * @description
 */
@FeignClient("gulimall-cart")
public interface CartFeign {
    @GetMapping("/getCurrentUserCartItems ")
     List<OrderItemVo> getCurrentUserCartItems();
}
