package com.ighor.teste_tecnico_api_spring.dto.response;

import com.ighor.teste_tecnico_api_spring.entity.Account;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

//Oq eu quero mandar de volta ao usuario
public record RegisterAccountResponse(
        String accountNumber,
        BigDecimal balance,
        Account.AccountStatus status,
        OffsetDateTime createdAt
) {

}
