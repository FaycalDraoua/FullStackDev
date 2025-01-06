package com.amigoscode;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestContainersTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("amigoscode-Dao-unit-test")
                    .withUsername("amigoscode")
                    .withPassword("password");
    
    @Test
    void canStartPostgreDb() {
        //Given  
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();

        //When

        //Then   
    }

    @Test
    void canApplyDbMigrationWithFlyway() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }
}
