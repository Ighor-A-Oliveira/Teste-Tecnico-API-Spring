package com.ighor.teste_tecnico_api_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

//Oq estou pedindo para criar a transaction
public record DepositRequest(
        @NotNull(message = "Id da conta é obrigatório!") Long fromAccountId,
        @NotNull @Positive(message = "Valor deve ser maior que 0!") BigDecimal amount
) {
}
