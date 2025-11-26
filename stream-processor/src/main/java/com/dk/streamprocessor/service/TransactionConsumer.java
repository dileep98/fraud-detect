package com.dk.streamprocessor.service;

import com.dk.streamprocessor.entity.TransactionEntity;
import com.dk.streamprocessor.messaging.TransactionEvent;
import com.dk.streamprocessor.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final ObjectMapper objectMapper;
    private final TransactionRepository transactionRepository;

    @KafkaListener(topics = "${fraud.kafka.tx-topic}", groupId = "${fraud.kafka.group-id}")
    public void onMessage(ConsumerRecord<String, String> record){

        String key = record.key();
        String payload = record.value();

        try {

            TransactionEvent event = objectMapper.readValue(payload, TransactionEvent.class);
            log.info("Consumed tx from Kafka: key={}, txId={}, accountId={}, amount={}, currency={}, channel={}",
                    key,
                    event.getTxId(),
                    event.getAccountId(),
                    event.getAmount(),
                    event.getCurrency(),
                    event.getChannel()
            );

            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setTxId(event.getTxId());
            transactionEntity.setAccountId(event.getAccountId());
            transactionEntity.setMerchantId(event.getMerchantId());
            transactionEntity.setAmount(event.getAmount());
            transactionEntity.setCurrency(event.getCurrency());
            transactionEntity.setChannel(event.getChannel());
            transactionEntity.setIp(event.getIp());
            transactionEntity.setDeviceId(event.getDeviceId());
            transactionEntity.setEventTime(event.getEventTime());

            transactionRepository.save(transactionEntity);

        }catch (Exception e){
            log.error("Failed to deserialize or persist TransactionEvent: {}", payload, e);
        }
    }
}
