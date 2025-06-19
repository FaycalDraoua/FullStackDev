package com.modelcontroller.customer;

import com.modelcontroller.exception.DuplicateResourceException;
import com.modelcontroller.exception.RequestValidationException;
import com.modelcontroller.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private CustomerDao customerDao;

    /**
     * @Autowired : cette annotation n’est plus obligatoire avec le constructeur pour injecter des Beans.
     * Désormais, Spring Boot peut le deviner automatiquement.
     */
    @Autowired
    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id) // ici un Optional<Customer> est retourné
                .orElseThrow(() -> new ResourceNotFound("Customer non trouvé, id : " + id));
        // orElseThrow extrait le client trouvé dans l’Optional et le retourne.
        // S’il ne trouve rien (Optional.empty), il lance l’exception avec le message défini.
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // On vérifie que l’email du nouveau client n’existe pas déjà dans la BD

        String email = customerRegistrationRequest.email();

        if (customerDao.existePersonWithEmail(email)) {
            throw new DuplicateResourceException("Email déjà utilisé");
        }

        // Sinon, on ajoute le nouveau client
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        ));
    }

    public void deleteCustomer(Integer id) {
        if (!customerDao.existPersonWithId(id)) {
            throw new ResourceNotFound("Client introuvable, id : " + id);
        } else {
            customerDao.deleteCustomerById(id);
        }
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest request) {

        // Récupérer le client existant dans la BD et déclencher une exception s’il n’est pas trouvé
        Customer customer = getCustomer(id);

        boolean change = false;

        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            change = true;
        }

        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDao.existePersonWithEmail(request.email())) {
                throw new DuplicateResourceException("Email déjà utilisé");
            }
            customer.setEmail(request.email());
            change = true;
        }

        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            change = true;
        }

        if (!change) {
            throw new RequestValidationException("Aucune donnée modifiée");
        }

        customerDao.updateCustomer(customer);
    }
}
