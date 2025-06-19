package com.modelcontroller.customer;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Repository(value = "list") // je peux uniquement ecrir @Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    private static List<Customer> customers;

    static{
        customers = Arrays.asList(
                new Customer(1,"Alex","Alex@gmail.com",30),
                new Customer(2,"Morad","Morad@gmail.com",25)
        );

    };


    @Override
    public List<Customer> selectAllCustomers(){

        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existePersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void insertCustomer(Customer customer) {

    }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        //customers.removeIf(c -> c.getId().equals(id));

        /*
         OR
         */
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);

    }

    @Override
    public void updateCustomer(Customer customer) {

    }


}
