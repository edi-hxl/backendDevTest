package com.similar_products.recommend.rest;

import com.similar_products.recommend.adapter.ProductServiceAdapter;
import com.similar_products.recommend.client.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.similar_products.recommend.config.WebComponentsConfiguration.SIMILAR_PRODUCTS_API_RESPONSE_CACHE_NAME;

@RestController
@RequiredArgsConstructor
public class SimilarProductsController {
    private final ProductServiceAdapter productServiceAdapter;

    @Cacheable(cacheNames = SIMILAR_PRODUCTS_API_RESPONSE_CACHE_NAME, sync = true)
    @GetMapping("/product/{productId}/similar")
    public List<ProductDTO> findSimilarProductsByProductId(@PathVariable String productId) {
        return productServiceAdapter.getSimilarProductsByProductId(productId);
    }
}
