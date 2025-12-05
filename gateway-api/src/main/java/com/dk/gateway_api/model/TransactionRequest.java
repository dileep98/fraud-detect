package com.dk.gateway_api.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private String txId;

    @NotBlank
    private String accountId;

    @NotBlank
    private String merchantId;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;  // e.g., "USD"

    @NotBlank
    private String channel;   // e.g., "CARD_PRESENT" / "CARD_NOT_PRESENT"

    @NotBlank
    private String ip;

    private String deviceId;

    private Double latitude;
    private Double longitude;

//    private Long eventTimeEpochMs;  // client event timestamp
}