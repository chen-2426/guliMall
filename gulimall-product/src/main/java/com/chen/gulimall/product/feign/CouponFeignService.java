package com.chen.gulimall.product.feign;

import com.chen.gulimall.base.TO.SkuReductionTo;
import com.chen.gulimall.base.TO.SpuBoundsTo;
import com.chen.gulimall.base.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/24 15:42
 * @description
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
