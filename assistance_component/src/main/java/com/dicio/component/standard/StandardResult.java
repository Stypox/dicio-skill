package com.dicio.component.standard;

import java.util.Map;

public class StandardResult {
    private final String sentenceId;
    private final Map<String, InputWordRange> capturingGroups;

    StandardResult(final String sentenceId, final Map<String, InputWordRange> capturingGroups) {
        this.sentenceId = sentenceId;
        this.capturingGroups = capturingGroups;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public Map<String, InputWordRange> getCapturingGroups() {
        return capturingGroups;
    }
}
