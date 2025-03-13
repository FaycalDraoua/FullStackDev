package com.amigoscode.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        /**
         * On a vu jusqu'au la l'utilisation de Mockito de deux maniere:
            1. utiliser l'annotation @Mock
            2. utiliser ce que je viens juste d'utiliser just en bas. "instance = Mockito.mock(nomClass.class)"
         */
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(10);
        when(resultSet.getInt("age")).thenReturn(29);
        when(resultSet.getString("name")).thenReturn("kamila");
        when(resultSet.getString("email")).thenReturn("kamila@gmail.com");

        //When
        Customer actual = customerRowMapper.mapRow(resultSet, 0);u

        //Then
        Customer custumer = new Customer(10,"kamila","kamila@gmail.com",29);

        assertThat(actual).isEqualTo(custumer);

    }
}