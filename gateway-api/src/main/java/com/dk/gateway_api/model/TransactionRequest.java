package com.dk.gateway_api.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import java.math.BigDecimal;

public record TransactionRequest (


    @NotBlank String accountId,
    @NotBlank String merchantId,
    @NotNull @Positive BigDecimal amount,
    @NotBlank String currency,
    @NotBlank String channel,
    @NotBlank String ip,
    @NotBlank String deviceId,

    Double latitude,
    Double longitude

//    private Long eventTimeEpochMs;  // client event timestamp
){}