package com.chen.gulimall.search.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/6 15:21
 * @description
 */
@FeignClient("gulimall_product")
public interface ProductFeignService {

}
