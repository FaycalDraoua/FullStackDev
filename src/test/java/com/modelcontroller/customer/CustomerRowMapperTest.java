package com.modelcontroller.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        /**
         * On a vu jusqu’ici l’utilisation de Mockito de deux manières :
         *
         * 1. Utiliser l’annotation @Mock
         *    Points clés :
         *      - Simplicité : L’annotation @Mock simplifie la création des mocks.
         *      - Intégration avec JUnit : Elle est souvent utilisée avec @ExtendWith(MockitoExtension.class)
         *        pour initialiser automatiquement les mocks.
         *      - Lisibilité : Le code est plus lisible et moins verbeux.
         *
         * 2. Utiliser ce que je viens juste d’utiliser ci-dessous : "instance = Mockito.mock(NomClasse.class)"
         *    Points clés :
         *      - Flexibilité : Vous pouvez créer des mocks à n’importe quel endroit dans votre code de test.
         *      - Indépendance : Cela ne nécessite pas d’annotations ou d’extensions JUnit.
         *      - Verbosité : Le code est un peu plus verbeux, car vous devez explicitement appeler Mockito.mock().
         *
         * Quand utiliser @Mock ?
         *    - Lorsque vous écrivez des tests unitaires avec JUnit.
         *    - Lorsque vous voulez une syntaxe plus concise et lisible.
         *    - Lorsque vous utilisez des frameworks comme Spring Boot Test ou MockitoExtension.
         *
         * Quand utiliser Mockito.mock() ?
         *    - Lorsque vous ne voulez pas dépendre de JUnit ou d’annotations.
         *    - Lorsque vous avez besoin de créer des mocks dans des endroits spécifiques (par exemple, dans des méthodes utilitaires ou des tests personnalisés).
         *    - Lorsque vous travaillez en dehors d’un contexte de test JUnit.
         */
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(10);
        when(resultSet.getInt("age")).thenReturn(29);
        when(resultSet.getString("name")).thenReturn("kamila");
        when(resultSet.getString("email")).thenReturn("kamila@gmail.com");

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 0);

        // Then
        Customer expected = new Customer(10, "kamila", "kamila@gmail.com", 29);

        assertThat(actual).isEqualTo(expected);
    }
}
