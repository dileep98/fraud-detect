package com.dk.streamprocessor.dto;

public record DashboardStats(
        long totalTransactions,
        long totalAlerts,
        long approveCount,
        long reviewCount,
        long declineCount
) {}
