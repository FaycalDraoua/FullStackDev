package com.modelcontroller.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static com.modelcontroller.AbstractTestContainers.FAKER;

class CustomerJPADataAccessServiceTest {

    /**
     * On l'appelle également System Under Test (SUT)
     */
    private CustomerJPADataAccessService underTest;

    /***
     * @Mock est une annotation de Mockito. Elle crée une version simulée (mockée) de l'objet CustomerRepository.
     * Cela permet d'isoler la classe testée (CustomerJPADataAccessService) de la dépendance réelle (CustomerRepository).
     * Les mocks sont utilisés pour remplacer les dépendances réelles afin de contrôler leur comportement dans les tests.
     */
    @Mock
    private CustomerRepository customerRepository;

    /**
     * L'appel à MockitoAnnotations.openMocks(this), voir ci-dessous, retourne un AutoCloseable qui doit être fermé
     * après chaque test pour nettoyer les mocks correctement (voir le code dans la méthode tearDown()).
     */
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        /**
         * Initialise les objets marqués par @Mock dans la classe de test (ici, customerRepository).
         * Retourne un objet AutoCloseable, qui sera utilisé pour fermer les mocks après les tests.
         */
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Crée une nouvelle instance de la classe à tester (CustomerJPADataAccessService) en injectant la dépendance simulée (customerRepository).
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    /**
     * @AfterEach :
     * Indique que cette méthode doit être exécutée après chaque test.
     * Sert à nettoyer ou libérer les ressources utilisées pendant les tests.
     */
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();

        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        int id = 1;

        // When
        underTest.selectCustomerById(id);

        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void existePersonWithEmail() {
        // Given
        String email = FAKER.internet().emailAddress();

        // When
        underTest.existePersonWithEmail(email);

        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.internet().emailAddress(),
                30);

        // When
        underTest.insertCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithId() {
        // Given
        int id = 1;

        // When
        underTest.existPersonWithId(id);

        // Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.internet().emailAddress(),
                30);

        // When
        underTest.updateCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }
}
