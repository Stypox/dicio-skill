package com.dicio.component.standard;

import java.util.Map;

public class StandardResult {
    private final String sentenceId;
    private final String input;
    private final Map<String, InputWordRange> capturingGroups;

    StandardResult(final String sentenceId,
                   final String input,
                   final Map<String, InputWordRange> capturingGroups) {
        this.sentenceId = sentenceId;
        this.input = input;
        this.capturingGroups = capturingGroups;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public Map<String, InputWordRange> getCapturingGroups() {
        return capturingGroups;
    }
}
