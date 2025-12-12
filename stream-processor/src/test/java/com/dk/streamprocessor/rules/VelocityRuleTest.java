package com.dk.streamprocessor.rules;


import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import com.dk.streamprocessor.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VelocityRuleTest {

    @Mock
    TransactionRepository transactionRepository;

    private VelocityRule newRule(int windowMinutes, int maxCount, int score, boolean enabled){
        FraudRulesProperties props = new FraudRulesProperties();
        FraudRulesProperties.Velocity velocity = props.getVelocity();

        velocity.setEnabled(enabled);
        velocity.setWindowMinutes(windowMinutes);
        velocity.setMaxCount(maxCount);
        velocity.setScore(score);

        return new VelocityRule(props, transactionRepository);
    }


    @Test
    void addsScoreWhenRecentCountExceedsMax(){
        VelocityRule rule = newRule(5, 3, 30, true);

        TransactionEntity tx = new TransactionEntity();

        tx.setAccountId("ACC-1");
        tx.setEventTime(Instant.now());

        // simulate 3 previous tx within window
        when(transactionRepository.countByAccountIdAndEventTimeAfter(
                org.mockito.ArgumentMatchers.eq("ACC-1"),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(3L);

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(30, result.getScoreDelta());
        assertTrue(result.getReasons().contains("VELOCITY"));
    }


    @Test
    void doesNothingWhenRecentCountBelowMax() {
        VelocityRule rule = newRule(5, 3, 30, true);

        TransactionEntity tx = new TransactionEntity();
        tx.setAccountId("ACC-1");
        tx.setEventTime(Instant.now());

        when(transactionRepository.countByAccountIdAndEventTimeAfter(
                org.mockito.ArgumentMatchers.eq("ACC-1"),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(1L);

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }


    @Test
    void doesNothingWhenDisabled() {
        VelocityRule rule = newRule(5, 3, 30, false);

        TransactionEntity tx = new TransactionEntity();
        tx.setAccountId("ACC-1");
        tx.setEventTime(Instant.now());

        RuleResult result = new RuleResult();

        rule.apply(tx, result);

        assertEquals(0, result.getScoreDelta());
        assertTrue(result.getReasons().isEmpty());
    }

}
