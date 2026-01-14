package com.similar_products.recommend.adapter;

import com.similar_products.recommend.client.ProductDTO;
import com.similar_products.recommend.client.ProductServiceClient;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ProductServiceAdapter {
    private final ProductServiceClient productServiceClient;

    /**
     * Returns a List of similar Products to the given Product ID parameter.
     * @param productId The requested Product ID.
     * @return List of Products as ProductDTO class objects.
     */
    public List<ProductDTO> getSimilarProductsByProductId(String productId) {
        String[] similarProductIds = productServiceClient.getSimilarProductIdsById(productId);
        return Arrays.stream(similarProductIds)
                .filter(Objects::nonNull)
                .map(productServiceClient::getProductDetailById)
                .filter(Objects::nonNull)
                .toList();
    }
}
