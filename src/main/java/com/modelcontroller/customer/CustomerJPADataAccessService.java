package com.modelcontroller.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "jpa") // Je peux également écrire simplement @Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

    /*
    Pour injecter un bean, j’ai 3 méthodes :

        1* Utiliser un constructeur, comme vous le voyez ci-dessous. C’est la méthode la plus recommandée.

        2* Utiliser l’annotation @Autowired, à condition de retirer le mot-clé final,
           car Spring ne peut pas injecter de dépendances dans un champ constant marqué comme final.

           PS : dans notre cas, comme on utilise un constructeur, l’attribut peut être déclaré final.

        3* Utiliser une méthode, comme un setter, par exemple :
            @Autowired
            public void setCustomerRepository(CustomerRepository customerRepository) {
                this.customerRepository = customerRepository;
            }

        PS : dans le cas du setter, l’attribut ne doit pas être marqué comme final.
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
