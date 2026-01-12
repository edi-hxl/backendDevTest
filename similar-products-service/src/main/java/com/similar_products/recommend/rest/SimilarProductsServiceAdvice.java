package com.similar_products.recommend.rest;

import com.similar_products.recommend.client.ProductServiceClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SimilarProductsServiceAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductServiceClientException.class)
    public ProblemDetail handleProductServiceClientException(ProductServiceClientException ex) {
        return ProblemDetail.forStatusAndDetail(ex.getHttpStatusCode(), ex.getMessage());
    }
}
