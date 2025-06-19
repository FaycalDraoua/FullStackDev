package com.modelcontroller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Cette annotation permet de modifier la réponse HTTP par défaut.
 * Par exemple, dans ce cas, au lieu de recevoir une erreur serveur 500 si l’ID n’existe pas,
 * on renvoie plutôt un "404 Not Found", qui est plus significatif pour l’utilisateur.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(String message) {
        super(message);
    }
}
