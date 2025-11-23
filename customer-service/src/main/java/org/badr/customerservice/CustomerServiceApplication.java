package org.badr.customerservice;

import org.badr.customerservice.entities.Customer;
import org.badr.customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(null,"John", "john@gmail.com"));
            customerRepository.save(Customer.builder().name("badr").email("badr@gmail.com").build());
            customerRepository.save(Customer.builder().name("mohcine").email("mohcine@gmail.com").build());
            customerRepository.save(Customer.builder().name("simo").email("simo@gmail.com").build());
        };
    }

}
