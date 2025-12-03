package com.dk.streamprocessor.mapper;

import com.dk.streamprocessor.dto.AlertDto;
import com.dk.streamprocessor.dto.TransactionDto;
import com.dk.streamprocessor.entity.AlertEntity;
import com.dk.streamprocessor.entity.TransactionEntity;

public class Mapper {

    public static TransactionDto toDto(TransactionEntity e) {
        return new TransactionDto(
                e.getTxId(),
                e.getAccountId(),
                e.getMerchantId(),
                e.getAmount(),
                e.getCurrency(),
                e.getChannel(),
                e.getIp(),
                e.getDeviceId(),
                e.getEventTime(),
                e.getScore(),
                e.getDecision(),
                e.getReason()
        );
    }

    public static AlertDto toDto(AlertEntity a) {
        return new AlertDto(
                a.getId(),
                a.getTxId(),
                a.getScore(),
                a.getDecision(),
                a.getReason(),
                a.getCreatedAt()
        );
    }
}
