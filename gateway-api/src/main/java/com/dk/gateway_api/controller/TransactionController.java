package com.dk.gateway_api.controller;

import com.dk.gateway_api.messaging.TransactionEvent;
import com.dk.gateway_api.messaging.TransactionProducer;
import com.dk.gateway_api.model.TransactionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/v1/tx")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionProducer producer;

    @PostMapping
    public ResponseEntity<Void> ingestTransaction(@Valid @RequestBody TransactionRequest request) {

        TransactionEvent event = new TransactionEvent(
                request.getTxId(),
                request.getAccountId(),
                request.getMerchantId(),
                request.getAmount(),
                request.getCurrency(),
                request.getIp(),
                request.getDeviceId(),
                request.getChannel(),
                Instant.now()
        );

        log.info("Publishing tx {} to Kafka topic tx.incoming", request.getTxId());
        producer.send(event);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
