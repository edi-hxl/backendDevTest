package com.similar_products.recommend.client;

import lombok.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static com.similar_products.recommend.config.WebComponentsConfiguration.PRODUCT_DETAIL_CACHE_NAME;
import static com.similar_products.recommend.config.WebComponentsConfiguration.SIMILAR_PRODUCT_IDS_CACHE_NAME;
import static java.util.Objects.isNull;

public class ProductServiceClient {
    private final RestTemplate restTemplate;
    private final String clientErrorMessage;
    private final String SIMILAR_PRODUCT_IDS_ENDPOINT;
    private final String PRODUCT_DETAIL_ENDPOINT;

    public ProductServiceClient(RestTemplate restTemplate,
                                String productServiceBaseUrl,
                                String clientErrorMessage) {
        this.restTemplate = restTemplate;
        this.clientErrorMessage = clientErrorMessage;
        this.SIMILAR_PRODUCT_IDS_ENDPOINT = productServiceBaseUrl + "/product/{productId}/similarids";
        this.PRODUCT_DETAIL_ENDPOINT = productServiceBaseUrl + "/product/{productId}";
    }

    /**
     * Returns similar Product IDs to the given Product ID parameter.
     * @param productId the ID of the Product for which similar Products will be returned.
     * @return Array of the similar Products' IDs.
     */
    @Cacheable(cacheNames = SIMILAR_PRODUCT_IDS_CACHE_NAME)
    public @NonNull String[] getSimilarProductIdsById(String productId) {
        try {
            ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(SIMILAR_PRODUCT_IDS_ENDPOINT, String[].class, productId);
            String[] responseBody = responseEntity.getBody();

            if (isNull(responseBody)) {
                return new String[]{};
            }
            return responseBody;
        } catch(HttpStatusCodeException e) {
             if (e.getStatusCode().is4xxClientError()) {
                 throwClientError(productId, e);
             } else {
                 throw e;
             }
            return null;
        }
    }

    /**
     * Returns the Product Details of the given Product ID parameter as a ProductDTO object.
     * @param productId requested Product ID.
     * @return ProductDTO or else throws exception.
     */
    @Cacheable(cacheNames = PRODUCT_DETAIL_CACHE_NAME)
    public ProductDTO getProductDetailById(String productId) {
        try {
            ResponseEntity<ProductDTO> responseEntity = restTemplate.getForEntity(PRODUCT_DETAIL_ENDPOINT, ProductDTO.class, productId);
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

    private void throwClientError(String productId, HttpStatusCodeException e) {
        throw new ProductServiceClientException((clientErrorMessage).formatted(productId, e.getResponseBodyAsString()), e.getStatusCode());
    }
}
