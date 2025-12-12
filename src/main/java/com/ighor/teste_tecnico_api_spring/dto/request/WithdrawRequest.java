package com.ighor.teste_tecnico_api_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotNull(message = "Id da conta é obrigatório!") Long fromAccountId,
        @NotNull @Positive(message = "Valor deve ser maior que 0!") BigDecimal amount
) {
}
