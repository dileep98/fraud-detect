package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.entity.TransactionEntity;

public interface RiskRule {

    void apply(TransactionEntity tx, RuleResult result);

}
