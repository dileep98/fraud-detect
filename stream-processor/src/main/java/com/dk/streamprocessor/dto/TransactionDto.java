package com.dk.streamprocessor.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDto(
        String txId,
        String accountId,
        String merchantId,
        BigDecimal amount,
        String currency,
        String channel,
        String ip,
        String deviceId,
        Instant eventTime,
        Integer score,
        String decision,
        String reason
) {}
