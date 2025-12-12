package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HighAmountRuleTest {

    private HighAmountRule newRule(int threshold, int score, boolean enabled) {
        FraudRulesProperties props = new FraudRulesProperties();
        FraudRulesProperties.HighAmount highAmount = props.getHighAmount();
        highAmount.setEnabled(enabled);
        highAmount.setThreshold(threshold);
        highAmount.setScore(score);
        return new HighAmountRule(props);
    }

    @Test
    void addsScoreWhenAmountAboveOrEqualThreshold() {
        HighAmountRule rule = newRule(1000, 40, true);

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("1500"));

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(40, result.getScoreDelta());
        assertTrue(result.getReasons().contains("HIGH_AMOUNT"));
    }

    @Test
    void doesNothingWhenAmountBelowThreshold() {
        HighAmountRule rule = newRule(1000, 40, true);

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("500"));

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void doesNothingWhenRuleDisabled() {
        HighAmountRule rule = newRule(1000, 40, false);

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("1500"));

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }
}
