package org.badr.mcpserver.tools;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.badr.mcpserver.feign.InventoryServiceRestClient;
import org.badr.mcpserver.model.Product;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Component
public class InventoryAiTools {
    private final InventoryServiceRestClient inventoryServiceRestClient;

    public InventoryAiTools(InventoryServiceRestClient inventoryServiceRestClient) {
        this.inventoryServiceRestClient = inventoryServiceRestClient;
    }

    @McpTool(name = "getProduct", description = "get product by id")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getDefaultProduct")
    public Product getProduct(@McpArg(description = "product id") Long id) {
        return inventoryServiceRestClient.getProductById(id);
    }

    @McpTool(name = "getAllProducts", description = "get all products")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getAllProductsFallback")
    public PagedModel<Product> getAllProducts() {
        return inventoryServiceRestClient.findAllProducts();
    }

    @McpTool(name = "getProductByName", description = "search product by name")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getDefaultProductByName")
    public Product getProductByName(@McpArg(description = "product name") String name) {
        return inventoryServiceRestClient.findProductByName(name);
    }

    @McpTool(name = "updateProduct", description = "update product information completely")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductFallback")
    public Product updateProduct(
            @McpArg(description = "product id") Long id,
            @McpArg(description = "updated product data") Product product) {
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @McpTool(name = "updateProductName", description = "update only product name")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductNameFallback")
    public Product updateProductName(
            @McpArg(description = "product id") Long id,
            @McpArg(description = "new name") String name) {
        Product product = inventoryServiceRestClient.getProductById(id);
        product.setName(name);
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @McpTool(name = "updateProductPrice", description = "update only product price")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductPriceFallback")
    public Product updateProductPrice(
            @McpArg(description = "product id") Long id,
            @McpArg(description = "new price") Double price) {
        Product product = inventoryServiceRestClient.getProductById(id);
        product.setPrice(price);
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @McpTool(name = "updateProductQuantity", description = "update only product quantity")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "updateProductQuantityFallback")
    public Product updateProductQuantity(
            @McpArg(description = "product id") Long id,
            @McpArg(description = "new quantity") Integer quantity) {
        Product product = inventoryServiceRestClient.getProductById(id);
        product.setQuantity(quantity);
        return inventoryServiceRestClient.updateProduct(id, product);
    }

    @McpTool(name = "deleteProductById", description = "delete product by id")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "deleteProductByIdFallback")
    public String deleteProductById(@McpArg(description = "product id") Long id) {
        inventoryServiceRestClient.deleteProductById(id);
        return "Product with id " + id + " deleted successfully";
    }

    @McpTool(name = "deleteProductByName", description = "delete product by name")
    @CircuitBreaker(name = "inventory-service", fallbackMethod = "deleteProductByNameFallback")
    public String deleteProductByName(@McpArg(description = "product name") String name) {
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