package com.amigoscode.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
Bonjour je viens de modifier depuis la branche clone et pusher dans la branche main
dans le GitHub
 */
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
    }


    @GetMapping()
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Integer id)
    {
        return customerService.getCustomer(id);

    }


    @PostMapping()
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer0(@PathVariable("customerId") Integer id,@RequestBody CustomerUpdateRequest request){
        customerService.updateCustomer(id,request);
    }

}