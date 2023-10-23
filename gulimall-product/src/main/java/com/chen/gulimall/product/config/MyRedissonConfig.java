package com.chen.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/3 15:50
 * @description
 */
@Configuration
public class MyRedissonConfig {
    /**
     * 所有对Redission的使用都是通过RedissionClient对象；
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://8.130.64.110");

        return Redisson.create(config);
    }
}
