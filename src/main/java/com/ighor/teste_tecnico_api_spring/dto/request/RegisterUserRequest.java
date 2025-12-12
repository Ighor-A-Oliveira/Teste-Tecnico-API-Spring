package com.ighor.teste_tecnico_api_spring.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(
        @NotEmpty(message = "Nome é obrigatorio")  String name,
        @NotEmpty(message = "Email é obrigatorio") String email,
        @NotEmpty(message = "Senha é obrigatoria") String password,
        @NotEmpty(message = "CPF é obrigatorio") String cpf
) {

}