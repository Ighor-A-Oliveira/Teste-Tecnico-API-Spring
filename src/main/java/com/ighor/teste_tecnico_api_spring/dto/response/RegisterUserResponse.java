package com.ighor.teste_tecnico_api_spring.dto.response;

//Oq eu quero mandar de volta ao usuario
public record RegisterUserResponse(
        String name,
        String email,
        String cpf) {

}
