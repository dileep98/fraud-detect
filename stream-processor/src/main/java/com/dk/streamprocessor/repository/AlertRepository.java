package com.dk.streamprocessor.repository;

import com.dk.streamprocessor.entity.AlertEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByTxId(String txId);

    Page<AlertEntity> findAll(Pageable pageable);

    Page<AlertEntity> findByDecisionIgnoreCase(String decision, Pageable pageable);

}
