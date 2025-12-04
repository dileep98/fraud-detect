package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngine {


    private final List<RiskRule> rules;

    public RuleEngine() {
        this.rules = List.of(
                new HighAmountRule(),
                new CardNotPresentRule(),
                new NightHighAmountRule()
        );
    }

    public RuleResult evaluate(TransactionEntity tx) {
        RuleResult result = new RuleResult();
        for (RiskRule rule : rules) {
            rule.apply(tx, result);
        }
        return result;
    }

    public Decision decide(int score) {
        // can tweak thresholds as we like
        if (score >= 60) {
            return Decision.DECLINE;
        } else if (score >= 30) {
            return Decision.REVIEW;
        } else {
            return Decision.APPROVE;
        }
    }

}
