package org.badr.chatbot.feign;

import org.badr.chatbot.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryServiceRestClient {

    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable Long id);

    @GetMapping("/products")
    PagedModel<Product> findAllProducts();

    @GetMapping("/products/search/findByName")
    Product findProductByName(@RequestParam("name") String name);

    @PutMapping("/products/{id}")
    Product updateProduct(@PathVariable Long id, @RequestBody Product product);

    @PatchMapping("/products/{id}")
    Product patchProduct(@PathVariable Long id, @RequestBody Product product);

    @DeleteMapping("/products/{id}")
    void deleteProductById(@PathVariable Long id);
}