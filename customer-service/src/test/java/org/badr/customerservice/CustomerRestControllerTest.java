package org.badr.customerservice;

import org.badr.customerservice.entities.Customer;
import org.badr.customerservice.repositories.CustomerRepository;
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
class CustomerRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        customerRepository.deleteAll();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        // Given
        customerRepository.save(Customer.builder().name("Test Customer").email("test@example.com").build());

        // When & Then
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customers", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$._embedded.customers[0].name", notNullValue()))
                .andExpect(jsonPath("$._embedded.customers[0].email", notNullValue()));
    }

    @Test
    void testGetCustomerById() throws Exception {
        // Given
        Customer customer = customerRepository.save(
                Customer.builder().name("John Doe").email("john@example.com").build()
        );

        // When & Then
        mockMvc.perform(get("/api/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customer.getId().intValue())))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void testCreateCustomer() throws Exception {
        // Given
        String customerJson = """
                {
                    "name": "New Customer",
                    "email": "new@example.com"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Customer")))
                .andExpect(jsonPath("$.email", is("new@example.com")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // Given
        Customer customer = customerRepository.save(
                Customer.builder().name("Original Name").email("original@example.com").build()
        );

        String updatedJson = """
                {
                    "name": "Updated Name",
                    "email": "updated@example.com"
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        // Given
        Customer customer = customerRepository.save(
                Customer.builder().name("To Delete").email("delete@example.com").build()
        );

        // When & Then
        mockMvc.perform(delete("/api/customers/" + customer.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/customers/" + customer.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCustomerNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound());
    }
}

