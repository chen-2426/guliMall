package com.chen.gulimall.order.feign;

import com.chen.gulimall.base.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/17 10:51
 * @description
 */
@FeignClient("gulimall-ware")
public interface WareFeign {
    @PostMapping("/hasstock")
    public R hasStock(@RequestBody List<Long> skuids);
}
