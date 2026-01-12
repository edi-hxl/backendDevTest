package com.similar_products.recommend.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private Boolean availability;
}
