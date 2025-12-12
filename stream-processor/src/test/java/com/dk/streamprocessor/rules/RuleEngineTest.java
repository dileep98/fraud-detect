package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {

    private RuleEngine newEngine(int reviewMin, int declineMin) {
        FraudRulesProperties props = new FraudRulesProperties();
        FraudRulesProperties.Decision decision = props.getDecision();
        decision.setReviewMinScore(reviewMin);
        decision.setDeclineMinScore(declineMin);
        return new RuleEngine(Collections.emptyList(), props);
    }

    @Test
    void approveWhenScoreBelowReviewThreshold() {
        RuleEngine engine = newEngine(30, 60);

        Decision decision = engine.decide(10);

        assertEquals(Decision.APPROVE, decision);
    }

    @Test
    void reviewWhenScoreBetweenReviewAndDecline() {
        RuleEngine engine = newEngine(30, 60);

        Decision decision = engine.decide(40);

        assertEquals(Decision.REVIEW, decision);
    }

    @Test
    void declineWhenScoreAboveDeclineThreshold() {
        RuleEngine engine = newEngine(30, 60);

        Decision decision = engine.decide(70);

        assertEquals(Decision.DECLINE, decision);
    }

    @Test
    void thresholdsAreConfigurable() {
        RuleEngine engine = newEngine(10, 20);

        assertEquals(Decision.APPROVE, engine.decide(5));
        assertEquals(Decision.REVIEW, engine.decide(15));
        assertEquals(Decision.DECLINE, engine.decide(25));
    }
}
