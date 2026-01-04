package com.meli.wallet.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private BigDecimal amount;

    @Column(unique = true)
    private String idempotencyKey;
    private String status;
    private LocalDateTime createdAt;
}
