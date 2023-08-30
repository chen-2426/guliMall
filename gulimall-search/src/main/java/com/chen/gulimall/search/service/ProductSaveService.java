package com.chen.gulimall.search.service;

import com.chen.gulimall.base.TO.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 16:39
 * @description
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
