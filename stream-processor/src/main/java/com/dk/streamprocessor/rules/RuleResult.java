package com.dk.streamprocessor.rules;

import java.util.ArrayList;
import java.util.List;

public class RuleResult {

    private int scoreDelta;
    private final List<String> reasons = new ArrayList<>();

    public void add(int delta, String reason) {
        this.scoreDelta += delta;
        this.reasons.add(reason);
    }

    public int getScoreDelta() {
        return scoreDelta;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public String reasonsAsString() {
        return String.join(",", reasons);
    }
}