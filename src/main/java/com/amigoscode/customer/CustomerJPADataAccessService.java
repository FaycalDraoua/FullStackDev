package com.amigoscode.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "jpa") // je peux uniquement ecrir @Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao{

    /*
    Afin d'injecter un bean j'ai 3 methode :

        1* Utiliser un constructeur comme vous le voyez en bas, et c'est la methode la plus recommander

        2* En utilisant l'annotation @Autowired a condition qu'il faut enlever le final, car Spring ne peut pas
    injecter de dépendances dans un champ constant marqué comme final.

    PS : ici dans notre cas d'utilisation de la methode du constructeur, l'
    a l'attribut peux etre declarer final

        3* utiliser une methode, comme un setter par exemple, comme suit :
            @Autowired
            public void setCustomerRepository(CustomerRepository customerRepository) {
            this.customerRepository = customerRepository;
             }
        4*
     ps : dans ce cas de setter l'attribut ne doit pas etre marquer avec final
     */
    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean existePersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
