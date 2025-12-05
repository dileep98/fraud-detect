package com.dk.streamprocessor.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity {

    @Id
    @Column(name = "tx_id", nullable = false, updatable = false)
    private String txId;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(nullable = false)
    private String channel;

    @Column(nullable = false)
    private String ip;

    private String deviceId;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    // fraud-related
    @Column(name = "score")
    private Integer score;

    @Column(name = "decision")
    private String decision; // APPROVE/REVIEW/DECLINE

    @Column(name = "reason")
    private String reason;
}
