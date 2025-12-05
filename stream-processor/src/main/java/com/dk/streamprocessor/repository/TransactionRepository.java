package com.dk.streamprocessor.repository;

import com.dk.streamprocessor.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    List<TransactionEntity> findByAccountId(String accountId);

    long countByAccountIdAndEventTimeAfter(String accountId, Instant eventTime);

}

