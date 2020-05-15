package com.dicio.component.standard;

import java.util.List;

public class StandardResult {
    private final String sentenceId;
    private final List<List<String>> capturingGroups;

    StandardResult(String sentenceId, List<List<String>> capturingGroups) {
        this.sentenceId = sentenceId;
        this.capturingGroups = capturingGroups;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public List<List<String>> getCapturingGroups() {
        return capturingGroups;
    }
}
