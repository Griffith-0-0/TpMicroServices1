package org.badr.billingservice.feign;

import org.badr.billingservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerServiceRestClient {

    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable Long id);
}
