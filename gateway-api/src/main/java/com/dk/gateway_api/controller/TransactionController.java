package com.dk.gateway_api.controller;

import com.dk.gateway_api.model.TransactionRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/tx")
public class TransactionController {

    @PostMapping
    public ResponseEntity<Void> ingestTransaction(@Valid @RequestBody TransactionRequest request) {
        // Day 1: just log. Day 2: send to Kafka.
        log.info("Received transaction: txId={}, accountId={}, amount={}, currency={}, channel={}",
                request.getTxId(),
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency(),
                request.getChannel()
        );

        // For asynchronous processing, 202 Accepted is nice
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}