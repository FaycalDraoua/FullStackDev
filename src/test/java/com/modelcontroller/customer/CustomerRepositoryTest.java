package com.modelcontroller.customer;

import com.modelcontroller.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * L'annotation @DataJpaTest :
 * - Elle charge et configure un contexte Spring limité aux beans nécessaires pour tester les interactions JPA (requêtes, relations entre entités et repositories).
 * - Par défaut, elle configure une base de données en mémoire (H2, Derby, ou autre) pour éviter d'utiliser la base de données de production pendant les tests.
 * - Chaque test est exécuté dans une transaction qui est automatiquement annulée à la fin du test. Cela garantit que les données insérées pendant un test n'affectent pas d'autres tests.
 */
@DataJpaTest
/**
 * L'annotation @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) :
 * - Son rôle principal est de contrôler si une base de données de test doit remplacer la base de données configurée dans l'application.
 * - Par défaut, @DataJpaTest remplace automatiquement la base de données configurée par une base en mémoire pour les tests.
 * - Cependant, "replace = AutoConfigureTestDatabase.Replace.NONE" indique à Spring de ne pas remplacer la base de données configurée
 *   dans votre fichier de configuration (ex : application.yml ou application.properties).
 * - Dans notre cas (jusqu'à la vidéo 143), nous utilisons une base de données réelle via TestContainers.
 *   Cette annotation permet donc à Spring d'utiliser la base de données fournie par TestContainers au lieu d'une base en mémoire.
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println("BEAN : " + applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name, email, 30);
        underTest.save(customer);

        // When
        Optional<Customer> actual = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();

        Boolean actualEmail = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actualEmail).isTrue();
    }

    @Test
    void existsCustomerById() {
        // Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name, email, 30);
        underTest.save(customer);

        Integer id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actual = underTest.findById(id);
        Boolean actualEmail = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(name);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(30);
        });

        assertThat(actualEmail).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdDoesNotExist() {
        // Given
        Integer id = -1;

        // When
        Boolean actual = underTest.existsById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // Given
        String fakeEmail = FAKER.internet().emailAddress();

        // When
        Boolean actualEmail = underTest.existsCustomerByEmail(fakeEmail);

        // Then
        assertThat(actualEmail).isFalse();
    }
}
