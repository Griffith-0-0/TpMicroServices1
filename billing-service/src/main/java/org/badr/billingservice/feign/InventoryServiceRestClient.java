package org.badr.billingservice.feign;

import org.badr.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
public interface InventoryServiceRestClient {
    @GetMapping("/products/{id}")
    Product findProductById(@PathVariable Long id);
}
