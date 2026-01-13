package com.similar_products.recommend.client;

import com.similar_products.recommend.adapter.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static com.similar_products.recommend.config.WebComponentsConfiguration.PRODUCT_DETAIL_CACHE_NAME;
import static com.similar_products.recommend.config.WebComponentsConfiguration.SIMILAR_PRODUCT_IDS_CACHE_NAME;

@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestTemplate restTemplate;
    private final CacheManager cacheManager;

    @Value("${PRODUCT_SERVICE_BASE_URL}")
    private String PRODUCT_SERVICE_BASE_URL;

    @Value("${CLIENT_ERROR_MESSAGE}")
    private String CLIENT_ERROR_MESSAGE;

    private String SIMILAR_PRODUCT_IDS_ENDPOINT;
    private String PRODUCT_DETAIL_ENDPOINT;
    private Cache similarProductIdsCache;
    private Cache productDetailCache;

    @PostConstruct
    public void postConstruct() {
        SIMILAR_PRODUCT_IDS_ENDPOINT = PRODUCT_SERVICE_BASE_URL +"/product/{productId}/similarids";
        PRODUCT_DETAIL_ENDPOINT = PRODUCT_SERVICE_BASE_URL +"/product/{productId}";
        similarProductIdsCache = cacheManager.getCache(SIMILAR_PRODUCT_IDS_CACHE_NAME);
        productDetailCache = cacheManager.getCache(PRODUCT_DETAIL_CACHE_NAME);
    }

    @AllArgsConstructor
    @Getter
    private class SimilarProductIdsCacheValue {
        private final String[] value;
        private final String eTag;
    }

    @AllArgsConstructor
    @Getter
    private class ProductDetailCacheValue {
        private final ProductDTO value;
        private final String eTag;
    }

    public String[] getSimilarProductIdsById(String productId) {
        SimilarProductIdsCacheValue result = similarProductIdsCache.get(productId, SimilarProductIdsCacheValue.class);
        if (result != null) {
            return getLatestSimilarProductIds(productId, result);
        }
        try {
            ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(SIMILAR_PRODUCT_IDS_ENDPOINT, String[].class, productId);
            result = new SimilarProductIdsCacheValue(responseEntity.getBody(), responseEntity.getHeaders().getETag());
            similarProductIdsCache.putIfAbsent(productId, result);
        } catch(HttpStatusCodeException e) {
             if (e.getStatusCode().is4xxClientError()) {
                 throwClientError(productId, e);
             } else {
                 throw e;
             }
        }
        return result.getValue();
    }


    public ProductDTO getProductDetailById(String productId) {
        ProductDetailCacheValue result = productDetailCache.get(productId, ProductDetailCacheValue.class);
        if (result != null) {
            return getLatestProductDetail(productId, result);
        }
        try {
            ResponseEntity<ProductDTO> responseEntity = restTemplate.getForEntity(PRODUCT_DETAIL_ENDPOINT, ProductDTO.class, productId);
            result = new ProductDetailCacheValue(responseEntity.getBody(), responseEntity.getHeaders().getETag());
            productDetailCache.putIfAbsent(productId, result);
        } catch(HttpStatusCodeException e) {
             if (e.getStatusCode().is4xxClientError()) {
                 throwClientError(productId, e);
             } else {
                 throw e;
             }
        }
        return result.getValue();
    }


    private String[] getLatestSimilarProductIds(String productId, SimilarProductIdsCacheValue result) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.IF_NONE_MATCH, result.getETag());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String[]> possiblyUpdatedResponseEntity = restTemplate.exchange(SIMILAR_PRODUCT_IDS_ENDPOINT, HttpMethod.GET, requestEntity, String[].class, productId);
        if (HttpStatus.NOT_MODIFIED.isSameCodeAs(possiblyUpdatedResponseEntity.getStatusCode())) {
            return result.getValue();
        } else {
            SimilarProductIdsCacheValue updatedValue = new SimilarProductIdsCacheValue(possiblyUpdatedResponseEntity.getBody(),
                    possiblyUpdatedResponseEntity.getHeaders().getETag());
            similarProductIdsCache.putIfAbsent(productId, updatedValue);
            return updatedValue.getValue();
        }
    }

    private ProductDTO getLatestProductDetail(String productId, ProductDetailCacheValue result) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.IF_NONE_MATCH, result.getETag());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO> possiblyUpdatedResponseEntity = restTemplate.exchange(PRODUCT_DETAIL_ENDPOINT, HttpMethod.GET, requestEntity, ProductDTO.class, productId);
        if (HttpStatus.NOT_MODIFIED.isSameCodeAs(possiblyUpdatedResponseEntity.getStatusCode())) {
            return result.getValue();
        } else {
            ProductDetailCacheValue updatedValue = new ProductDetailCacheValue(possiblyUpdatedResponseEntity.getBody(),
                    possiblyUpdatedResponseEntity.getHeaders().getETag());
            similarProductIdsCache.putIfAbsent(productId, updatedValue);
            return updatedValue.getValue();
        }
    }

    private void throwClientError(String productId, HttpStatusCodeException e) {
        throw new ProductServiceClientException((CLIENT_ERROR_MESSAGE).formatted(productId, e.getResponseBodyAsString()), e.getStatusCode());
    }
}
