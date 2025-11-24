package org.badr.chatbot.feign;

import org.badr.chatbot.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerServiceRestClient {

    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable Long id);

    @GetMapping("/customers")
    PagedModel<Customer> findAllCustomers();

    @GetMapping("/customers/search/findByName")
    Customer findCustomerByName(@RequestParam("name") String name);

    @GetMapping("/customers/search/findByEmail")
    Customer findCustomerByEmail(@RequestParam("email") String email);

    @PutMapping("/customers/{id}")
    Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer);

    @PatchMapping("/customers/{id}")
    Customer patchCustomer(@PathVariable Long id, @RequestBody Customer customer);

    @DeleteMapping("/customers/{id}")
    void deleteCustomerById(@PathVariable Long id);
}