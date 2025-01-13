package com.amigoscode.customer;


import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private JdbcTemplate jdbcTemplate;
    private CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        var sql = """
                    select id,name,email,age
                    from customer
                    """;

        return jdbcTemplate.query(sql, customerRowMapper);

    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        var sql = """
                select id,name,email,age
                from customer
                where id=?
                """;
        return jdbcTemplate.query(sql,customerRowMapper,id).stream().findFirst();

        /*
        PS : si vous remarquer bien. On a mis un seul "?" dans notre requete SQL donc il faut passer cette argument
        dans une methode de jdbcTemplate.
        Memme chose en bas pour la methode insertCustomers(). on a mis 3 "?" dans la requette SQL donc on a utiliser
        la methode qui accepter les 3 valeur recherer afin de matcher avec la requette sql.
         */

    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name,email,age)
                VALUES (?,?,?)
                """;
        int update = jdbcTemplate.update(sql, customer.getName(),customer.getEmail(),customer.getAge());
    }

    @Override
    public boolean existePersonWithEmail(String email) {
        var sql = """
                select count(*)
                from customer
                where email=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);

        return count !=null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = """
                delete from customer where id=?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        var sql = """
                select id,name,email,age
                from customer
                where id=?
                """;
        Optional<Customer> optCustomer = jdbcTemplate.query(sql,customerRowMapper,id).stream().findFirst();

        return optCustomer.isPresent();
    }

    @Override
    public void updateCustomer(Customer customer) {
        var sql = """
                update customer
                set name=?,email=?,age=?
                where id=?
                """;
        jdbcTemplate.update(sql,customer.getName(),customer.getEmail(),customer.getAge(),customer.getId());
    }
}
