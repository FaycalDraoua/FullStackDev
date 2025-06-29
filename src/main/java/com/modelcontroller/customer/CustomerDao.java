package com.modelcontroller.customer;



import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existePersonWithEmail(String email);
    void deleteCustomerById(Integer id);
    boolean existPersonWithId(Integer id);
    void updateCustomer(Customer customer);


}
