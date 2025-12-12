package com.ighor.teste_tecnico_api_spring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Table(name = "accounts")
@Entity(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @Column(name = "account_number", unique = true, nullable = false)
    private Long accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    // coluna que referencia a tabela User
    private User owner;

    @Column(nullable=false)
    private BigDecimal balance ;

    public enum AccountStatus {
        ACTIVE, INACTIVE
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;


    @PrePersist
    //Define o date time para mim automatico
    public void prePersist() {
        if (balance == null) balance = BigDecimal.ZERO;
        if (status == null) status = AccountStatus.ACTIVE;
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }


}

