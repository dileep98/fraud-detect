package com.dk.streamprocessor.rules;

import com.dk.streamprocessor.config.FraudRulesProperties;
import com.dk.streamprocessor.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CardNotPresentRule implements RiskRule{

    private final FraudRulesProperties.CardNotPresent props;

    public CardNotPresentRule(FraudRulesProperties fraudRulesProperties) {
        this.props = fraudRulesProperties.getCardNotPresent();
    }

    /**
     * @param tx
     * transaction entity has all the transaction data
     * @param result
     * to se the result to the RuleResult object
     */
    @Override
    public void apply(TransactionEntity tx, RuleResult result) {
        if (!props.isEnabled() || tx.getAmount() == null) {
            return;
        }

        if (!"CARD_NOT_PRESENT".equalsIgnoreCase(tx.getChannel())) {
            return;
        }

        BigDecimal threshold = BigDecimal.valueOf(props.getThreshold());

        if (tx.getAmount().compareTo(threshold) >= 0) {
            result.add(props.getScore(), "CARD_NOT_PRESENT_HIGH");
        }
    }
}
