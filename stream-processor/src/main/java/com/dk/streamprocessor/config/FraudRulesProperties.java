package com.dk.streamprocessor.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fraud.rules")
public class FraudRulesProperties {

    private Decision decision = new Decision();
    private HighAmount highAmount = new HighAmount();
    private CardNotPresent cardNotPresent = new CardNotPresent();
    private NightHighAmount nightHighAmount = new NightHighAmount();
    private Velocity velocity = new Velocity();

    public Decision getDecision() { return decision; }
    public void setDecision(Decision decision) { this.decision = decision; }

    public HighAmount getHighAmount() { return highAmount; }
    public void setHighAmount(HighAmount highAmount) { this.highAmount = highAmount; }

    public CardNotPresent getCardNotPresent() { return cardNotPresent; }
    public void setCardNotPresent(CardNotPresent cardNotPresent) { this.cardNotPresent = cardNotPresent; }

    public NightHighAmount getNightHighAmount() { return nightHighAmount; }
    public void setNightHighAmount(NightHighAmount nightHighAmount) { this.nightHighAmount = nightHighAmount; }

    public Velocity getVelocity() { return velocity; }
    public void setVelocity(Velocity velocity) { this.velocity = velocity; }

    // === Nested classes ===

    public static class Decision {
        private int reviewMinScore = 30;
        private int declineMinScore = 60;

        public int getReviewMinScore() { return reviewMinScore; }
        public void setReviewMinScore(int reviewMinScore) { this.reviewMinScore = reviewMinScore; }

        public int getDeclineMinScore() { return declineMinScore; }
        public void setDeclineMinScore(int declineMinScore) { this.declineMinScore = declineMinScore; }
    }

    public static class HighAmount {
        private boolean enabled = true;
        private int threshold = 1000;
        private int score = 40;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getThreshold() { return threshold; }
        public void setThreshold(int threshold) { this.threshold = threshold; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
    }

    public static class CardNotPresent {
        private boolean enabled = true;
        private int threshold = 500;
        private int score = 25;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getThreshold() { return threshold; }
        public void setThreshold(int threshold) { this.threshold = threshold; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
    }

    public static class NightHighAmount {
        private boolean enabled = true;
        private int minAmount = 200;
        private int startHour = 0;
        private int endHour = 5;
        private int score = 15;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getMinAmount() { return minAmount; }
        public void setMinAmount(int minAmount) { this.minAmount = minAmount; }

        public int getStartHour() { return startHour; }
        public void setStartHour(int startHour) { this.startHour = startHour; }

        public int getEndHour() { return endHour; }
        public void setEndHour(int endHour) { this.endHour = endHour; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
    }

    public static class Velocity {
        private boolean enabled = true;
        private int windowMinutes = 5;
        private int maxCount = 3;
        private int score = 30;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getWindowMinutes() { return windowMinutes; }
        public void setWindowMinutes(int windowMinutes) { this.windowMinutes = windowMinutes; }

        public int getMaxCount() { return maxCount; }
        public void setMaxCount(int maxCount) { this.maxCount = maxCount; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
    }
}
