package com.amigoscode;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
/*
 Cette annotation @SpringBootTest est strictement à éviter dans les tests unitaires, dans le sens où il est inutile
 d’injecter des beans provenant d’autres classes pour rien. Car le principe de base d’un test unitaire est de tester
 localement une méthode uniquement à l’intérieur de la classe ou de la couche concernée (controller, service, DAO…).

 Par contre, cette annotation est très utile dans les tests d’intégration, car le but de ces derniers est justement
 de tester l’interaction entre plusieurs classes et couches dans le contexte complet de l’application Spring.
 */
//@SpringBootTest
public class TestContainersTest extends AbstractTestContainers {

    @Test
    void canStartPostgreDb() {
        // Given
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();

        // When

        // Then
    }
}
