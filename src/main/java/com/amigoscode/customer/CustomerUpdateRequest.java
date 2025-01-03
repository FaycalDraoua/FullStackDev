package com.amigoscode.customer;

public record CustomerUpdateRequest(
        Integer id,
        String name,
        String email,
        Integer age
){


}
