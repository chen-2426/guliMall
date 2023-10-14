package com.chen.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableFeignClients
public class GulimallSearchApplication {
//    private static final RequestOptions COMMON_OPTIONS;
//    static {
//        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
////        builder.addHeader("Authorization", "Bearer " + TOKEN);
////        builder.setHttpAsyncResponseConsumerFactory(
////                new HttpAsyncResponseConsumerFactory
////                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
//        COMMON_OPTIONS = builder.build();
//    }

    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }

}
