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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final ObjectMapper objectMapper;
    private final RuleEngine ruleEngine;
    private final TransactionRepository transactionRepository;
    private final AlertRepository alertRepository;
    private final MeterRegistry meterRegistry;


    private Timer txProcessTimer() {
        // lazy-created timer (Micrometer reuses by name+tags)
        return Timer.builder("fraud_tx_process_duration_seconds")
                .description("Time taken to process a single transaction from Kafka")
                .register(meterRegistry);
    }

    @KafkaListener(topics = "${fraud.kafka.tx-topic}", groupId = "${fraud.kafka.group-id}")
    public void onMessage(ConsumerRecord<String, String> record){

        String key = record.key();
        String payload = record.value();

        txProcessTimer().record(() -> {
            try {

                TransactionEvent event = objectMapper.readValue(payload, TransactionEvent.class);
                log.info("Consumed tx from Kafka: topic={}, partition={}, offset={}, key={}, txId={}, amount={}, channel={}",
                        record.topic(), record.partition(), record.offset(),
                        key, event.getTxId(), event.getAmount(), event.getChannel());


                TransactionEntity transactionEntity = new TransactionEntity();
                transactionEntity.setTxId(event.getTxId());
                transactionEntity.setAccountId(event.getAccountId());
                transactionEntity.setMerchantId(event.getMerchantId());
                transactionEntity.setAmount(event.getAmount());
                transactionEntity.setCurrency(event.getCurrency());
                transactionEntity.setChannel(event.getChannel());
                transactionEntity.setIp(event.getIp());
                transactionEntity.setDeviceId(event.getDeviceId());
                transactionEntity.setEventTime(event.getEventTime() != null ? event.getEventTime() : Instant.now());

                RuleResult ruleResult = ruleEngine.evaluate(transactionEntity);
                int score = ruleResult.getScoreDelta();
                Decision decision = ruleEngine.decide(score);

                transactionEntity.setScore(score);
                transactionEntity.setDecision(decision.name());
                transactionEntity.setReason(ruleResult.reasonsAsString());


                transactionRepository.save(transactionEntity);

                // Record decision metric
                meterRegistry.counter(
                        "fraud_decisions_total",
                        "decision", decision.name()
                ).increment();

                if (decision != Decision.APPROVE) { // In case I add more decisions other than "APPROVE"
                    AlertEntity alertEntity = new AlertEntity();
                    alertEntity.setTxId(event.getTxId());
                    alertEntity.setDecision(decision.name());
                    alertEntity.setScore(score);
                    alertEntity.setReason(ruleResult.reasonsAsString());
                    alertRepository.save(alertEntity);

                    meterRegistry.counter(
                            "fraud_alerts_total",
                            "decision", decision.name()
                    ).increment();
                    log.info("Created alert for tx {}: score={}, decision={}, reasons={}",
                            transactionEntity.getTxId(), score, decision, ruleResult.reasonsAsString());
                } else {
                    log.info("Approved tx {} with score {}", transactionEntity.getTxId(), score);
                }


            } catch (Exception e) {
                log.error(
                        "Failed to process message from topic={} partition={} offset={} payload={}",
                        record.topic(), record.partition(), record.offset(), payload
                );
                log.error("Exception while processing message", e);

                // Optionally you can also track failures:
                meterRegistry.counter("fraud_tx_process_failures_total").increment();
            }
        });
    }
}
