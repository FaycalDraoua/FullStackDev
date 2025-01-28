package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private CustomerService customerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        //Give

        //When

        //Then
    }

    @Test
    void getCustomer() {
        //Give

        //When

        //Then
    }

    @Test
    void addCustomer() {
        //Give

        //When

        //Then
    }

    @Test
    void deleteCustomer() {
        //Give

        //When

        //Then
    }

    @Test
    void updateCustomer() {
        //Give

        //When

        //Then
    }
}