package com.similar_products.recommend.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProductServiceClientTest {
    private static ProductServiceClient productServiceClient;
    private static RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        productServiceClient = new ProductServiceClient(restTemplate,
                "BASE-URL",
                "Product Service Client Exception getting similar Product Ids for productId='%s'. Response Body='%s'");
    }

    @Test
    public void getSimilarProductIdsById_whenNoErrorInResponse_thenSuccessful() {
        String[] expectedResult = new String[]{"1", "2"};
        ResponseEntity<String[]> responseEntity = new ResponseEntity<>(expectedResult, HttpStatusCode.valueOf(200));

        when(restTemplate.getForEntity(anyString(), any(Class.class), anyString())).thenReturn(responseEntity);
        String[] result = productServiceClient.getSimilarProductIdsById("1L");

        assertArrayEquals(expectedResult, result);
    }

}
