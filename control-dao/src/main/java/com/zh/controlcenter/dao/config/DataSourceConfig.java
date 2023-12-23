package com.zh.controlcenter.dao.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Describle This Class Is 数据源配置
 * @Author ZengMin
 * @Date 2019/3/15 19:01
 */
@Configuration
@MapperScan("com.zh.controlcenter.dao.mapper")
public class DataSourceConfig {

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
