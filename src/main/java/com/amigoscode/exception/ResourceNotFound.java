package com.amigoscode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/* cette anotation nous permet de modifier la reponse par defaut.
    par exemple dans ce cas, au lieu de recevoir une erreur de serveur 500 au cas ou l'ID nexiste pas,
    je vais plus tot envoyer un "Not Fund" qui est plus signifiant au utilisateur.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(String message) {
        super(message);
    }
}
