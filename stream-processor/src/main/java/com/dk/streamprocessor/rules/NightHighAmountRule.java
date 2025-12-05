package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class NightHighAmountRule implements RiskRule{

    private final FraudRulesProperties.NightHighAmount props;

    public NightHighAmountRule(FraudRulesProperties fraudRulesProperties) {
        this.props = fraudRulesProperties.getNightHighAmount();
    }

    /**
     * @param tx
     * transaction entity has all the transaction data
     * @param result
     * to se the result to the RuleResult object
     */
    @Override
    public void apply(TransactionEntity tx, RuleResult result) {
        if (!props.isEnabled() || tx.getAmount() == null || tx.getEventTime() == null) {
            return;
        }

        LocalDateTime local =
                LocalDateTime.ofInstant(tx.getEventTime(), ZoneOffset.UTC);
        int hour = local.getHour();

        if (hour < props.getStartHour() || hour > props.getEndHour()) {
            return;
        }

        BigDecimal minAmount = BigDecimal.valueOf(props.getMinAmount());

        if (tx.getAmount().compareTo(minAmount) >= 0) {
            result.add(props.getScore(), "NIGHT_TX");
        }
    }
}
