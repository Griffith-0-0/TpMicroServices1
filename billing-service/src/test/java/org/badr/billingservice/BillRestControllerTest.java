package org.badr.billingservice;

import org.badr.billingservice.entities.Bill;
import org.badr.billingservice.entities.ProductItem;
import org.badr.billingservice.repositories.BillRepository;
import org.badr.billingservice.repositories.ProductItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class BillRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        productItemRepository.deleteAll();
        billRepository.deleteAll();
    }

    @Test
    void testGetBillById() throws Exception {
        // Given
        Bill bill = new Bill();
        bill.setBillDate(new Date());
        bill.setCustomerId(1L);
        bill = billRepository.save(bill);

        ProductItem productItem = new ProductItem();
        productItem.setProductId(1L);
        productItem.setPrice(1000.0);
        productItem.setQuantity(2);
        productItem.setBill(bill);
        productItemRepository.save(productItem);

        // When & Then
        // Note: This test will fail if customer-service and inventory-service are not running
        // In a real scenario, you would mock the Feign clients
        mockMvc.perform(get("/api/bills/" + bill.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customerId", is(1)));
    }

    @Test
    void testGetBillNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/bills/999"))
                .andExpect(status().is5xxServerError());
    }
}

