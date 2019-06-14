package com.digitalgeko.servicebus.config;

import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class MappingConfig {

    protected static final Logger log = LoggerFactory.getLogger(MappingConfig.class);

    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapper dozerBean() {
        return (DozerBeanMapper) DozerBeanMapperSingletonWrapper.getInstance();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        /*SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.2.0.15", 8080));
        clientHttpReq.setProxy(proxy);
        RestTemplate restTemplate = new RestTemplate(clientHttpReq);*/
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
