package com.amigoscode.customer;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
/*
Avec l'annotation @Table je peux avoir le plien controle sur ma table. par exemple je peux renommer la la table comme
ca me chante, par defaut c'est le nom de la class en miniscul. vaux mieux laisser par default.
Ou par exemple definir les contraints des columns de cette table(entity)
 */
@Table(name = "customer",
        uniqueConstraints = {
        @UniqueConstraint(name = "customer_email_unique",columnNames = "email")
        })
public class Customer{

        @Id
        @SequenceGenerator(
            // ici (name) c'est le nom de l'id sequencce que j'utiliserai dans le code java. il n'affcete pas la BD
            name = "customer_id_seq",
            /*
             ici (sequenceName) c'est le nom de l'id sequence qui va exister reelement dans la BD.
             PS : la priorite va etre donner a flyway. cela veux dire que si le nom de la sequence est different dans
             flyway ou par defaut dans la BD(nomTable_nomColumn_seq) Hibernate va prendre ces nom la au lieu de celui la en dessou.
             */
            sequenceName = "customer_id_seq",
            //definir le numero du commencement de la sequence
            initialValue = 1,
            //definir le bloc de suite de sequence. par defaut c'est 50.
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            // ici le nom de la sequence id donner a (generateur) il doit faire etre le meme que celui de (name) en haut. car il lui fait reference.
            generator = "customer_id_seq"

    )
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private Integer age;


    public Customer() {}

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Customer(Integer id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer personne = (Customer) o;
        return Objects.equals(id, personne.id) && Objects.equals(name, personne.name) && Objects.equals(email, personne.email) && Objects.equals(age, personne.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
