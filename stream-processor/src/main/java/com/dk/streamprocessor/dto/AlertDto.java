package com.dk.streamprocessor.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record AlertDto(
        Long id,
        String txId,
        Integer score,
        String decision,
        String reason,
        Instant createdAt
) {}

