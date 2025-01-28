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
 cette annotation @SpringBootTest est strictement interdite dans les Test unitaire dans le sense ou extrement unitile d'injecter
 des bean de d'autre class inutillement. Car le principe de base des Test unitaire est de tester des methode localement
 uniquement a l'interieur de la class ou la couche dans laquel on se trouve(cntroller, service, DAO...)
 Par Contre cette annotation est tres utile dans les Test d'integration, car deja le principe de base des tests d'integration et
 belle et bien de tester l'interation de different classe et couche entre elle dans le context de notre appli Spring..
 */
//@SpringBootTest
public class TestContainersTest extends AbstractTestContainers{

    @Test
    void canStartPostgreDb() {
        //Given  
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();

        //When

        //Then   
    }


}
