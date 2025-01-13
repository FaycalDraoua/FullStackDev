package com.amigoscode;

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
cette annotation @Testcontainers :
 *pour indiquer que cest une class qui utilise des conteneur.
 *gere automatiquement le cycle de vie des conteneur dans le test(damarrage, arret...).
 *grantie que les conteneur seront charger avant le test et arreter juste apres.
 */
@Testcontainers
public class AbstractTestContainers {

    /**
     * Cette annotation est utiliser pour demarrer une methode avant tous les autre methode du test.
     * elle etre static.
     */
    @BeforeAll
    static void beforeAll() {
        /*
        code pour migrer(creation de version) de schema de table qu'on a creer dans nos fichier Flyway.
        Et donc la creation de la table customer avec les different contraintes.(voir fichiers de db.migration folder)
         */
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }


    /**
     * Cette annotaion indique q'une instance de conteneur testcontainers dois etre initialiser,gerer,arreter automatiquement.

    Methode : Creation de la BD dans une une image Docker. Avec :
    *le type de BD
    *le nom de la BD
    *le userName
    *le Password
     */
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest") // postgres(pour limage postgres), latest(pour charger la derniere version). on peux tres bien preciser une version comme "postgres:14.5"
                    .withDatabaseName("amigoscode-Dao-unit-test")
                    .withUsername("amigoscode")
                    .withPassword("password");


    /**
     *l'annotation Permet de définir dynamiquement les propriétés de configuration Spring pour les tests.

    *Methode : Configuration de Connexion a la BD qui se trouve dans l'image du Docker.
    *PS : faut dabord creer la BD dans docker(voir le code juste en haut : postgreSQLContainer)
    *La Methode doit etre static.
    */
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        // Spring.datasource.url cest la meme config que celle du application.yml(spring datasource url)
        registry.add("spring.datasource.url", () ->
                postgreSQLContainer.getJdbcUrl());
// la diference de syntaxe entre celle de haut et celle d'enbas, cest la difference entre lambda expression et methode references
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    /**
     * Methode pour creer une source de donnees, donc qui va etre connecter avec notre source de donnees qui se trouvera
     dans l'image du conteneur posgreSQLContainer.
     *on va l'utiliser dans le test de la class CustomerJDBCDataAcccess.
     * @return la source de donnees.
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

    protected static final Faker FAKER = new Faker();



}


