package com.ighor.teste_tecnico_api_spring.controller;

import com.ighor.teste_tecnico_api_spring.dto.request.RegisterAccountRequest;
import com.ighor.teste_tecnico_api_spring.dto.response.RegisterAccountResponse;
import com.ighor.teste_tecnico_api_spring.entity.Account;
import com.ighor.teste_tecnico_api_spring.repository.AccountRepository;
import com.ighor.teste_tecnico_api_spring.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;


    private  AccountController(AccountService accountService, AccountRepository accountRepository){
        this.accountService = accountService;

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterAccountResponse> createAccount(@Valid @RequestBody RegisterAccountRequest accountRequest){
        //joga a requisicao com os dados no service para cria a nova conta
        Account createdAccount = accountService.createAccount(accountRequest);
        //Agora usamos a nova conta para criar uma response
        RegisterAccountResponse response = new RegisterAccountResponse(
                String.valueOf(createdAccount.getAccountNumber()),
                createdAccount.getBalance(),
                createdAccount.getStatus(),
                createdAccount.getCreatedAt()
        );




        //Retorna uma resposta HTTP 201 (Created)
        //Responde 201 CREATED, dizendo que o usuário foi criado.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
