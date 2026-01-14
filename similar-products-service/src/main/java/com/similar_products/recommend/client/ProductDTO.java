package com.similar_products.recommend.client;

import java.math.BigDecimal;

public record ProductDTO(String id, String name, BigDecimal price, Boolean availability)
{}
