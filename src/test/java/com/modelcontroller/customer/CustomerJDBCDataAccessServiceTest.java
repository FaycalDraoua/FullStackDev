package com.modelcontroller.customer;

import com.modelcontroller.AbstractTestContainers;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    /**
     * Cette annotation, ainsi que sa méthode setUp(), permet d'exécuter du code avant chaque méthode annotée avec @Test.
     * Cela signifie que pour chaque méthode de test unitaire (comme selectAllCustomers, selectCustomerById, etc.)
     * une nouvelle instance de `underTest` sera créée.
     *
     * Le paramètre d'entrée `getJdbcTemplate()` permet de se connecter à la base de données chargée dans une image Docker.
     *
     * La base de données n'est pas réinitialisée à chaque création d'une instance de `underTest` dans @BeforeEach.
     * Elle est partagée entre toutes les méthodes de test, ce qui est logique dans le contexte d'une base de données.
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
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name, email,30);

        underTest.insertCustomer(customer);

        //When
        var actual = underTest.selectCustomerById(-1);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name, email, 20);

        //When
    underTest.insertCustomer(customer);
        Optional<Customer> actual = underTest.selectAllCustomers()
            .stream().filter(c -> c.getEmail().equals(email))
            .findFirst();

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
        }

    @Test
    void existePersonWithEmail() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name,email,30);
        underTest.insertCustomer(customer);

        //When
        Optional<Customer> actual = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();

        Boolean actualEmail = underTest.existePersonWithEmail(email);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(
                c ->{
                        assertThat(c.getName()).isEqualTo(customer.getName());
                        assertThat(actual.get().getEmail()).isEqualTo(customer.getEmail());
        });
        assertThat(actualEmail).isTrue();
    }
    @Test
    void existePersonWithEmailReturnFalseWhenEmailDoesNotExist() {
        //Given
        String email = FAKER.internet().emailAddress();


        //When
        boolean actualEmail = underTest.existePersonWithEmail(email);

        //Then
        assertThat(actualEmail).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name,email,30);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                        .filter(c -> c.getEmail().equals(email))
                                .map(Customer::getId)
                                        .findFirst()
                                                .orElseThrow();

        //When
        underTest.deleteCustomerById(id);
        List<Customer> actuals = underTest.selectAllCustomers();


        //Then
        long count = actuals.stream().filter(c -> c.getEmail().equals(email)).count();
        assertThat(count).isEqualTo(0);

        assertThat(underTest.existPersonWithId(id)).isFalse();
    }

    @Test
    void willReturnEmptyWhenDeleteCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name,email,30);

        underTest.insertCustomer(customer);
        List<Customer> befor = underTest.selectAllCustomers();

        //When
        underTest.deleteCustomerById(-1);
        List<Customer> actuals = underTest.selectAllCustomers();

        //Then
        assertThat(actuals).isEqualTo(befor);

    }

    @Test
    void existPersonWithId() {
        //Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(),email,30);

        underTest.insertCustomer(customer);

        //When
    Integer id = underTest.selectAllCustomers().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //Then
        assertThat(underTest.existPersonWithId(id)).isTrue();
    }

    @Test
    void existPersonWithIdWillReturnFalseWhenIdDoesNotExist() {
        //Given
        int id = -1;

        //When
        var actual = underTest.existPersonWithId(id);

        //Then
        assertThat(actual).isFalse();
        }


    @Test
    void updateCustomerName(){
       //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name,email,30);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When Name is changer
        Customer update = new Customer();
        update.setId(id);
        String newName = FAKER.name().firstName();
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        assertThat(underTest.selectCustomerById(id)).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getName()).isEqualTo(newName);
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(30);
                });

    }

    @Test
    void updateCustomerEmail(){
        //Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(),email,30);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When Email is changer
        Customer update = new Customer();
        update.setId(id);
        String newEmail = FAKER.internet().emailAddress();
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Given
        assertThat(underTest.selectCustomerById(id)).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(newEmail);
                    assertThat(c.getAge()).isEqualTo(30);
                });
    }


    @Test
    void updateCustomerAge(){{
        //Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(),email,30);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When Age is changer
        Customer update = new Customer();
        update.setId(id);
        update.setAge(50);

        underTest.updateCustomer(update);

        //Given
        assertThat(underTest.selectCustomerById(id)).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(50);
                });
    }
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name,email,30);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Customer update = new Customer();
        update.setId(id);
        String newEmail = FAKER.internet().emailAddress();
        String newName = FAKER.name().firstName();
        update.setName(newName);
        update.setEmail(newEmail);
        update.setAge(50);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        //Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email,30);

        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Customer update = new Customer();
        customer.setId(id);
        update.setId(id);

        underTest.updateCustomer(update);

        //Given
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(customer);
    }
}