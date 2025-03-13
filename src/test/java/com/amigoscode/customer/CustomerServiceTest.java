package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @ExtendWith, cette annotation permet d'initialiser automatiquement les objets annote avec @Mok, @Spy, @InjectionMocks
    dans la class de test, sans Avoir besoin a utiliser :
        1. autoCloseable = MockitoAnnotations.openMocks(this) dans la méthode @BeforeEach pour initaliser les objets Moquè.
        2. autoCloseable.close() dans la methode @AfterEach pour ferme les objets moquè.
            (Exactement ce qu'on a fait dans la class de test CustomerJPADataAccessServiceTest, voir)
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDao).selectAllCustomers();
    }


    /**
     * IMPORTANT :
        Il faut savoir que le bute du test uniatire n'est pas de tester le COMPORTEMENT et l'INTERACTION entre les different couhes et class, mais c'est plus tot
     de tester le comporetement de chaque class ou couche TOUTE SEUL.

     Par exemple ici dans le test ci-dessou de la methode getCustomer() du service, Le but de ce test unitaire n’est pas de tester l’implémentation de
     selectCustomerById, mais bien de tester le comportement de getCustomer(id).

     DONC...

        * 👉 Ce qu'on veut vérifier :
             * Est Que si customerDao.selectCustomerById(id) retourne un Optional contenant un Customer, alors getCustomer(id) retourne bien ce Customer.
             * Que si customerDao.selectCustomerById(id) retourne un Optional.empty(), alors une exception ResourceNotFound est bien levée.
     ET...
        * ⚠️ Ce qu'on ne veux pas teste ici :
             * Est Comment customerDao.selectCustomerById(id) fonctionne réellement (avec une BD, ou un fichier, ou etc.), we don't care.
     */
    @Test
    void canGetCustomer() {
        //Give

        ///Ici on cree un customer
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com",19);

        /// ON MOCK le comportement de customerDao.selectCustomerById(), cest dire on explique a l'algorithme ce qu'il doit faire
        when(customerDao.selectCustomerById(10)).thenReturn(Optional.of(customer));

        //When

        /// apres le mock, on passe au test qu'on veux reelement tester et non pas mocker, est qui celui de la methode underTest.getCustomer(), donc on appele la methode
        Customer actual =  underTest.getCustomer(id);

        //Then

        /// ici on s'assure qu'on a eux le bon comportement attendu de la methode underTest.getCustomer();
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willReturnThrowWhenGetCustomerReturnOptionalEmpty() {
        //Give
        int id = 10;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
            ///assertThatThrownBy() est une methode pour capturer les exception. mais je sais toujours pas ca fonctionne.
        assertThatThrownBy(()-> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer Not fund, id : "+id);

    }

    @Test
    void CanAddCustomer() {
        //Give
        String email = "alex@gmail.com";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest( "Alex", email,19);
        when(customerDao.existePersonWithEmail(email)).thenReturn(false);

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        /**
         *ici je capture le customer qui est passer dans customerDao.insertCustomer(customer), voir la methode
             addCustomer dans le service. Ensuite on va comparer cette capture avec request pour s'assurer que les valeurs
             qui ont ete passe a la methode addCustomer dans le service sont les meme valeurs qui ont ete ajouter a la
             methode insertCustomer de CustomerDao.
         *Ce que je comprend pas est que comment customerCaptor.capture() peux capturer ces information ????? faut voir avec Gpt !!!
         */

        verify(customerDao).insertCustomer(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
   void willReturnThrowWhenEmailExistingWhilAddingCustomer() {
        //Given
        String email = "alex@gmail.com";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        when(customerDao.existePersonWithEmail(email)).thenReturn(true);

        //When
        assertThatThrownBy(()-> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email deja utiliser");

        //Then
        /// On a utilise la methode static never() de la class Mockito, pour exprimer que j'ai customerDao n'a pu appeler insertCustomer().
        verify(customerDao, never()).insertCustomer(any(Customer.class));

    }

    @Test
    void deleteCustomer() {
        //Give
        Integer id=10;

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        //When
        underTest.deleteCustomer(id);

        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willReturnThrowWhenDeleteCustomerReturnFalse() {
        //Give
        Integer id=10;

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        //When
        //Then
       assertThatThrownBy(()-> underTest.deleteCustomer(id))
               .isInstanceOf(ResourceNotFound.class)
               .hasMessageContaining("Customer introuvable, id : "+id);

       verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomer() {

    }


}