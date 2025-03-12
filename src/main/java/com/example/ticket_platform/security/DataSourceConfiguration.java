package com.example.ticket_platform.security;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/milestone_4")
                .username("Paolo")
                .password("aGbHigy7")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

}
