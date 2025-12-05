package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngine {


    private final List<RiskRule> rules;
    private final FraudRulesProperties.Decision decisionProps;

    public RuleEngine(List<RiskRule> rules, FraudRulesProperties fraudRulesProperties) {
        this.rules = rules;
        this.decisionProps = fraudRulesProperties.getDecision();
    }

    public RuleResult evaluate(TransactionEntity tx) {
        RuleResult result = new RuleResult();
        for (RiskRule rule : rules) {
            rule.apply(tx, result);
        }
        return result;
    }

    public Decision decide(int score) {
        int declineMin = decisionProps.getDeclineMinScore();
        int reviewMin = decisionProps.getReviewMinScore();

        if (score >= declineMin) {
            return Decision.DECLINE;
        } else if (score >= reviewMin) {
            return Decision.REVIEW;
        } else {
            return Decision.APPROVE;
        }
    }

}
