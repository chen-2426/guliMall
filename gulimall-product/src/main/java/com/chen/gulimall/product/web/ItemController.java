package com.chen.gulimall.product.web;

import com.chen.gulimall.product.VO.Web.SkuItemVO;
import com.chen.gulimall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/7 15:35
 * @description
 */
@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    /**
     * 展示sku的详情
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId){
        SkuItemVO item = skuInfoService.item(skuId);
        return "item";
    }
}
