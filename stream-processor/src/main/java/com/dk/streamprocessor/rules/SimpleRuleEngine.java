package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.messaging.TransactionEvent;
import org.springframework.stereotype.Component;

@Component
public class SimpleRuleEngine {


    public record FraudDecision(int score, String decision, String reason) {}

    public FraudDecision evaluate(TransactionEvent event) {
        int score;
        String decision;
        String reason;

        if (event.getAmount() != null && event.getAmount().doubleValue() > 1000.0) {
            score = 80;
            decision = "REVIEW";
            reason = "HIGH_AMOUNT";
        } else {
            score = 10;
            decision = "APPROVE";
            reason = "LOW_RISK_BASELINE";
        }

        return new FraudDecision(score, decision, reason);
    }

}
