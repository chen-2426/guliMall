package com.chen.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/11 9:49
 * @description
 */
@Configuration
public class MythreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadProperties threadProperties){
        return new ThreadPoolExecutor(
                threadProperties.getCoreSize(),
                threadProperties.getMaxSize(),
                threadProperties.getKeepAliveTime(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
