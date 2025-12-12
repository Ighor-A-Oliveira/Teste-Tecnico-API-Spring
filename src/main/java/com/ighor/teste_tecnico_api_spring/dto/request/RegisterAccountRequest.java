package com.ighor.teste_tecnico_api_spring.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterAccountRequest(
        @NotEmpty(message = "Cpf é obrigatorio!") String cpf

) {

}
