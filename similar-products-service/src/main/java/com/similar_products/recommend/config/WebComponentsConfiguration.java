package com.similar_products.recommend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.similar_products.recommend.adapter.ProductServiceAdapter;
import com.similar_products.recommend.client.ProductServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
public class WebComponentsConfiguration {
    public static final String SIMILAR_PRODUCT_IDS_CACHE_NAME = "similar_products_cache";
    public static final String PRODUCT_DETAIL_CACHE_NAME = "product_detail_cache";
    public static final String SIMILAR_PRODUCTS_API_RESPONSE_CACHE_NAME = "similar_products_api_response_cache";

    @Value("${PRODUCT_SERVICE_BASE_URL}")
    private String PRODUCT_SERVICE_BASE_URL;

    @Value("${CLIENT_ERROR_MESSAGE}")
    private String CLIENT_ERROR_MESSAGE;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ProductServiceClient productServiceClient() {
        return new ProductServiceClient(restTemplate(), PRODUCT_SERVICE_BASE_URL, CLIENT_ERROR_MESSAGE);
    }

    @Bean
    public ProductServiceAdapter productServiceAdapter() {
        return new ProductServiceAdapter(productServiceClient());
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();

        manager.setCaches(List.of(
                new CaffeineCache(
                        SIMILAR_PRODUCTS_API_RESPONSE_CACHE_NAME,
                        Caffeine.newBuilder()
                                .expireAfterAccess(10, TimeUnit.MINUTES)
                                .maximumSize(10_000)
                                .build()
                ),
                new CaffeineCache(
                        SIMILAR_PRODUCT_IDS_CACHE_NAME,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .maximumSize(20_000)
                                .build()
                ),
                new CaffeineCache(
                        PRODUCT_DETAIL_CACHE_NAME,
                        Caffeine.newBuilder()
                                .expireAfterWrite(15, TimeUnit.MINUTES)
                                .maximumSize(50_000)
                                .build()
                )
        ));
        return manager;
    }
}
