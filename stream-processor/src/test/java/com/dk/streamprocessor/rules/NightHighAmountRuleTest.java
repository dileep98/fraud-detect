package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class NightHighAmountRuleTest {

    private NightHighAmountRule newRule(
            boolean enabled,
            int minAmount,
            int startHour,
            int endHour,
            int score
    ) {
        FraudRulesProperties props = new FraudRulesProperties();
        FraudRulesProperties.NightHighAmount night = props.getNightHighAmount();
        night.setEnabled(enabled);
        night.setMinAmount(minAmount);
        night.setStartHour(startHour);
        night.setEndHour(endHour);
        night.setScore(score);
        return new NightHighAmountRule(props);
    }

    private Instant atUtcHour(int hour) {
        // 2025-01-01 at given hour UTC, arbitrary date
        return LocalDateTime.of(2025, 1, 1, hour, 0)
                .toInstant(ZoneOffset.UTC);
    }

    @Test
    void addsScoreWhenWithinNightWindowAndAboveMinAmount() {
        NightHighAmountRule rule = newRule(
                true,   // enabled
                200,    // minAmount
                0,      // startHour
                5,      // endHour
                15      // score
        );

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("250"));
        tx.setEventTime(atUtcHour(2)); // 02:00 UTC

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(15, result.getScoreDelta());
        assertTrue(result.getReasons().contains("NIGHT_TX"));
    }

    @Test
    void doesNothingOutsideNightWindow() {
        NightHighAmountRule rule = newRule(true, 200, 0, 5, 15);

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("250"));
        tx.setEventTime(atUtcHour(10)); // 10:00 UTC

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void doesNothingWhenAmountBelowMinAmount() {
        NightHighAmountRule rule = newRule(true, 200, 0, 5, 15);

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("150")); // below threshold
        tx.setEventTime(atUtcHour(2));       // in window

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void doesNothingWhenRuleDisabled() {
        NightHighAmountRule rule = newRule(false, 200, 0, 5, 15);

        TransactionEntity tx = new TransactionEntity();
        tx.setAmount(new BigDecimal("300"));
        tx.setEventTime(atUtcHour(2));

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void doesNothingWhenMissingEventTimeOrAmount() {
        NightHighAmountRule rule = newRule(true, 200, 0, 5, 15);

        // missing eventTime
        TransactionEntity tx1 = new TransactionEntity();
        tx1.setAmount(new BigDecimal("250"));

        RuleResult r1 = new RuleResult();
        rule.apply(tx1, r1);
        assertEquals(0, r1.getScoreDelta());
        assertTrue(r1.getReasons().isEmpty());

        // missing amount
        TransactionEntity tx2 = new TransactionEntity();
        tx2.setEventTime(atUtcHour(2));

        RuleResult r2 = new RuleResult();
        rule.apply(tx2, r2);
        assertEquals(0, r2.getScoreDelta());
        assertTrue(r2.getReasons().isEmpty());
    }
}
