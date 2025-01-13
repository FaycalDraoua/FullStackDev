package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    /*
    Cette annotation ainsi que ca methode setUp() permet de s'executer avant chaque debut de @Test. Cela veux que
    pour chaque methode qu'on voit en bas annoter avec @Test et qui va passer le test unitaire comme selectAllCustomers,
    selectCustomerById... une nouvelle instance de underTest va etre creer.
     */
    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(),customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(FAKER.name().firstName(),
                FAKER.internet().emailAddress()+"-"+ UUID.randomUUID(),
                20);

        underTest.insertCustomer(customer);

        //When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
    Customer customer = new Customer(name,email,20);

    underTest.insertCustomer(customer);

    Integer id = underTest.selectAllCustomers()
            .stream().filter(c-> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
    Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
    assertThat(actual).isPresent().hasValueSatisfying(
            c -> {
                assertThat(c.getId()).isEqualTo(id);
                assertThat(c.getName()).isEqualTo(customer.getName());
                assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                assertThat(c.getAge()).isEqualTo(customer.getAge());
            }
    );
    }

    @Test
    void insertCustomer() {
        //Given

        //When

        //Then
    }

    @Test
    void existePersonWithEmail() {
        //Given

        //When

        //Then
    }

    @Test
    void deleteCustomerById() {
        //Given

        //When

        //Then
    }

    @Test
    void existPersonWithId() {
        //Given

        //When

        //Then
    }

    @Test
    void updateCustomer() {
        //Given

        //When

        //Then
    }
}