package com.chen.gulimall.product.feign;

import com.chen.gulimall.base.TO.es.SkuEsModel;
import com.chen.gulimall.base.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 17:02
 * @description
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
