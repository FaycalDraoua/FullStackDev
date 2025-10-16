package com.modelcontroller.customer;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
/*
Avec l'annotation @Table, je peux avoir le plein contrôle sur ma table.
Par exemple, je peux renommer la table comme je veux. Par défaut, c’est le nom de la classe en minuscule.
Il vaut mieux laisser par défaut.
Je peux aussi y définir les contraintes sur les colonnes de cette table (entity).
*/
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                )
        }
)
public class Customer {


    @Id
    @SequenceGenerator(
            // Le "name" est le nom de la séquence utilisé dans le code Java. Il n'affecte pas la BD.
            name = "customer_id_seq",
            /*
             Le "sequenceName" est le nom de la séquence qui existera réellement dans la base de données.
             Remarque : La priorité est donnée à Flyway.
             Cela signifie que si la séquence est déjà définie par Flyway ou par défaut (sous forme nomTable_nomColumn_seq),
             Hibernate utilisera ce nom plutôt que celui défini ici.
             */
            sequenceName = "customer_id_seq",
            // Numéro de départ de la séquence
            initialValue = 1,
            // Taille du bloc de la séquence. Par défaut, c'est 50.
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            // Le "generator" doit correspondre au "name" défini dans @SequenceGenerator
            generator = "customer_id_seq"
    )
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer age;

    public Customer() {
    }

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
        return Objects.equals(id, personne.id) &&
                Objects.equals(name, personne.name) &&
                Objects.equals(email, personne.email) &&
                Objects.equals(age, personne.age);
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
