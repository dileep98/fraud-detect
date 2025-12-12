package com.dk.gateway_api.model;

public record TransactionResponse(
        String txId,
        String status
) {}
