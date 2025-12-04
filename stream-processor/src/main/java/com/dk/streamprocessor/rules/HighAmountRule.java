package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.entity.TransactionEntity;

import java.math.BigDecimal;

public class HighAmountRule implements RiskRule{
    /**
     * @param tx
     * @param result
     */
    @Override
    public void apply(TransactionEntity tx, RuleResult result) {
        if (tx.getAmount() != null && tx.getAmount().compareTo(new BigDecimal("1000")) >= 0) {
            result.add(40, "HIGH_AMOUNT");
        }
    }
}
