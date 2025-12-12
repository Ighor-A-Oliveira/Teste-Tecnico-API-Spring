package com.ighor.teste_tecnico_api_spring.repository;

import com.ighor.teste_tecnico_api_spring.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(Long transactionId);
}
