package org.dicio.component.standard;

import java.util.List;

public class Sentence {
    private final String sentenceId;
    private final int[] startingWordIndices;
    private final Word[] words;

    private List<String> inputWords;
    private PartialScoreResult[][][] memory;

    ////////////////////
    // PUBLIC METHODS //
    ////////////////////

    public Sentence(final String sentenceId, final int[] startingWordIndices, final Word... words) {
        this.sentenceId = sentenceId;
        this.startingWordIndices = startingWordIndices;
        this.words = words;
    }


    public PartialScoreResult score(final List<String> inputWords) {
        this.inputWords = inputWords;
        memory = new PartialScoreResult[words.length][inputWords.size()][2];
        final int inputWordCount = inputWords.size();

        PartialScoreResult bestResult = bestScore(startingWordIndices[0], 0, false);
        for (int i = 1; i != startingWordIndices.length; ++i) {
            bestResult = bestScore(startingWordIndices[i], 0, false)
                    .keepBest(bestResult, inputWordCount);
        }

        // cleanup to prevent memory leaks
        this.inputWords = null;
        memory = null;
        return bestResult;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    ///////////
    // SCORE //
    ///////////

    /**
     * Dynamic programming implementation along two dimensions
     * Complexity: O(numSentenceWords * numInputWords)
     */
    PartialScoreResult bestScore(int wordIndex,
                                 int inputWordIndex,
                                 boolean foundWordAfterStart) {

        if (wordIndex >= words.length) {
            return new PartialScoreResult(0,
                    inputWordIndex < inputWords.size() ? inputWords.size() - inputWordIndex : 0);
        } else if (inputWordIndex >= inputWords.size()) {
            return new PartialScoreResult(words[wordIndex].getMinimumSkippedWordsToEnd(), 0);
        }

        final int foundWordAfterStartInt = foundWordAfterStart ? 1 : 0;
        if (memory[wordIndex][inputWordIndex][foundWordAfterStartInt] != null) {
            // clone object to prevent edits
            return new PartialScoreResult(memory[wordIndex][inputWordIndex][foundWordAfterStartInt]);
        }

        PartialScoreResult result;
        if (words[wordIndex].isCapturingGroup()) {
            result = bestScoreCapturingGroup(wordIndex, inputWordIndex, foundWordAfterStart);
        } else {
            result = bestScoreNormalWord(wordIndex, inputWordIndex, foundWordAfterStart);
        }

        memory[wordIndex][inputWordIndex][foundWordAfterStartInt] = result;
        return new PartialScoreResult(result); // clone object to prevent edits
    }

    private PartialScoreResult bestScoreCapturingGroup(int wordIndex,
                                                       int inputWordIndex,
                                                       boolean foundWordAfterStart) {
        // do not use recursion here, to make checking whether anything was captured simpler
        // first try to skip capturing group
        PartialScoreResult result = bestScore(words[wordIndex].getNextIndices()[0],
                inputWordIndex, foundWordAfterStart)
                .skipCapturingGroup();
        for (int i = 1; i < words[wordIndex].getNextIndices().length; ++i) {
            result = bestScore(words[wordIndex].getNextIndices()[i],
                    inputWordIndex, foundWordAfterStart)
                    .skipCapturingGroup()
                    .keepBest(result, inputWords.size());
        }

        // then try various increasing lengths of capturing group
        for (int i = inputWords.size(); i > inputWordIndex; --i) {
            for (int nextIndex : words[wordIndex].getNextIndices()) {
                // keepBest will keep the current (i.e. latest) result in case of equality
                // so smaller capturing groups are preferred (leading to more specific sentences)
                result = bestScore(nextIndex, i, true)
                        .setCapturingGroup(words[wordIndex].toString(),
                                new InputWordRange(inputWordIndex, i))
                        .keepBest(result, inputWords.size());
            }
        }

        return result;
    }

    private PartialScoreResult bestScoreNormalWord(int wordIndex,
                                                   int inputWordIndex,
                                                   boolean foundWordAfterStart) {
        PartialScoreResult result = bestScore(wordIndex, inputWordIndex + 1, foundWordAfterStart)
                .skipInputWord(foundWordAfterStart);

        if (words[wordIndex].toString().equals(inputWords.get(inputWordIndex))) {
            for (int nextIndex : words[wordIndex].getNextIndices()) {
                result = bestScore(nextIndex, inputWordIndex + 1, true)
                        .matchWord()
                        .keepBest(result, inputWords.size());
            }

        } else {
            for (int nextIndex : words[wordIndex].getNextIndices()) {
                result = bestScore(nextIndex, inputWordIndex, foundWordAfterStart)
                        .skipWord()
                        .keepBest(result, inputWords.size());
            }
        }

        return result;
    }
}
