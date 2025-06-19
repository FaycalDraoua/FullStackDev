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
 * @ExtendWith : cette annotation permet d'initialiser automatiquement les objets annotés avec @Mock, @Spy, @InjectMocks
 * dans la classe de test, sans avoir besoin d’utiliser :
 *     1. autoCloseable = MockitoAnnotations.openMocks(this) dans la méthode @BeforeEach pour initialiser les objets mockés.
 *     2. autoCloseable.close() dans la méthode @AfterEach pour fermer les objets mockés.
 *        (Exactement ce qu’on a fait dans la classe de test CustomerJPADataAccessServiceTest)
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
    void tearDown() {}

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao).selectAllCustomers();
    }

    /**
     * IMPORTANT :
     * Il faut savoir que le but du test unitaire n’est pas de tester le COMPORTEMENT et l’INTERACTION entre les différentes couches/classes,
     * mais plutôt de tester le comportement de chaque classe ou couche individuellement.
     *
     * Par exemple ici, dans le test de la méthode getCustomer(), le but n’est pas de tester l’implémentation de
     * selectCustomerById, mais bien le comportement de getCustomer(id).
     *
     * 👉 Ce qu’on veut vérifier :
     * - Si customerDao.selectCustomerById(id) retourne un Optional contenant un Customer, alors getCustomer(id) retourne bien ce Customer.
     * - Si customerDao.selectCustomerById(id) retourne un Optional.empty(), alors une exception ResourceNotFound est bien levée.
     *
     * ⚠️ Ce qu’on ne veut PAS tester ici :
     * - Comment fonctionne réellement customerDao.selectCustomerById(id) (avec une BD, un fichier, etc.), cela ne nous concerne pas ici.
     */
    @Test
    void canGetCustomer() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        when(customerDao.selectCustomerById(10)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willReturnThrowWhenGetCustomerReturnOptionalEmpty() {
        // Given
        int id = 10;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer Not fund, id : " + id);
    }

    @Test
    void canAddCustomer() {
        // Given
        String email = "alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);
        when(customerDao.existePersonWithEmail(email)).thenReturn(false);

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        /**
         * Ici je capture le Customer passé dans customerDao.insertCustomer(customer), voir la méthode
         * addCustomer dans le service. Ensuite, on compare cette capture avec la request pour s’assurer que les valeurs
         * passées à la méthode addCustomer sont bien celles utilisées dans insertCustomer de CustomerDao.
         *
         * ❓ Ce que je ne comprends pas encore : comment customerCaptor.capture() peut capturer ces informations automatiquement ?
         */
        verify(customerDao).insertCustomer(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willReturnThrowWhenEmailExistsWhileAddingCustomer() {
        // Given
        String email = "alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);
        when(customerDao.existePersonWithEmail(email)).thenReturn(true);

        // Then
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email deja utiliser");

        verify(customerDao, never()).insertCustomer(any(Customer.class));
    }

    @Test
    void deleteCustomer() {
        // Given
        Integer id = 10;
        when(customerDao.existPersonWithId(id)).thenReturn(true);

        // When
        underTest.deleteCustomer(id);

        // Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willReturnThrowWhenDeleteCustomerReturnFalse() {
        // Given
        Integer id = 10;
        when(customerDao.existPersonWithId(id)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer introuvable, id : " + id);

        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        // Given
        Integer id = 10;
        String email = "Alex@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(id, "Alex", email, 19);
        Customer customer = new Customer(id, "Boby", "Boby@gmail.com", 23);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existePersonWithEmail(email)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        Integer id = 10;
        String email = "Alex@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(id, "Alex", email, 20);
        Customer customer = new Customer(id, "Boby", email, 20);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void willReturnThrowWhenUpdateCustomerEmailAlreadyExists() {
        // Given
        Integer id = 10;
        String email = "Alex@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(id, "Alex", email, 20);
        Customer customer = new Customer(id, "Boby", "Boby@gmail.com", 20);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existePersonWithEmail(email)).thenReturn(true);

        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email deja utilise");
    }

    @Test
    void willReturnThrowWhenUpdateCustomerReturnNoDataChangeFound() {
        // Given
        Integer id = 10;
        String email = "Alex@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(id, "Boby", email, 20);
        Customer customer = new Customer(id, "Boby", email, 20);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("No data change fund");
    }
}
