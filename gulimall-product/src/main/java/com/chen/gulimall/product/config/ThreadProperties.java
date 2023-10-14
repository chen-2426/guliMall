package com.chen.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/11 10:03
 * @description
 */
@ConfigurationProperties(prefix = "product.thread")
@Component
@Data
public class ThreadProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;


}
