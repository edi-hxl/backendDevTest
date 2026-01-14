package com.similar_products.recommend.rest;

import com.similar_products.recommend.client.ProductServiceClientException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@RestControllerAdvice
public class SimilarProductsServiceAdvice {

    @ExceptionHandler(ProductServiceClientException.class)
    public ResponseEntity<ProblemDetail> handleProductServiceClientException(ProductServiceClientException ex) {
        return ResponseEntity.status(ex.getHttpStatusCode())
                .body(ProblemDetail.forStatusAndDetail(ex.getHttpStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ProblemDetail> handleHttpStatusCodeException(HttpStatusCodeException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(500))
                .body(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), ex.getMessage()));
    }


}
