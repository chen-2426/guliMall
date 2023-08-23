package com.chen.gulimall.product.config;

//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/3/13 20:51
 * @description
 */
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfig {
    @Bean
    public PaginationInterceptor mybatisPlusInterceptor(){
        PaginationInterceptor mybatisPlusInterceptor = new PaginationInterceptor();
        return mybatisPlusInterceptor;
    }
}
