package com.similar_products.recommend.client;

import com.similar_products.recommend.adapter.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestTemplate restTemplate;

    @Value("${PRODUCT_SERVICE_BASE_URL}")
    private String PRODUCT_SERVICE_BASE_URL;

    @Value("${CLIENT_ERROR_MESSAGE}")
    private String CLIENT_ERROR_MESSAGE;

    private String SIMILAR_PRODUCT_IDS_ENDPOINT;
    private String PRODUCT_DETAIL_ENDPOINT;

    @PostConstruct
    public void postConstruct() {
        SIMILAR_PRODUCT_IDS_ENDPOINT = PRODUCT_SERVICE_BASE_URL +"/product/{productId}/similarids";
        PRODUCT_DETAIL_ENDPOINT = PRODUCT_SERVICE_BASE_URL +"/product/{productId}";
    }

    public String[] getSimilarProductIdsById(String productId) {
        try {
            ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(SIMILAR_PRODUCT_IDS_ENDPOINT, String[].class, productId);
            return responseEntity.getBody();
        } catch(HttpStatusCodeException e) {
             if (e.getStatusCode().is4xxClientError()) {
                 throwClientError(productId, e);
             } else {
                 throw e;
             }
            return null;
        }
    }

    public ProductDTO getProductDetailById(String productId) {
        try {
            return restTemplate.getForObject(PRODUCT_DETAIL_ENDPOINT, ProductDTO.class, productId);
        } catch(HttpStatusCodeException e) {
             if (e.getStatusCode().is4xxClientError()) {
                 throwClientError(productId, e);
             } else {
                 throw e;
             }
            return null;
        }
    }

    private void throwClientError(String productId, HttpStatusCodeException e) {
        throw new ProductServiceClientException((CLIENT_ERROR_MESSAGE).formatted(productId, e.getResponseBodyAsString()), e.getStatusCode());
    }
}
