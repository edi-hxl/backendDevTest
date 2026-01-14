package com.similar_products.recommend.adapter;

import com.similar_products.recommend.client.ProductDTO;
import com.similar_products.recommend.client.ProductServiceClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ProductServiceAdapterTest {
    private static ProductServiceAdapter adapter;
    private static ProductServiceClient productServiceClient;

    @BeforeAll
    public static void setup() {
        productServiceClient = Mockito.mock(ProductServiceClient.class);
        adapter = new ProductServiceAdapter(productServiceClient);
    }

    @Test
    public void getSimilarProductsByProductId_whenNoError_returnsCorrectly() {
        String[] expectedIds = new String[]{"2", "3"};
        when(productServiceClient.getSimilarProductIdsById(eq("1")))
                .thenReturn(expectedIds);
        ProductDTO productDTO2 = new ProductDTO("2", "NAME_2", BigDecimal.ONE, false);
        ProductDTO productDTO3 = new ProductDTO("3", "NAME_3", BigDecimal.TWO, true);
        List<ProductDTO> expected = List.of(productDTO2, productDTO3);

        when(productServiceClient.getProductDetailById(eq("2")))
                .thenReturn(productDTO2);
        when(productServiceClient.getProductDetailById(eq("3")))
                .thenReturn(productDTO3);

        List<ProductDTO> result = adapter.getSimilarProductsByProductId("1");

        assertEquals(expected, result, "Similar Products Adapter Method is broken!");
    }

    @Test
    public void getSimilarProductsByProductId_whenIdsEmpty_returnsCorrectly() {
        String[] expectedIds = new String[]{};
        when(productServiceClient.getSimilarProductIdsById(eq("1")))
                .thenReturn(expectedIds);

        List<ProductDTO> result = adapter.getSimilarProductsByProductId("1");

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void getSimilarProductsByProductId_whenIdsNull_returnsCorrectly() {
        String[] expectedIds = new String[]{null, null};
        when(productServiceClient.getSimilarProductIdsById(eq("1")))
                .thenReturn(expectedIds);

        List<ProductDTO> result = adapter.getSimilarProductsByProductId("1");

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void getSimilarProductsByProductId_whenDetailNull_returnsCorrectly() {
        String[] expectedIds = new String[]{"2", "3"};
        when(productServiceClient.getSimilarProductIdsById(eq("1")))
                .thenReturn(expectedIds);
        ProductDTO productDTO2 = new ProductDTO("2", "NAME_2", BigDecimal.ONE, false);
        List<ProductDTO> expected = List.of(productDTO2);

        when(productServiceClient.getProductDetailById(eq("2")))
                .thenReturn(productDTO2);
        when(productServiceClient.getProductDetailById(eq("3")))
                .thenReturn(null);

        List<ProductDTO> result = adapter.getSimilarProductsByProductId("1");

        assertEquals(expected, result, "Similar Products Adapter Method is broken!");
    }

}
