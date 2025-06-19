CREATE TABLE customer (
                          id BIGSERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          email TEXT NOT NULL,
                          age INT NOT NULL
);

/*
Si je voulais créer la table et la séquence séparément, pour avoir plus de contrôle — 
par exemple sur le nom de la séquence, afin qu’il corresponde exactement à celui défini dans l'entité `Customer` — 
je pourrais faire comme suit :

CREATE SEQUENCE customer_id_sequence;

CREATE TABLE customer (
    id BIGINT NOT NULL DEFAULT nextval('customer_id_sequence'),
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    age INT NOT NULL,
    PRIMARY KEY (id)
);
*/
