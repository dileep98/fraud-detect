package com.dk.streamprocessor.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "alerts")
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_id", nullable = false)
    private String txId;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private String decision; // REVIEW/DECLINE

    @Column
    private String reason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

}
