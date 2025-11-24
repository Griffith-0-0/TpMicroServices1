package org.badr.chatbot.feign;

import org.badr.chatbot.model.Bill;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "billing-service")
public interface BillingServiceRestClient {

    @GetMapping("/api/bills/{id}")
    Bill getBillById(@PathVariable Long id);

    @GetMapping("/bills")
    PagedModel<Bill> findAllBills();

    @GetMapping("/bills/search/findByCustomerId")
    List<Bill> findBillsByCustomerId(@RequestParam("customerId") Long customerId);

    @GetMapping("/bills/search/findByProductId")
    List<Bill> findBillsByProductId(@RequestParam("productId") Long productId);
}