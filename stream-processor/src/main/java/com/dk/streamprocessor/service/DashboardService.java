package com.dk.streamprocessor.service;

import com.dk.streamprocessor.dto.DashboardStats;
import com.dk.streamprocessor.repository.AlertRepository;
import com.dk.streamprocessor.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AlertRepository alertRepository;

    public DashboardStats computeStats() {

        var allTx = transactionRepository.findAll();
        var allAlerts = alertRepository.findAll();

        long approve = allTx.stream()
                .filter(t -> "APPROVE".equalsIgnoreCase(t.getDecision()))
                .count();

        long review = allTx.stream()
                .filter(t -> "REVIEW".equalsIgnoreCase(t.getDecision()))
                .count();

        long decline = allTx.stream()
                .filter(t -> "DECLINE".equalsIgnoreCase(t.getDecision()))
                .count();

        return new DashboardStats(
                allTx.size(),
                allAlerts.size(),
                approve,
                review,
                decline
        );
    }
}
