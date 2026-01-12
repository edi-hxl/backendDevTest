package com.similar_products.recommend.client;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public class ProductServiceClientException extends RuntimeException {
    @Getter
    private final HttpStatusCode httpStatusCode;

    public ProductServiceClientException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
