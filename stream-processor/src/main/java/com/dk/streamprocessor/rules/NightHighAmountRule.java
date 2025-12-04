package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.entity.TransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class NightHighAmountRule implements RiskRule{
    /**
     * @param tx
     * @param result
     */
    @Override
    public void apply(TransactionEntity tx, RuleResult result) {
        if (tx.getEventTime() == null || tx.getAmount() == null) return;

        LocalDateTime local =
                LocalDateTime.ofInstant(tx.getEventTime(), ZoneOffset.UTC);
        int hour = local.getHour();

        if (hour >= 0 && hour <= 5 &&
                tx.getAmount().compareTo(new BigDecimal("200")) >= 0) {
            result.add(15, "NIGHT_TX");
        }
    }
}
