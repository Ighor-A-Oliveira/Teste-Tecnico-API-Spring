package com.ighor.teste_tecnico_api_spring.repository;

import com.ighor.teste_tecnico_api_spring.entity.Account;
import com.ighor.teste_tecnico_api_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(Long accountNumber);

    Optional<Account> findById(Long AccountId);

    Optional<Account> findByOwner(User user);

}
