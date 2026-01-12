package com.similar_products.recommend.adapter;

import com.similar_products.recommend.client.ProductServiceClient;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ProductServiceAdapter {
    private final ProductServiceClient productServiceClient;

    public List<ProductDTO> getSimilarProductsByProductId(String productId) {
        String[] similarProductIds = productServiceClient.getSimilarProductIdsById(productId);
        return Arrays.stream(similarProductIds)
                .map(productServiceClient::getProductDetailById)
                .toList();
    }
}
