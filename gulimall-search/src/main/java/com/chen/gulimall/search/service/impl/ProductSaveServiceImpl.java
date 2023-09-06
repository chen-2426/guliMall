package com.chen.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.chen.gulimall.base.TO.es.SkuEsModel;
import com.chen.gulimall.search.config.GulimallElasticSearchConfig;
import com.chen.gulimall.search.constant.Esconstant;
import com.chen.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/30 16:40
 * @description
 */
@Service
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest("product");
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        boolean hasFailures = bulkResponse.hasFailures();
        List<String> collect = Arrays.asList(bulkResponse.getItems()).stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        log.info("商品上架完成：{}",collect);

        return !hasFailures;
    }
//    @Override
//    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
//        BulkRequest.Builder builder = new BulkRequest.Builder();;
//        skuEsModels.forEach( a->{
//            builder.operations(op->
//                    op.index(idx ->
//                            idx.index(Esconstant.PRODUCT_INDEX)
//                                    .id(a.getSkuId().toString())
//                                    .document(a)
//                    )
//            );
//        });
//
//        BulkResponse result = client.bulk(builder.build());
//
//        // Log errors, if any error
//        if (result.errors()) {
//            log.error("Bulk had errors");
//            for (BulkResponseItem item: result.items()) {
//                if (item.error() != null) {
//                    log.error(item.error().reason());
//                }
//            }
//        }
//        return result.errors();
//    }
}
