package com.similar_products.recommend.rest;

import com.similar_products.recommend.adapter.ProductServiceAdapter;
import com.similar_products.recommend.client.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = SimilarProductsController.class)
@AutoConfigureRestTestClient
public class SimilarProductsControllerTest {
    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private ProductServiceAdapter productServiceAdapter;


    @Test
    public void findSimilarProductsByProductIdTest() {
        ProductDTO productDTO = new ProductDTO("1", "NAME_1", BigDecimal.ONE, true);
        List<ProductDTO> productDTOList = List.of(productDTO);
        given(productServiceAdapter.getSimilarProductsByProductId(eq("1")))
                .willReturn(productDTOList);

        restTestClient.get().uri("/product/1/similar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                    [
                      {
                        "id": "1",
                        "name": "NAME_1",
                        "price": 1.0,
                        "availability": true
                      }
                    ]
                    """);
    }

}
