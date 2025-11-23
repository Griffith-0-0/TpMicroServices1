package org.badr.inventoryservice;

import org.badr.inventoryservice.entites.Product;
import org.badr.inventoryservice.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class ProductRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        productRepository.deleteAll();
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Given
        productRepository.save(Product.builder().name("Laptop").price(1500.0).quantity(10).build());

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$._embedded.products[0].name", notNullValue()))
                .andExpect(jsonPath("$._embedded.products[0].price", notNullValue()))
                .andExpect(jsonPath("$._embedded.products[0].quantity", notNullValue()));
    }

    @Test
    void testGetProductById() throws Exception {
        // Given
        Product product = productRepository.save(
                Product.builder().name("Smartphone").price(800.0).quantity(5).build()
        );

        // When & Then
        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Smartphone")))
                .andExpect(jsonPath("$.price", is(800.0)))
                .andExpect(jsonPath("$.quantity", is(5)));
    }

    @Test
    void testCreateProduct() throws Exception {
        // Given
        String productJson = """
                {
                    "name": "Tablet",
                    "price": 500.0,
                    "quantity": 15
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Tablet")))
                .andExpect(jsonPath("$.price", is(500.0)))
                .andExpect(jsonPath("$.quantity", is(15)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Given
        Product product = productRepository.save(
                Product.builder().name("Old Product").price(100.0).quantity(1).build()
        );

        String updatedJson = """
                {
                    "name": "Updated Product",
                    "price": 200.0,
                    "quantity": 2
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(200.0)))
                .andExpect(jsonPath("$.quantity", is(2)));
    }

    @Test
    void testDeleteProduct() throws Exception {
        // Given
        Product product = productRepository.save(
                Product.builder().name("To Delete").price(50.0).quantity(1).build()
        );

        // When & Then
        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}

