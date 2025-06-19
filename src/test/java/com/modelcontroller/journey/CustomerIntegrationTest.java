package com.modelcontroller.journey;

import com.modelcontroller.customer.Customer;
import com.modelcontroller.customer.CustomerRegistrationRequest;
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

        webTestClient.get().uri("/api/v1/customers/{customerId}", id) ///  envoyer le requette get http
                .accept(MediaType.APPLICATION_JSON)                     /// definir le format de la reponse attendu, dans ce cas c'est du JSON
                .exchange()                                             /// envoyer la requette
                .expectStatus().isOk()                                  /// verifier que le status de la reponse est 200
                .expectBody(new ParameterizedTypeReference<Customer>(){}) ///  definir le corp de la reponse, ParameterizedTypeReference<Customer>(){} est utilisé pour gérer la désérialisation correcte des types génériques (nécessaire pour les objets complexes).
                .isEqualTo(expectedCustomer);                               /// verifier que le corp de la reponse est egale a expectedCustomer, field by field


        ///  6. delete customer
        webTestClient.delete().uri("/api/v1/customers/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        /// 7. get customer by id, not found
        webTestClient.get().uri("/api/v1/customers/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void canUpdateCustomer(){

        ///  1. create registration request
        Faker faker = new Faker();
        Name fakerNAme = faker.name();
        String name = fakerNAme.fullName();
        String email = fakerNAme.lastName()+"-"+"@addForUpdateTestInteg.com";
        int age = faker.number().numberBetween(18, 100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        /// 2. send a post request
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        /// 3. get all cutomers
        List<Customer> customerList = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        /// 4. extract the cutomer id
        var id = customerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        /// 5. create a new request for update
        String newName = fakerNAme.fullName();
        int newAge = faker.number().numberBetween(18, 100);

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(newName, email, newAge);

        /// 6. send a put request
        webTestClient.put().uri("/api/v1/customers/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        /// 7. get customer by id
        Customer expectedCustomer = new Customer(newName, email, newAge);
        expectedCustomer.setId(id);

        Customer customerRecup = webTestClient.get().uri("/api/v1/customers/{customerId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>(){})
                .returnResult()
                .getResponseBody();

        assertThat(customerRecup).isEqualTo(expectedCustomer);

    }
}
