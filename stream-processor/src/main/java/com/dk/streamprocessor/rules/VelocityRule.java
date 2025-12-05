package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import com.dk.streamprocessor.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class VelocityRule implements RiskRule{

    private final FraudRulesProperties.Velocity props;
    private final TransactionRepository transactionRepository;

    public VelocityRule(FraudRulesProperties fraudRulesProperties, TransactionRepository transactionRepository) {
        this.props = fraudRulesProperties.getVelocity();
        this.transactionRepository = transactionRepository;
    }


    /**
     * @param tx
     * transaction entity has all the transaction data
     * @param result
     * to se the result to the RuleResult object
     */
    @Override
    public void apply(TransactionEntity tx, RuleResult result) {
        if(!props.isEnabled())
            return;

        if(tx.getAccountId() == null || tx.getEventTime() == null)
            return;

        Instant windowStart = tx.getEventTime()
                .minus(props.getWindowMinutes(), ChronoUnit.MINUTES);

        long recentCount = transactionRepository.countByAccountIdAndEventTimeAfter(tx.getAccountId(), windowStart);

        if (recentCount >= props.getMaxCount()) {
            result.add(props.getScore(), "VELOCITY");
        }

    }
}
