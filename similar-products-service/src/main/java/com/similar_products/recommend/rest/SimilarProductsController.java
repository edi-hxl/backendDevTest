package com.similar_products.recommend.rest;

import com.similar_products.recommend.adapter.ProductDTO;
import com.similar_products.recommend.adapter.ProductServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class SimilarProductsController {
    private final ProductServiceAdapter productServiceAdapter;

    @GetMapping("/{productId}/similar")
    public List<ProductDTO> findSimilarProductsByProductId(@PathVariable String productId) {
        return productServiceAdapter.getSimilarProductsByProductId(productId);
    }
}
