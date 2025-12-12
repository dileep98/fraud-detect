package com.dk.streamprocessor.repository;

import com.dk.streamprocessor.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    List<TransactionEntity> findByAccountId(String accountId);

    Page<TransactionEntity> findAll(Pageable pageable);

    Page<TransactionEntity> findByAccountId(String accountId, Pageable pageable);

    long countByAccountIdAndEventTimeAfter(String accountId, Instant eventTime);

}

