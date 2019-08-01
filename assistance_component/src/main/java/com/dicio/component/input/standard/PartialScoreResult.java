package com.dicio.component.input.standard;

class PartialScoreResult {
    final int nrFoundWords, nrExpectedWords;
    int usedWordsOffset;
    final int usedWordsSize;

    PartialScoreResult(int nrFoundWords, int nrExpectedWords, int usedWordsOffset, int usedWordsSize) {
        this.nrFoundWords = nrFoundWords;
        this.nrExpectedWords = nrExpectedWords;
        this.usedWordsOffset = usedWordsOffset;
        this.usedWordsSize = usedWordsSize;
    }

    int firstUsedIdx() {
        return usedWordsOffset;
    }
    int onePastLastUsedIdx() {
        return usedWordsOffset + usedWordsSize;
    }

    static float dropAt0point75(float x) {
        // similar to a sigmoid; it has low values in range [0,0.75) and high values otherwise
        return (171f * (x-.65f)/(.2f+Math.abs(x-.75f)) + 117f) / 250f;
    }
    static float dropAt0point6(float x) {
        // similar to a sigmoid; it has low values in range [0,0.6) and high values otherwise
        return (28 * (x-.55f)/(.15f+Math.abs(x-.55f)) + 22f) / 43f;
    }

    float score(int nrActualWords) {
        if (nrFoundWords == 0) {
            return 0.0f; // it should be 0 even when nrActualWords == 0
        }

        float calculatedScore = dropAt0point75((float)nrFoundWords/nrExpectedWords) * dropAt0point6((float)nrFoundWords/nrActualWords);

        // eliminate floating point errors
        if (calculatedScore > 1.0f) {
            return 1.0f;
        } else if (calculatedScore < 0.0f) {
            return 0.0f;
        }
        return calculatedScore;
    }
    float score() {
        return score(usedWordsSize);
    }
}
