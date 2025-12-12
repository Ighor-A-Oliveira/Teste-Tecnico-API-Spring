package com.ighor.teste_tecnico_api_spring.dto.response;

import com.ighor.teste_tecnico_api_spring.entity.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

//Oq vou mandar de volta
public record TransactionResponse(
        Long id,
        TransactionType type,
        BigDecimal amount,
        Long sourceAccountId,
        Long destinationAccountId,
        OffsetDateTime timestamp,
        BigDecimal balanceAfterOperation
) {}
