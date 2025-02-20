package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * L'Annotation @DataJpaTest :
    * Elle charge et configure un context spring limite aux beans necessaire pour tester les interaction JPA(requetes, relation entre entite et repositories)
    * Par default elle configure une base de donnees en memoir (H2, Derby, ou autre) pour eviter d'utiliser la base de donnees de production pendant les tests.
    * Chaque test est exécuté dans une transaction qui est automatiquement annulée à la fin du test. Cela garantit que les données insérées pendant un test n'affectent pas d'autres tests.
 */
@DataJpaTest
/**
 * L'Annotation @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    * Son Role principale est de Controler si une base de donnees de test doit remplacer la base de donnees configurer dans l'application
    * Par default @DataJpaTest remplace automatiquement la base de donnees configurer par une base de donnees en memoir pour test,
      cependant  replace = AutoConfigureTestDatabase.Replace.NONE indique à Spring de ne pas remplacer la base de données configurée
      dans votre fichier de configuration (par exemple, application.yml ou application.properties).
      Car dans mon cas(jusqu'a maintenant video 143) on utilise la BD reel pour nos tests.
      Cella veux dire que dans notre cas, @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) permet à Spring d'utiliser la base de données réelle fournie par TestContainers au lieu d'une base de données en mémoire.

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
        System.out.println("BEAN : "+applicationContext.getBeanDefinitionCount());

    }


    @Test
    void existsCustomerByEmail() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(name,email,30);
        underTest.save(customer);

        //When
        Optional<Customer> actual = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();

        Boolean actualEmail = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actualEmail).isTrue();
    }

    @Test
    void existsCustomerById() {
        //Given
        String name = FAKER.name().firstName();
        String email = FAKER.internet().emailAddress();

        Customer customer = new Customer(name,email,30);

        underTest.save(customer);

        Integer id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //When
        Optional<Customer> actual = underTest.findById(id);
        Boolean actualEmail = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
                assertThat(c.getId()).isEqualTo(id);
                assertThat(c.getName()).isEqualTo(name);
                assertThat(c.getEmail()).isEqualTo(email);
                assertThat(c.getAge()).isEqualTo(30);
                });

        assertThat(actualEmail).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdDoesNotExist() {
        //Given
        Integer id = -1;

        //When
        Boolean actual = underTest.existsById(id);

        //Given
        assertThat(actual).isFalse();
    }

    @Test
    void existeCustomerByEmailFailsWhenEmailNotPresent() {
        //Given
        String fakeEmail = FAKER.internet().emailAddress();

        //When
        Boolean actualEmail = underTest.existsCustomerByEmail(fakeEmail);

        //Then
        assertThat(actualEmail).isFalse();
    }
}
