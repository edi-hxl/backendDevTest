package com.similar_products.recommend.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProductServiceClientTest {
    private static ProductServiceClient productServiceClient;
    private static RestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
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

    @Test
    public void getSimilarProductIdsById_whenErrorInResponse_thenThrowsException() {
        HttpClientErrorException ex = HttpClientErrorException.create("ERROR MESSAGE", HttpStatusCode.valueOf(404), "STATUS TEXT", null, "ResponseBody".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        when(restTemplate.getForEntity(anyString(), any(Class.class), anyString())).thenThrow(ex);
        assertThrows(ProductServiceClientException.class, () -> productServiceClient.getSimilarProductIdsById("1L"));
    }

    @Test
    public void getProductDetailById_whenNoErrorInResponse_thenSuccessful() {
        ProductDTO expectedResult = new ProductDTO("1L","Product Name 1",BigDecimal.ONE,true);
        ResponseEntity<ProductDTO> responseEntity = new ResponseEntity<>(expectedResult, HttpStatusCode.valueOf(200));

        when(restTemplate.getForEntity(anyString(), any(Class.class), anyString())).thenReturn(responseEntity);
        ProductDTO result = productServiceClient.getProductDetailById("1L");

        assertEquals(expectedResult, result);
    }

    @Test
    public void getProductDetailById_whenErrorInResponse_thenThrowsException() {
        HttpClientErrorException ex = HttpClientErrorException.create("ERROR MESSAGE", HttpStatusCode.valueOf(404), "STATUS TEXT", null, "ResponseBody".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        when(restTemplate.getForEntity(anyString(), any(Class.class), anyString())).thenThrow(ex);
        assertThrows(ProductServiceClientException.class, () -> productServiceClient.getProductDetailById("1L"));
    }

}
