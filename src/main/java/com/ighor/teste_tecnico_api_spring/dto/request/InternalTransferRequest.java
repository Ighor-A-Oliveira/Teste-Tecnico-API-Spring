package com.ighor.teste_tecnico_api_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InternalTransferRequest(
        @NotNull(message = "Conta origem é obrigatória!") Long fromAccountId,
        @NotNull(message = "Conta destino é obrigatória!") Long toAccountId,
        @NotNull @Positive(message = "Valor deve ser maior que 0!") BigDecimal amount
) {
}
