package org.badr.inventoryservice;

import org.badr.inventoryservice.entites.Product;
import org.badr.inventoryservice.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder().name("pc").price(35000).quantity(10).build());
            productRepository.save(Product.builder().name("phone").price(11000).quantity(20).build());
            productRepository.save(Product.builder().name("printer").price(3000).quantity(6).build());
            productRepository.save(Product.builder().name("watch").price(2000).quantity(9).build());
        };
    }

}
