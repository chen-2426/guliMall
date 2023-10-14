package com.chen.gulimall.search.controller;

import com.chen.gulimall.base.TO.es.SkuEsModel;
import com.chen.gulimall.base.exception.BizCodeEnum;
import com.chen.gulimall.base.utils.R;
import com.chen.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 16:34
 * @description
 */
@RestController
@Slf4j
@RequestMapping("/search/save")
public class ElasticSearchSaveController {
    @Autowired
    ProductSaveService productSaveService;
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels)  {
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("商品上架错误",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }
        if(b) return R.ok();
        return R.error();
    }

}
