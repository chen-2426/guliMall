package com.chen.gulimall.order.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/11 9:49
 * @description
 */
@Configuration
@EnableConfigurationProperties(ThreadProperties.class)
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
