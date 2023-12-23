package com.zh.controlcenter.common.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Describle This Class Is RedisConfig
 * @Author ZengMin
 * @Date 2018/8/28 10:54
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * GuavaCache
     *
     * @return
     */
    @Bean
    public Cache<String, Integer> cache() {
        return CacheBuilder.newBuilder().maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.SECONDS)         // 写入后30s删除
                .expireAfterAccess(5, TimeUnit.SECONDS)        // 5s不访问删除
                .initialCapacity(1)    // 设置初始容量为1
                .concurrencyLevel(10) // 设置并发级别为10
                .recordStats() // 开启缓存统计
                .build();
    }
}
