package org.badr.mcpserver.tools;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.badr.mcpserver.feign.BillingServiceRestClient;
import org.badr.mcpserver.model.Bill;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class BillingAiTools {
    private final BillingServiceRestClient billingServiceRestClient;

    public BillingAiTools(BillingServiceRestClient billingServiceRestClient) {
        this.billingServiceRestClient = billingServiceRestClient;
    }

    @McpTool(name = "getBill", description = "get bill by id")
    @CircuitBreaker(name = "billing-service", fallbackMethod = "getDefaultBill")
    public Bill getBill(@McpArg(description = "bill id") Long id) {
        return billingServiceRestClient.getBillById(id);
    }

    @McpTool(name = "getAllBills", description = "get all bills")
    @CircuitBreaker(name = "billing-service", fallbackMethod = "getAllBillsFallback")
    public PagedModel<Bill> getAllBills() {
        return billingServiceRestClient.findAllBills();
    }

    @McpTool(name = "getBillsByCustomer", description = "get all bills for a specific customer")
    @CircuitBreaker(name = "billing-service", fallbackMethod = "getBillsByCustomerFallback")
    public List<Bill> getBillsByCustomer(@McpArg(description = "customer id") Long customerId) {
        return billingServiceRestClient.findBillsByCustomerId(customerId);
    }

    @McpTool(name = "getBillsByProduct", description = "get all bills that contain a specific product")
    @CircuitBreaker(name = "billing-service", fallbackMethod = "getBillsByProductFallback")
    public List<Bill> getBillsByProduct(@McpArg(description = "product id") Long productId) {
        return billingServiceRestClient.findBillsByProductId(productId);
    }

    // Fallback methods
    private Bill getDefaultBill(Long id, Exception e) {
        Bill bill = new Bill();
        bill.setId(id);
        bill.setBillDate(new Date());
        bill.setCustomerId(0L);
        bill.setProductItems(Collections.emptyList());
        return bill;
    }

    private PagedModel<Bill> getAllBillsFallback(Exception e) {
        return PagedModel.empty();
    }

    private List<Bill> getBillsByCustomerFallback(Long customerId, Exception e) {
        return Collections.emptyList();
    }

    private List<Bill> getBillsByProductFallback(Long productId, Exception e) {
        return Collections.emptyList();
    }
}