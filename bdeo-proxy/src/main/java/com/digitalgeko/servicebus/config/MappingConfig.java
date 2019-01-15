package com.digitalgeko.servicebus.config;

import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MappingConfig {

    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapper dozerBean() {
        return (DozerBeanMapper) DozerBeanMapperSingletonWrapper.getInstance();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
