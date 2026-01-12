package com.similar_products.recommend.config;

import com.similar_products.recommend.adapter.ProductServiceAdapter;
import com.similar_products.recommend.client.ProductServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebComponentsConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ProductServiceClient productServiceClient() {
        return new ProductServiceClient(restTemplate());
    }

    @Bean
    public ProductServiceAdapter productServiceAdapter() {
        return new ProductServiceAdapter(productServiceClient());
    }
}
