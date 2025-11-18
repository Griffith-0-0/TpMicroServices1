package org.badr.billingservice;

import org.badr.billingservice.entities.Bill;
import org.badr.billingservice.entities.ProductItem;
import org.badr.billingservice.model.Product;
import org.badr.billingservice.repositories.BillRepository;
import org.badr.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BillRepository billRepository, ProductItemRepository pItemRepository) {
        return args -> {
            List<Long> customerIds= List.of(1L, 2L, 3L);
            List<Long> productIds= List.of(1L, 2L, 3L);
            customerIds.forEach(customerId -> {
                Bill bill = new Bill();
                bill.setBillDate(new Date());
                billRepository.save(bill);
                productIds.forEach(productId -> {
                    ProductItem productItem = new ProductItem();
                    productItem.setPrice(1000+Math.random()*1000);
                    productItem.setQuantity((int) (50*Math.random()));
                    productItem.setBill(bill);
                    pItemRepository.save(productItem);
                });
            });
        };
    }
}
