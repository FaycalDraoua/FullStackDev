package com.amigoscode.journey;

import com.amigoscode.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

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



    }
}
