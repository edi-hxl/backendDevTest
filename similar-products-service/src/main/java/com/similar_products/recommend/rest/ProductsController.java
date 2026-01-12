package com.similar_products.recommend.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductsController {
    RestTemplate template = new RestTemplate();

    @GetMapping("/{productId}/similar")
    public List<ProductDTO> findSimilarProductsByProductId(@PathVariable String productId) {
// exception handling ... maybe RestEntity instead?
        String[] similarIds = template.getForObject("http://localhost:3001/product/{productId}/similarids", String[].class, productId);

        List<ProductDTO> similarProducts = Arrays.stream(similarIds)
                .map(id -> template.getForObject("http://localhost:3001/product/{id}", ProductDTO.class, id))
                .toList();

        return similarProducts;
    }
}
