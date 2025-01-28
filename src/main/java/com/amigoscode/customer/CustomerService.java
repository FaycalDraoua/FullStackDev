package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private CustomerDao customerDao;


    /**
     * @Autowired, cette annotation n'est plus obligatoire a utiliser avec le constructeur afin d'injecter des Beans
        mainent spring Boot peux le savoir implicitement.
     */
    @Autowired
    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {

        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){

        return customerDao.selectAllCustomers();
    }


    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id) // ici un Optional<Custoemr> est retourne
                .orElseThrow(() -> new ResourceNotFund("Customer Not fund, id : "+id)); // ici orElseThrow
                // extre le customer trouver dans l'Optional et le retourne. si il trouve un Optional<Vide>
                // il va retourne le message de l'exception defini dans le lambda ici " Customer Not fund, id : "+id"
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        // on a besoin de verifier que le nouveau email a ajouter n'existe pas deja dans la bd pour un autre user

        String email = customerRegistrationRequest.email();

        if(customerDao.existePersonWithEmail(email)){
            throw new DuplicateResourceException("Email deja utiliser");
        }

        // si non ajouter le nouveau customer
        else {
            customerDao.insertCustomer(new Customer(
                    customerRegistrationRequest.name(),
                    customerRegistrationRequest.email(),
                    customerRegistrationRequest.age()
            ));
        }

    }

    public void deleteCustomer(Integer id){
        if(!customerDao.existPersonWithId(id)){
            throw new ResourceNotFund("Customer introuvable, id : "+id);
        }
        else {
            customerDao.deleteCustomerById(id);
        }
    }


    public void updateCustomer(Integer id, CustomerUpdateRequest request){

        // recuperer le customerExisting dans la BD en meme temp declancher une exception au cas ou il se trouve pas
        Customer customer = getCustomer(id);

        boolean change = false;

        if(request.name() != null && !request.name().equals(customer.getName())){
            customer.setName(request.name());
            change = true;
        }

        if(request.email() != null && !request.email().equals(customer.getEmail())){
            if(customerDao.existePersonWithEmail(request.email())){
                throw new DuplicateResourceException("Email deja utilise");
            }
            customer.setEmail(request.email());
            change = true;
        }

        if(request.age() != null && !request.age().equals(customer.getAge())){
            customer.setAge(request.age());
            change = true;
        }

        if(!change){
            throw new RequestValidationException("No data change fund");
        }

        customerDao.updateCustomer(customer);


    }
}
