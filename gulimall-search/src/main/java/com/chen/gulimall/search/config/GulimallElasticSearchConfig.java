package com.chen.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/28 17:20
 * @description es配置
 */
@Configuration
public class GulimallElasticSearchConfig {
//
//    新版本，但问题众多，无法使用
//    @Bean
//    public ElasticsearchClient restHighLevelClient() {
//        RestClient restClient = RestClient.builder(
//                new HttpHost("localhost", 9200)
//        ).build();
//        ElasticsearchTransport elasticsearchTransport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//
//        return new ElasticsearchClient(elasticsearchTransport);
//    }
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.56.102", 9200, "http")));
        return client;
    }
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

}
