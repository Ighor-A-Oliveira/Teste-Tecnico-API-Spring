package com.ighor.teste_tecnico_api_spring.service;

import com.ighor.teste_tecnico_api_spring.dto.request.DepositRequest;
import com.ighor.teste_tecnico_api_spring.dto.request.ExternalTransferRequest;
import com.ighor.teste_tecnico_api_spring.dto.request.InternalTransferRequest;
import com.ighor.teste_tecnico_api_spring.dto.request.WithdrawRequest;
import com.ighor.teste_tecnico_api_spring.entity.Account;
import com.ighor.teste_tecnico_api_spring.entity.Transaction;
import com.ighor.teste_tecnico_api_spring.entity.TransactionType;
import com.ighor.teste_tecnico_api_spring.repository.AccountRepository;
import com.ighor.teste_tecnico_api_spring.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BankService bankService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, BankService bankService){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.bankService = bankService;
    }


    @Transactional
    public Transaction makeDeposit(DepositRequest depositRequest){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "ANÔNIMO";

        //Aqui tento procurar pela conta que vai receber o deposito
        Account account = accountRepository.findById(depositRequest.fromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        //Checa se o valor do deposito eh positivo
        if (depositRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo");
        }

        if (!account.getStatus().equals(Account.AccountStatus.ACTIVE)) {
            throw new IllegalArgumentException("Conta destino não está ativa");
        }

        //Saldo atual da conta
        BigDecimal currBalance = account.getBalance();
        //Valor a ser depositado
        BigDecimal depositValue = depositRequest.amount();
        //Soma dos dois valores
        BigDecimal newBalance = currBalance.add(depositValue);
        //Atualizando o saldo da conta
        account.setBalance(newBalance);
        //Fazendo update na conta na db
        accountRepository.save(account);

        //Criando objeto Transaction para registrar o deposito
        Transaction trans = new Transaction();
        //Definindo como tipo transaction
        trans.setType(TransactionType.DEPOSIT);
        //Definindo o valor a ser depositado
        trans.setAmount(depositRequest.amount());
        //Definindo a conta origem
        trans.setSourceAccount(account);
        //Definindo a conta destino como null
        trans.setDestinationAccount(null);
        //Definindo o saldo apos a operacao
        trans.setBalanceAfterOperation(newBalance);


        logger.info("Usuário: {}, Endpoint: {}, Data: {}, Payload: {}",
                username,
                "/transaction/deposit",
                LocalDateTime.now(),
                depositRequest
        );



        //retornando um objeto Transaction
        return transactionRepository.save(trans);

    }


    @Transactional
    public Transaction makeWithdrawal(WithdrawRequest withdrawRequest){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "ANÔNIMO";


        //Aqui tento procurar pela conta que vai receber o deposito
        Account account = accountRepository.findById(withdrawRequest.fromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        //Checa se o valor do saque eh positivo
        if (withdrawRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo");
        }

        if (withdrawRequest.amount().compareTo(account.getBalance()) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque");
        }

        if (!account.getStatus().equals(Account.AccountStatus.ACTIVE)) {
            throw new IllegalArgumentException("Conta destino não está ativa");
        }

        //Saldo atual da conta
        BigDecimal currBalance = account.getBalance();
        //Valor a ser sacado
        BigDecimal withdrawalValue = withdrawRequest.amount();
        //Subtracao dos dois valores
        BigDecimal newBalance = currBalance.subtract(withdrawalValue);
        //Atualizando o saldo da conta
        account.setBalance(newBalance);
        //Fazendo update na conta na db
        accountRepository.save(account);

        //Criando objeto Transaction para registrar o deposito
        Transaction trans = new Transaction();
        //Definindo como tipo transaction
        trans.setType(TransactionType.WITHDRAW);
        //Definindo o valor a ser sacado
        trans.setAmount(withdrawRequest.amount());
        //Definindo a conta origem
        trans.setSourceAccount(account);
        //Definindo a conta destino como null
        trans.setDestinationAccount(null);
        //Definindo o saldo apos a operacao
        trans.setBalanceAfterOperation(newBalance);

        logger.info("Usuário: {}, Endpoint: {}, Data: {}, Payload: {}",
                username,
                "/transaction/withdraw",
                LocalDateTime.now(),
                withdrawRequest
        );


        //retornando um objeto Transaction
        return transactionRepository.save(trans);
    }

    @Transactional
    public Transaction makeInternalTransfer(InternalTransferRequest internalTransferRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "ANÔNIMO";

        //Aqui tento procurar pela conta que vai receber o deposito
        Account sourceAccount = accountRepository.findById(internalTransferRequest.fromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        Account destinationAccount = accountRepository.findById(internalTransferRequest.toAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        //Checa se o valor do saque eh positivo
        if (internalTransferRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferencia deve ser positivo");
        }

        if (internalTransferRequest.amount().compareTo(sourceAccount.getBalance()) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferencia");
        }

        if (!destinationAccount.getStatus().equals(Account.AccountStatus.ACTIVE)) {
            throw new IllegalArgumentException("Conta destino não está ativa");
        }

        //CONTA ORIGEM
        //Saldo atual da conta
        BigDecimal currBalance = sourceAccount.getBalance();
        //Valor a ser tranferido
        BigDecimal transferValue = internalTransferRequest.amount();
        //Subtracao dos dois valores
        BigDecimal newBalance = currBalance.subtract(transferValue);
        //Atualizando o saldo da conta
        sourceAccount.setBalance(newBalance);
        //Fazendo update na conta na db
        accountRepository.save(sourceAccount);

        //CONTA DESTINO
        //Saldo atual da conta
        BigDecimal currBalanceDest = destinationAccount.getBalance();
        //Valor a ser tranferido
        BigDecimal transferValueDest = internalTransferRequest.amount();
        //Somando dos dois valores
        BigDecimal newBalanceDest = currBalanceDest.add(transferValueDest);
        //Atualizando o saldo da conta
        destinationAccount.setBalance(newBalanceDest);
        //Fazendo update na conta destino na db
        accountRepository.save(destinationAccount);

        //Criando objeto Transaction para registrar o deposito
        Transaction trans = new Transaction();
        //Definindo como tipo transaction
        trans.setType(TransactionType.TRANSFER_INTERNAL);
        //Definindo o valor a ser sacado
        trans.setAmount(internalTransferRequest.amount());
        //Definindo a conta origem
        trans.setSourceAccount(sourceAccount);
        //Definindo a conta destino
        trans.setDestinationAccount(destinationAccount);
        //Definindo o saldo apos a operacao
        trans.setBalanceAfterOperation(newBalance);


        logger.info("Usuário: {}, Endpoint: {}, Data: {}, Payload: {}",
                username,
                "/transaction/internal-transfer",
                LocalDateTime.now(),
                internalTransferRequest
        );

        //retornando um objeto Transaction
        return transactionRepository.save(trans);
    }


    @Transactional
    public Transaction makeExternalTransfer(ExternalTransferRequest externalTransferRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "ANÔNIMO";

        //Aqui tento procurar pela conta que vai receber o deposito
        Account sourceAccount = accountRepository.findById(externalTransferRequest.fromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));


        //Checa se o valor do saque eh positivo
        if (externalTransferRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferencia deve ser positivo");
        }

        if (externalTransferRequest.amount().compareTo(sourceAccount.getBalance()) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferencia");
        }

        if (!bankService.isBankValid(externalTransferRequest.toBankCode())) {
            throw new IllegalArgumentException("Banco inválido");
        }

        String cpf = externalTransferRequest.toAccountHolderCpf();

        if( externalTransferRequest.toAgency() == null || externalTransferRequest.toAgency().isBlank() ||
                externalTransferRequest.toBankCode() == null || externalTransferRequest.toBankCode().isBlank() ||
                externalTransferRequest.toAccountNumber() == null || cpf == null || externalTransferRequest.toAccountHolderCpf().length() != 11
        ){
            throw new IllegalArgumentException("Dados da conta destino inválidos");
        }


        //CONTA ORIGEM
        //Saldo atual da conta
        BigDecimal currBalance = sourceAccount.getBalance();
        //Valor a ser tranferido
        BigDecimal transferValue = externalTransferRequest.amount();
        //Subtracao dos dois valores
        BigDecimal newBalance = currBalance.subtract(transferValue);
        //Atualizando o saldo da conta
        sourceAccount.setBalance(newBalance);
        //Fazendo update na conta na db
        accountRepository.save(sourceAccount);




        //Criando objeto Transaction para registrar o deposito
        Transaction trans = new Transaction();
        //Definindo como tipo transaction
        trans.setType(TransactionType.TRANSFER_EXTERNAL);
        //Definindo o valor a ser sacado
        trans.setAmount(externalTransferRequest.amount());
        //Definindo a conta origem
        trans.setSourceAccount(sourceAccount);
        //Definindo a conta destino
        trans.setDestinationAccount(null);
        //Definindo o saldo apos a operacao
        trans.setBalanceAfterOperation(newBalance);

        logger.info("Usuário: {}, Endpoint: {}, Data: {}, Payload: {}",
                username,
                "/transaction/internal-transfer",
                LocalDateTime.now(),
                externalTransferRequest
        );


        //retornando um objeto Transaction
        return transactionRepository.save(trans);
    }
}
