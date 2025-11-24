package org.badr.chatbot.tools;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.badr.chatbot.feign.CustomerServiceRestClient;
import org.badr.chatbot.model.Customer;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerAiTools {
    private final CustomerServiceRestClient customerServiceRestClient;

    public CustomerAiTools(CustomerServiceRestClient customerServiceRestClient) {
        this.customerServiceRestClient = customerServiceRestClient;
    }

    @Tool(name = "getCustomer", description = "get customer by id")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomer")
    public Customer getCustomer(@ToolParam(description = "customer id") Long id) {
        return customerServiceRestClient.findCustomerById(id);
    }

    @Tool(name = "getAllCustomers", description = "get all customers")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "getAllCustomersFallback")
    public PagedModel<Customer> getAllCustomers() {
        return customerServiceRestClient.findAllCustomers();
    }

    @Tool(name = "getCustomerByName", description = "search customer by name")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomerByName")
    public Customer getCustomerByName(@ToolParam(description = "customer name") String name) {
        return customerServiceRestClient.findCustomerByName(name);
    }

    @Tool(name = "getCustomerByEmail", description = "search customer by email")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomerByEmail")
    public Customer getCustomerByEmail(@ToolParam(description = "customer email") String email) {
        return customerServiceRestClient.findCustomerByEmail(email);
    }

    @Tool(name = "updateCustomer", description = "update customer information completely")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "updateCustomerFallback")
    public Customer updateCustomer(
            @ToolParam(description = "customer id") Long id,
            @ToolParam(description = "updated customer data") Customer customer) {
        return customerServiceRestClient.updateCustomer(id, customer);
    }

    @Tool(name = "updateCustomerName", description = "update only customer name")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "updateNameFallback")
    public Customer updateCustomerName(
            @ToolParam(description = "customer id") Long id,
            @ToolParam(description = "new name") String name) {
        Customer customer = customerServiceRestClient.findCustomerById(id);
        customer.setName(name);
        return customerServiceRestClient.updateCustomer(id, customer);
    }

    @Tool(name = "updateCustomerEmail", description = "update only customer email")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "updateEmailFallback")
    public Customer updateCustomerEmail(
            @ToolParam(description = "customer id") Long id,
            @ToolParam(description = "new email") String email) {
        Customer customer = customerServiceRestClient.findCustomerById(id);
        customer.setEmail(email);
        return customerServiceRestClient.updateCustomer(id, customer);
    }

    @Tool(name = "deleteCustomerById", description = "delete customer by id")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "deleteByIdFallback")
    public String deleteCustomerById(@ToolParam(description = "customer id") Long id) {
        customerServiceRestClient.deleteCustomerById(id);
        return "Customer with id " + id + " deleted successfully";
    }

    @Tool(name = "deleteCustomerByName", description = "delete customer by name")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "deleteByNameFallback")
    public String deleteCustomerByName(@ToolParam(description = "customer name") String name) {
        Customer customer = customerServiceRestClient.findCustomerByName(name);
        if (customer != null && customer.getId() != null) {
            customerServiceRestClient.deleteCustomerById(customer.getId());
            return "Customer " + name + " deleted successfully";
        }
        return "Customer " + name + " not found";
    }

    @Tool(name = "deleteCustomerByEmail", description = "delete customer by email")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "deleteByEmailFallback")
    public String deleteCustomerByEmail(@ToolParam(description = "customer email") String email) {
        Customer customer = customerServiceRestClient.findCustomerByEmail(email);
        if (customer != null && customer.getId() != null) {
            customerServiceRestClient.deleteCustomerById(customer.getId());
            return "Customer with email " + email + " deleted successfully";
        }
        return "Customer with email " + email + " not found";
    }

    // Fallback methods
    private Customer getDefaultCustomer(Long id, Exception e) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("default");
        customer.setEmail("default");
        return customer;
    }

    private PagedModel<Customer> getAllCustomersFallback(Exception e) {
        return PagedModel.empty();
    }

    private Customer getDefaultCustomerByName(String name, Exception e) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail("not-found");
        return customer;
    }

    private Customer getDefaultCustomerByEmail(String email, Exception e) {
        Customer customer = new Customer();
        customer.setName("not-found");
        customer.setEmail(email);
        return customer;
    }

    private Customer updateCustomerFallback(Long id, Customer customer, Exception e) {
        return customer;
    }

    private Customer updateNameFallback(Long id, String name, Exception e) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setEmail("update-failed");
        return customer;
    }

    private Customer updateEmailFallback(Long id, String email, Exception e) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("update-failed");
        customer.setEmail(email);
        return customer;
    }

    private String deleteByIdFallback(Long id, Exception e) {
        return "Failed to delete customer with id " + id + ": " + e.getMessage();
    }

    private String deleteByNameFallback(String name, Exception e) {
        return "Failed to delete customer " + name + ": " + e.getMessage();
    }

    private String deleteByEmailFallback(String email, Exception e) {
        return "Failed to delete customer with email " + email + ": " + e.getMessage();
    }
}