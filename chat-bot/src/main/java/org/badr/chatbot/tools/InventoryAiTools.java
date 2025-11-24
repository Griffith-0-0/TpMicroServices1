package org.badr.chatbot.tools;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.badr.chatbot.feign.InventoryServiceRestClient;
import org.badr.chatbot.model.Product;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Component
public class InventoryAiTools {
    private final InventoryServiceRestClient inventoryServiceRestClient;

    public InventoryAiTools(InventoryServiceRestClient inventoryServiceRestClient) {
        this.inventoryServiceRestClient = inventoryServiceRestClient;
    }

    @Tool(name = "getProduct", description = "get product by id")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getDefaultProduct")
    public Product getProduct(@ToolParam(description = "product id") Long id) {
        return inventoryServiceRestClient.getProductById(id);
    }

    @Tool(name = "getAllProducts", description = "get all products")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getAllProductsFallback")
    public PagedModel<Product> getAllProducts() {
        return inventoryServiceRestClient.findAllProducts();
    }

    @Tool(name = "getProductByName", description = "search product by name")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getDefaultProductByName")
    public Product getProductByName(@ToolParam(description = "product name") String name) {
        return inventoryServiceRestClient.findProductByName(name);
    }

    @Tool(name = "updateProduct", description = "update product information completely")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductFallback")
    public Product updateProduct(
            @ToolParam(description = "product id") Long id,
            @ToolParam(description = "updated product data") Product product) {
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @Tool(name = "updateProductName", description = "update only product name")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductNameFallback")
    public Product updateProductName(
            @ToolParam(description = "product id") Long id,
            @ToolParam(description = "new name") String name) {
        Product product = inventoryServiceRestClient.getProductById(id);
        product.setName(name);
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @Tool(name = "updateProductPrice", description = "update only product price")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductPriceFallback")
    public Product updateProductPrice(
            @ToolParam(description = "product id") Long id,
            @ToolParam(description = "new price") Double price) {
        Product product = inventoryServiceRestClient.getProductById(id);
        product.setPrice(price);
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @Tool(name = "updateProductQuantity", description = "update only product quantity")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductQuantityFallback")
    public Product updateProductQuantity(
            @ToolParam(description = "product id") Long id,
            @ToolParam(description = "new quantity") Integer quantity) {
        Product product = inventoryServiceRestClient.getProductById(id);
        product.setQuantity(quantity);
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @Tool(name = "deleteProductById", description = "delete product by id")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "deleteProductByIdFallback")
    public String deleteProductById(@ToolParam(description = "product id") Long id) {
        inventoryServiceRestClient.deleteProductById(id);
        return "Product with id " + id + " deleted successfully";
    }

    @Tool(name = "deleteProductByName", description = "delete product by name")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "deleteProductByNameFallback")
    public String deleteProductByName(@ToolParam(description = "product name") String name) {
        Product product = inventoryServiceRestClient.findProductByName(name);
        if (product != null && product.getId() != null) {
            inventoryServiceRestClient.deleteProductById(product.getId());
            return "Product " + name + " deleted successfully";
        }
        return "Product " + name + " not found";
    }

    // Fallback methods
    private Product getDefaultProduct(Long id, Exception e) {
        Product product = new Product();
        product.setId(id);
        product.setName("default");
        product.setPrice(0.0);
        product.setQuantity(0);
        return product;
    }

    private PagedModel<Product> getAllProductsFallback(Exception e) {
        return PagedModel.empty();
    }

    private Product getDefaultProductByName(String name, Exception e) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(0.0);
        product.setQuantity(0);
        return product;
    }

    private Product updateProductFallback(Long id, Product product, Exception e) {
        return product;
    }

    private Product updateProductNameFallback(Long id, String name, Exception e) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(0.0);
        product.setQuantity(0);
        return product;
    }

    private Product updateProductPriceFallback(Long id, Double price, Exception e) {
        Product product = new Product();
        product.setId(id);
        product.setName("update-failed");
        product.setPrice(price);
        product.setQuantity(0);
        return product;
    }

    private Product updateProductQuantityFallback(Long id, Integer quantity, Exception e) {
        Product product = new Product();
        product.setId(id);
        product.setName("update-failed");
        product.setPrice(0.0);
        product.setQuantity(quantity);
        return product;
    }

    private String deleteProductByIdFallback(Long id, Exception e) {
        return "Failed to delete product with id " + id + ": " + e.getMessage();
    }

    private String deleteProductByNameFallback(String name, Exception e) {
        return "Failed to delete product " + name + ": " + e.getMessage();
    }
}