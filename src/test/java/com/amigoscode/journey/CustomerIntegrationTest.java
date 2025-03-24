package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    /** A journay of someone who wants to use our API :
     1. create registration request
     2. send a post request
     3. get all cutomers
     4. make sure that customer is present
     5. get customer by id
     Don on veux reproduir tous cela a l'aide de webTestClient au lieu de posteMan
     **/
    @Test
    void canRegisterACustomer() {
        //Given
        /// 1. create registration request
        Faker faker = new Faker();
        Name fakerNa = faker.name();
        String name = faker.name().fullName();

        String email = fakerNa.lastName()+"-"+ UUID.randomUUID() + "@foobarhello123.com";

        int age = faker.number().numberBetween(18, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        //When, Then
        /// 2. send a post request
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        /// 3. get all cutomers
        List<Customer> allCustomers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        /// 4.make sure that customer is present
        Customer expectedCustomer = new Customer(name, email, age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);


        /// 5.Get customer by id
        var id = allCustomers.stream()
                .filter(customer -> {
                    return customer.getEmail().equals(email);
                })
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        expectedCustomer.setId(id);

         webTestClient.get().uri("/api/v1/customers/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>(){})
                .isEqualTo(expectedCustomer);



    }

    @Test
    void canDeleteCustomer(){
        //Given
        /// 1. create registration request
        Faker faker = new Faker();
        Name fakerNa = faker.name();
        String name = faker.name().fullName();

        String email = fakerNa.lastName()+"-"+ UUID.randomUUID() + "@foobarhello123.com";

        int age = faker.number().numberBetween(18, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        //When, Then
        /// 2. send a post request
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        /// 3. get all cutomers
        List<Customer> allCustomers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        /// 4.make sure that customer is present
        Customer expectedCustomer = new Customer(name, email, age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);


        /// 5.Get customer by id
        var id = allCustomers.stream()
                .filter(customer -> {
                    return customer.getEmail().equals(email);
                })
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        webTestClient.get().uri("/api/v1/customers/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>(){})
                .isEqualTo(expectedCustomer);



    }
}
