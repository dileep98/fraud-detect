package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.entity.TransactionEntity;

import java.math.BigDecimal;

public class CardNotPresentRule implements RiskRule{
    /**
     * @param tx
     * @param result
     */
    @Override
    public void apply(TransactionEntity tx, RuleResult result) {
        if ("CARD_NOT_PRESENT".equalsIgnoreCase(tx.getChannel())
                && tx.getAmount() != null
                && tx.getAmount().compareTo(new BigDecimal("500")) >= 0) {
            result.add(25, "CARD_NOT_PRESENT_HIGH");
        }
    }
}
