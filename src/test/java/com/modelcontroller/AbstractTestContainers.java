package com.modelcontroller;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

/**
 * Cette annotation @Testcontainers :
 * - Indique que c’est une classe qui utilise des conteneurs.
 * - Gère automatiquement le cycle de vie des conteneurs dans les tests (démarrage, arrêt...).
 * - Garantit que les conteneurs seront lancés avant les tests et arrêtés juste après.
 */
@Testcontainers
public class AbstractTestContainers {

    /**
     * Cette annotation est utilisée pour exécuter une méthode une seule fois avant toutes les autres méthodes de test.
     * Elle doit être statique.
     */
    @BeforeAll
    static void beforeAll() {
        /*
         Code pour exécuter les migrations Flyway (création de versions de schémas de tables) définies
         dans les fichiers du dossier db.migration, comme la table customer avec ses contraintes.
         */
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }

    /**
     * Cette annotation @Container :
     * - Indique qu’une instance de conteneur Testcontainers doit être initialisée, gérée et arrêtée automatiquement.
     *
     * Méthode : Création de la BD dans une image Docker avec :
     * - le type de BD
     * - le nom de la BD
     * - le nom d’utilisateur
     * - le mot de passe
     */
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest") // "postgres" pour l'image, "latest" pour la dernière version. On peut aussi spécifier une version précise comme "postgres:14.5"
                    .withDatabaseName("amigoscode-Dao-unit-test") // Nom de la base de données
                    .withUsername("amigoscode")
                    .withPassword("password");

    /**
     * L’annotation @DynamicPropertySource permet de définir dynamiquement les propriétés de configuration Spring pour les tests.
     *
     * Méthode : Configuration de la connexion à la BD qui se trouve dans l’image Docker.
     * ⚠️ La méthode doit être statique.
     */
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        // "spring.datasource.url" correspond à la propriété dans application.yml
        registry.add("spring.datasource.url", () ->
                postgreSQLContainer.getJdbcUrl());

        // Syntaxe lambda vs méthode référence (ci-dessous)
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    /**
     * Méthode pour créer une source de données, connectée à la BD du conteneur PostgreSQL.
     * Elle sera utilisée dans les tests de la classe CustomerJDBCDataAccessService.
     * @return la DataSource connectée au conteneur.
     */
    protected static DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());

        return dataSourceBuilder.build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    public static final Faker FAKER = new Faker();
}
