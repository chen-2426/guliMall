package com.chen.gulimall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/25 21:45
 * @description
 */
@EnableTransactionManagement
@MapperScan("com.chen.gulimall.ware.dao")
@Configuration
public class WareMyBatisConfig {
    @Bean
    public PaginationInterceptor mybatisPlusInterceptor() {
        PaginationInterceptor mybatisPlusInterceptor = new PaginationInterceptor();
        return mybatisPlusInterceptor;
    }
}

