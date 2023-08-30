package com.chen.gulimall.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.chen.gulimall.base.TO.es.SkuEsModel;
import com.chen.gulimall.search.constant.Esconstant;
import com.chen.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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
    ElasticsearchClient client;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        BulkRequest.Builder builder = new BulkRequest.Builder();;
        skuEsModels.forEach( a->{
            builder.operations(op->
                    op.index(idx ->
                            idx.index(Esconstant.PRODUCT_INDEX)
                                    .id(a.getSkuId().toString())
                                    .document(a)
                    )
            );
        });

        BulkResponse result = client.bulk(builder.build());

        // Log errors, if any error
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
        return result.errors();
    }
}
