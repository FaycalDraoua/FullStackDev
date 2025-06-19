create table customer(
    id Bigserial primary key,
    name TEXT not null,
    email TEXT not null,
    age INT not null
 );

/* si je pouvais creer la table et la sequence de la maniere qui va suivre afin d'avoir plus de controle. sur le
 de la sequence par exemple qui devrait matcher avec le nom celui deja creer dans l'Entity customer

create sequence customer_id_sequence;
create table customer(
    id Bigint not null default nextval('customer_id_sequence'),
    name TEXT not null,
    email TEXT not null,
    age INT not null,
    primary key (id)
);*/