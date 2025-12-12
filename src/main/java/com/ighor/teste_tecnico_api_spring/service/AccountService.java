package com.ighor.teste_tecnico_api_spring.service;

import com.ighor.teste_tecnico_api_spring.dto.request.RegisterAccountRequest;
import com.ighor.teste_tecnico_api_spring.entity.Account;
import com.ighor.teste_tecnico_api_spring.entity.User;
import com.ighor.teste_tecnico_api_spring.repository.AccountRepository;
import com.ighor.teste_tecnico_api_spring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Long generateUniqueAccountNumber() {
        Long number;

        do {
            number = ThreadLocalRandom.current()
                    .nextLong(10_000_000L, 99_999_999L);
        } while (accountRepository.findByAccountNumber(number).isPresent());

        return number;
    }

    @Transactional
    public Account createAccount(RegisterAccountRequest registerAccountRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "ANÔNIMO";

        //Criando conta
        Account newAccount = new Account();
        //Definindo saldo inicial
        newAccount.setBalance(BigDecimal.ZERO);
        //Definindo status como ativo
        newAccount.setStatus(Account.AccountStatus.ACTIVE);
        //Busco o registro de User de quem esta querendo criar conta
        User user = userRepository.findByCpf(registerAccountRequest.cpf())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        newAccount.setOwner(user);
        boolean saved = false;

        //Vai tentando ate gerar um numero valido
        while (!saved) {
            try {
                newAccount.setAccountNumber(generateUniqueAccountNumber());
                logger.info("Usuário: {}, Endpoint: {}, Data: {}, Payload: {}",
                        username,
                        "/transaction/deposit",
                        LocalDateTime.now(),
                        registerAccountRequest
                );
                newAccount = accountRepository.save(newAccount);
                saved = true;
            } catch (DataIntegrityViolationException e) {
                // Número gerado estava duplicado, tenta novamente
                // O loop segue para gerar outro
            }
        }

        return newAccount;
    }

}
