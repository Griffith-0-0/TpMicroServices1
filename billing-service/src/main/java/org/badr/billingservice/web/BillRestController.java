package org.badr.billingservice.web;

import org.badr.billingservice.entities.Bill;
import org.badr.billingservice.feign.CustomerServiceRestClient;
import org.badr.billingservice.feign.InventoryServiceRestClient;
import org.badr.billingservice.model.Customer;
import org.badr.billingservice.repositories.BillRepository;
import org.badr.billingservice.repositories.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BillRestController {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private CustomerServiceRestClient customerServiceRestClient;
    @Autowired
    private InventoryServiceRestClient inventoryServiceRestClient;

    @GetMapping("/bills/{id}")
    public Bill getBillById(@PathVariable Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        Customer customer = customerServiceRestClient.findCustomerById(bill.getCustomerId());
        bill.setCustomer(customer);

        bill.getProductItems().forEach(pi -> {
            pi.setProduct(inventoryServiceRestClient.getProduct(pi.getProductId()));
        });

        return bill;
    }
}