package com.dk.gateway_api.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEvent(
        String txId,
        String accountId,
        String merchantId,
        BigDecimal amount,
        String currency,
        String channel,
        String ip,
        String deviceId,
        Instant eventTime
) {}
