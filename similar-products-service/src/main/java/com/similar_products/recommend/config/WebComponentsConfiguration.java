package com.similar_products.recommend.config;

import com.similar_products.recommend.adapter.ProductServiceAdapter;
import com.similar_products.recommend.client.ProductServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class WebComponentsConfiguration {
    public static final String SIMILAR_PRODUCT_IDS_CACHE_NAME = "similar_products_cache";
    public static final String PRODUCT_DETAIL_CACHE_NAME = "product_detail_cache";

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
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager(SIMILAR_PRODUCT_IDS_CACHE_NAME, PRODUCT_DETAIL_CACHE_NAME);
//        concurrentMapCacheManager.
        return concurrentMapCacheManager;
    }
}
