package com.modelcontroller.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private JdbcTemplate jdbcTemplate;
    private CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        var sql = """
                    select id, name, email, age
                    from customer
                    """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        var sql = """
                select id, name, email, age
                from customer
                where id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();

        /*
        PS : si vous remarquez bien, on a mis un seul "?" dans notre requête SQL,
        donc il faut passer cet argument dans une méthode de jdbcTemplate.
        Même chose plus bas pour la méthode insertCustomer() : on a mis 3 "?" dans la requête SQL,
        donc on utilise la méthode qui accepte les 3 valeurs à insérer afin de correspondre à la requête SQL.
        */
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
    }

    @Override
    public boolean existePersonWithEmail(String email) {
        var sql = """
                select count(*)
                from customer
                where email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        // Retourner "true" si les 2 conditions sont vraies, c’est-à-dire count != null et count > 0
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = """
                delete from customer where id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        var sql = """
                select id, name, email, age
                from customer
                where id = ?
                """;
        Optional<Customer> optCustomer = jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();

        return optCustomer.isPresent();
    }

    @Override
    public void updateCustomer(Customer customer) {

        Customer actual = selectCustomerById(customer.getId()).orElseThrow();

        if (customer.getName() != null && !customer.getName().isEmpty() && !customer.getName().equals(actual.getName())) {
            var sql = """
                    update customer
                    set name = ?
                    where id = ?
                    """;
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }

        if (customer.getEmail() != null && !customer.getEmail().isEmpty() && !customer.getEmail().equals(actual.getEmail())) {
            var sql = """
                    update customer
                    set email = ?
                    where id = ?
                    """;
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }

        if (customer.getAge() != null && !customer.getAge().equals(actual.getAge())) {
            var sql = """
                    update customer
                    set age = ?
                    where id = ?
                    """;
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }
    }
}
