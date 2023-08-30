package com.chen.gulimall.product.feign;

import com.chen.gulimall.base.TO.SpuBoundsTo;
import com.chen.gulimall.base.utils.R;
import com.chen.gulimall.product.VO.HasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 16:08
 * @description
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    R HasStock(@RequestBody List<Long> skuids);
}
