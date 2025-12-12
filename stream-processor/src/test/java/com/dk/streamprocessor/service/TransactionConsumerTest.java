package com.dk.streamprocessor.service;

import com.dk.streamprocessor.entity.AlertEntity;
import com.dk.streamprocessor.entity.TransactionEntity;
import com.dk.streamprocessor.messaging.TransactionEvent;
import com.dk.streamprocessor.repository.AlertRepository;
import com.dk.streamprocessor.repository.TransactionRepository;
import com.dk.streamprocessor.rules.Decision;
import com.dk.streamprocessor.rules.RuleEngine;
import com.dk.streamprocessor.rules.RuleResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionConsumerTest {

    @Mock
    ObjectMapper objectMapper;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AlertRepository alertRepository;

    @Mock
    RuleEngine ruleEngine;

    @InjectMocks
    TransactionConsumer consumer;

    private TransactionEvent sampleEvent() {
        return new TransactionEvent(
                "tx-123",
                "ACC-1",
                "M-1",
                new BigDecimal("1000"),
                "USD",
                "CARD_PRESENT",
                "1.2.3.4",
                "dev-1",
                Instant.now()
        );
    }

    private ConsumerRecord<String, String> recordForEvent(String key, String jsonPayload) {
        return new ConsumerRecord<>("tx.incoming", 0, 0L, key, jsonPayload);
    }

    @Test
    void savesTransactionWithoutAlertWhenDecisionApprove() throws Exception {
        TransactionEvent event = sampleEvent();
        String json = "{\"dummy\":\"json\"}";

        // ObjectMapper maps JSON -> TransactionEvent
        when(objectMapper.readValue(json, TransactionEvent.class))
                .thenReturn(event);

        // RuleEngine: score 10 -> APPROVE
        RuleResult ruleResult = new RuleResult();
        ruleResult.add(10, "SOME_REASON");

        when(ruleEngine.evaluate(any(TransactionEntity.class)))
                .thenReturn(ruleResult);
        when(ruleEngine.decide(10)).thenReturn(Decision.APPROVE);

        ConsumerRecord<String, String> record = recordForEvent(event.getTxId(), json);

        consumer.onMessage(record);

        // verify transaction saved
        ArgumentCaptor<TransactionEntity> txCaptor =
                ArgumentCaptor.forClass(TransactionEntity.class);
        verify(transactionRepository, times(1)).save(txCaptor.capture());
        TransactionEntity savedTx = txCaptor.getValue();

        assertEquals("tx-123", savedTx.getTxId());
        assertEquals("ACC-1", savedTx.getAccountId());
        assertEquals(10, savedTx.getScore());
        assertEquals("APPROVE", savedTx.getDecision());
        assertTrue(savedTx.getReason().contains("SOME_REASON"));

        // no alert should be saved
        verify(alertRepository, never()).save(any(AlertEntity.class));
    }

    @Test
    void savesTransactionAndAlertWhenDecisionNotApprove() throws Exception {
        TransactionEvent event = sampleEvent();
        String json = "{\"dummy\":\"json2\"}";

        when(objectMapper.readValue(json, TransactionEvent.class))
                .thenReturn(event);

        RuleResult ruleResult = new RuleResult();
        ruleResult.add(45, "HIGH_AMOUNT");
        ruleResult.add(30, "VELOCITY");
        // total score = 75

        when(ruleEngine.evaluate(any(TransactionEntity.class)))
                .thenReturn(ruleResult);
        when(ruleEngine.decide(75)).thenReturn(Decision.REVIEW);

        ConsumerRecord<String, String> record = recordForEvent(event.getTxId(), json);

        consumer.onMessage(record);

        // transaction saved
        ArgumentCaptor<TransactionEntity> txCaptor =
                ArgumentCaptor.forClass(TransactionEntity.class);
        verify(transactionRepository, times(1)).save(txCaptor.capture());
        TransactionEntity savedTx = txCaptor.getValue();

        assertEquals("tx-123", savedTx.getTxId());
        assertEquals("ACC-1", savedTx.getAccountId());
        assertEquals(75, savedTx.getScore());
        assertEquals("REVIEW", savedTx.getDecision());
        assertTrue(savedTx.getReason().contains("HIGH_AMOUNT"));
        assertTrue(savedTx.getReason().contains("VELOCITY"));

        // alert saved
        ArgumentCaptor<AlertEntity> alertCaptor =
                ArgumentCaptor.forClass(AlertEntity.class);
        verify(alertRepository, times(1)).save(alertCaptor.capture());
        AlertEntity alert = alertCaptor.getValue();

        assertEquals("tx-123", alert.getTxId());
        assertEquals(75, alert.getScore());
        assertEquals("REVIEW", alert.getDecision());
        assertTrue(alert.getReason().contains("HIGH_AMOUNT"));
        assertTrue(alert.getReason().contains("VELOCITY"));
    }

    @Test
    void handlesJsonParseErrorGracefully() throws Exception {
        String badJson = "{invalid-json}";
        ConsumerRecord<String, String> record = recordForEvent("tx-bad", badJson);

        when(objectMapper.readValue(badJson, TransactionEvent.class))
                .thenThrow(new RuntimeException("parse error"));

        // should not throw out of the method
        assertDoesNotThrow(() -> consumer.onMessage(record));

        // and should not attempt to save anything
        verify(transactionRepository, never()).save(any(TransactionEntity.class));
        verify(alertRepository, never()).save(any(AlertEntity.class));
    }
}
