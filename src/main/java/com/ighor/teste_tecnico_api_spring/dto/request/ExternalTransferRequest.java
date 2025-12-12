package com.ighor.teste_tecnico_api_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ExternalTransferRequest(
        //Source account
        @NotNull(message = "A conta de origem não pode ser nula") Long fromAccountId,  // conta de origem no meu bd

        @NotNull(message = "O valor não pode ser nulo")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount,          // valor a ser transferido
        //Bank
        @NotNull(message = "O código do banco não pode ser nulo")
        @Size(min = 3, max = 3, message = "O código do banco deve ter 3 dígitos")
        String toBankCode,         // código do banco na api
        //Destination account
        @NotNull(message = "A agência não pode ser nula")
        @Size(min = 4, max = 4, message = "A agência deve ter 4 dígitos")
        String toAgency,           // codigo de agenncia na api
        @NotNull(message = "O número da conta não pode ser nulo")
        Long toAccountNumber,    // número da conta destino na api
        @NotNull(message = "O CPF do destinatário não pode ser nulo")
        @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
        String toAccountHolderCpf // CPF do destinatário

) {}
