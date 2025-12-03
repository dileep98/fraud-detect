package com.dk.streamprocessor.repository;

import com.dk.streamprocessor.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
}
