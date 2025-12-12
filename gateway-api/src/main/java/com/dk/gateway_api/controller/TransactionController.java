package com.dk.gateway_api.controller;

import com.dk.gateway_api.messaging.TransactionEvent;
import com.dk.gateway_api.messaging.TransactionProducer;
import com.dk.gateway_api.model.TransactionRequest;
import com.dk.gateway_api.model.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/tx")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionProducer producer;

    @PostMapping
    public ResponseEntity<TransactionResponse> ingestTransaction(@Valid @RequestBody TransactionRequest request) {

        String txId = UUID.randomUUID().toString();

        TransactionEvent event = new TransactionEvent(
                txId,
                request.accountId(),
                request.merchantId(),
                request.amount(),
                request.currency(),
                request.ip(),
                request.deviceId(),
                request.channel(),
                Instant.now()
        );

        log.info("Publishing tx {} to Kafka topic tx.incoming", txId);
        producer.send(event);

        return ResponseEntity.ok(new TransactionResponse(txId, "ACCEPTED"));
    }
}
