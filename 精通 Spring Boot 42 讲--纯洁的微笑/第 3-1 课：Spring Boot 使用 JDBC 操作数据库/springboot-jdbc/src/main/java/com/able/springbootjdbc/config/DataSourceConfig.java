package com.able.springbootjdbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author jipeng
 * @date 2019-03-06 17:14
 * @description
 */
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate (
            DataSource primaryDataSource ) {
        return new JdbcTemplate(primaryDataSource);
    }

    @Bean(name="secondaryJdbcTemplate")
    public JdbcTemplate  secondaryJdbcTemplate(
            DataSource secondaryDataSource) {
        return new JdbcTemplate(secondaryDataSource);
    }

}

