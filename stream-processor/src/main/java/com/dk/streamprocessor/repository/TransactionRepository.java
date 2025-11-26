package com.dk.streamprocessor.repository;

import com.dk.streamprocessor.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {


}
