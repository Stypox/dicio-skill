package com.dicio.input_recognition.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sentence {
    private static final float penaltyMissingCapturingGroup = 0.25f;

    private final ArrayList<ArrayList<String>> packs;
    private ArrayList<ArrayList<String>> capturingGroups = new ArrayList<>();

    //////////////////
    // Constructors //
    //////////////////

    public Sentence(final String[] firstPack, final String[] secondPack, final String[] thirdPack) {
        packs = new ArrayList<ArrayList<String>>() {{
                add(new ArrayList<String>(Arrays.asList(firstPack)));
                add(new ArrayList<String>(Arrays.asList(secondPack)));
                add(new ArrayList<String>(Arrays.asList(thirdPack)));
        }};
    }

    public Sentence(final String[] firstPack, final String[] secondPack) {
        packs = new ArrayList<ArrayList<String>>() {{
            add(new ArrayList<String>(Arrays.asList(firstPack)));
            add(new ArrayList<String>(Arrays.asList(secondPack)));
        }};
    }

    public Sentence(final String[] firstPack) {
        packs = new ArrayList<ArrayList<String>>() {{
            add(new ArrayList<String>(Arrays.asList(firstPack)));
        }};
    }


    ///////////
    // Score //
    ///////////

    static PartialScoreResult scoreFromLeft(List<String> expected, List<String> actual) {
        int nrFoundWords = 0;
        final int expectedSize = expected.size(), actualSize = actual.size();
        int expectedIdx = 0, actualIdx = 0;

        for(; expectedIdx != expectedSize && actualIdx != actualSize; ++expectedIdx) {
            int foundWordOffset = actual.subList(actualIdx, actualSize).indexOf(expected.get(expectedIdx));
            
            if (foundWordOffset != -1) {
                ++nrFoundWords;
                actualIdx += foundWordOffset + 1;
            }
        }

        return new PartialScoreResult(nrFoundWords, expectedSize, 0, actualIdx);
    }

    static PartialScoreResult scoreFromRight(List<String> expected, List<String> actual) {
        int nrFoundWords = 0;
        final int expectedSize = expected.size(), actualSize = actual.size();
        int expectedIdx = expectedSize-1, actualIdx = actualSize-1;

        for(; expectedIdx != -1 && actualIdx != -1; --expectedIdx) {
            int foundWordIdx = actual.subList(0, actualIdx+1).lastIndexOf(expected.get(expectedIdx));

            if (foundWordIdx != -1) {
                ++nrFoundWords;
                actualIdx = foundWordIdx - 1;
            }
        }

        return new PartialScoreResult(nrFoundWords, expectedSize, actualIdx+1, actualSize-actualIdx-1);
    }


    static int centralIndexOf(List<String> words, String word) {
        final int wordsSize = words.size(), center = wordsSize/2;

        int idxFoundLeft = words.subList(0, center).lastIndexOf(word),
                idxFoundRight = words.subList(center, wordsSize).indexOf(word);

        if (idxFoundRight == -1) {
            return idxFoundLeft;
        } else {
            idxFoundRight += center;
        }
        if (idxFoundLeft == -1) {
            return idxFoundRight;
        }

        if ((center - idxFoundLeft) < (idxFoundRight - center)) {
            return idxFoundLeft;
        } else {
            return idxFoundRight;
        }
    }

    static PartialScoreResult scoreCenter(List<String> expected, List<String> actual) {
        final int expectedSize = expected.size(), actualSize = actual.size();
        int expectedToLeftIdx = (expectedSize-1)/2, expectedToRightIdx = expectedToLeftIdx+1;
        int actualCentralIdx = -1;

        while (expectedToLeftIdx >= 0) {
            actualCentralIdx = centralIndexOf(actual, expected.get(expectedToLeftIdx));
            --expectedToLeftIdx;
            if (actualCentralIdx != -1) {
                break;
            }

            if (expectedToRightIdx < expectedSize) {
                actualCentralIdx = centralIndexOf(actual, expected.get(expectedToRightIdx));
                ++expectedToRightIdx;
                if (actualCentralIdx != -1) {
                    break;
                }
            }
        }

        if (actualCentralIdx == -1) {
            return new PartialScoreResult(0, expectedSize, (actualSize+1)/2, 0);
        }


        PartialScoreResult scoreLeft = scoreFromRight(expected.subList(0, expectedToLeftIdx+1), actual.subList(0, actualCentralIdx));
        PartialScoreResult scoreRight = scoreFromLeft(expected.subList(expectedToRightIdx-1, expectedSize), actual.subList(actualCentralIdx+1, actualSize));

        return new PartialScoreResult(
                scoreLeft.nrFoundWords + 1 + scoreRight.nrFoundWords, expectedSize,
                scoreLeft.usedWordsOffset, scoreLeft.usedWordsSize + 1 + scoreRight.usedWordsSize);
    }


    static float penalizeIfMissingCapturingGroup(boolean missingCapturingGroup, float x) {
        if (missingCapturingGroup) {
            return Math.max(0f, x - penaltyMissingCapturingGroup);
        } else {
            return x;
        }
    }

    float score(List<String> words) {
        final int actualSize = words.size();
        capturingGroups.clear();
        float score = 0;

        switch (packs.size()) {
            case 1:
                score = scoreFromLeft(packs.get(0), words).score(actualSize);
                break;

            case 2:
                if (packs.get(0).isEmpty()) {
                    PartialScoreResult scoreRight = scoreFromRight(packs.get(1), words);

                    capturingGroups.add(new ArrayList<>(words.subList(0, scoreRight.firstUsedIdx())));
                    score = scoreRight.score();
                } else if (packs.get(1).isEmpty()) {
                    PartialScoreResult scoreLeft = scoreFromLeft(packs.get(0), words);

                    capturingGroups.add(new ArrayList<>(words.subList(scoreLeft.onePastLastUsedIdx(), actualSize)));
                    score = scoreLeft.score();
                } else {
                    PartialScoreResult scoreLeft = scoreFromLeft(packs.get(0), words);
                    PartialScoreResult scoreRight = scoreFromRight(packs.get(1), words.subList(scoreLeft.onePastLastUsedIdx(), actualSize));
                    scoreRight.usedWordsOffset += scoreLeft.onePastLastUsedIdx();

                    capturingGroups.add(new ArrayList<>(words.subList(scoreLeft.onePastLastUsedIdx(), scoreRight.firstUsedIdx())));
                    score = (scoreLeft.score() + scoreRight.score()) / 2;
                }

                score = penalizeIfMissingCapturingGroup(capturingGroups.get(0).isEmpty(), score);
                break;

            case 3:
                if (packs.get(0).isEmpty() && packs.get(2).isEmpty()) {
                    PartialScoreResult scoreCenter = scoreCenter(packs.get(1), words);

                    capturingGroups.add(new ArrayList<>(words.subList(0, scoreCenter.firstUsedIdx())));
                    capturingGroups.add(new ArrayList<>(words.subList(scoreCenter.onePastLastUsedIdx(), actualSize)));
                    score = scoreCenter.score();

                } else if (packs.get(0).isEmpty()) {
                    PartialScoreResult scoreRight = scoreFromRight(packs.get(2), words);
                    PartialScoreResult scoreCenter = scoreCenter(packs.get(1), words.subList(0, scoreRight.firstUsedIdx()));

                    capturingGroups.add(new ArrayList<>(words.subList(0, scoreCenter.firstUsedIdx())));
                    capturingGroups.add(new ArrayList<>(words.subList(scoreCenter.onePastLastUsedIdx(), scoreRight.firstUsedIdx())));
                    score = (scoreRight.score() + scoreCenter.score()) / 2;

                } else if (packs.get(2).isEmpty()) {
                    PartialScoreResult scoreLeft = scoreFromLeft(packs.get(0), words);
                    PartialScoreResult scoreCenter = scoreCenter(packs.get(1), words.subList(scoreLeft.onePastLastUsedIdx(), actualSize));
                    scoreCenter.usedWordsOffset += scoreLeft.onePastLastUsedIdx();

                    capturingGroups.add(new ArrayList<>(words.subList(scoreLeft.onePastLastUsedIdx(), scoreCenter.firstUsedIdx())));
                    capturingGroups.add(new ArrayList<>(words.subList(scoreCenter.onePastLastUsedIdx(), actualSize)));
                    score = (scoreLeft.score() + scoreCenter.score()) / 2;

                } else {
                    PartialScoreResult scoreLeft = scoreFromLeft(packs.get(0), words);
                    PartialScoreResult scoreRight = scoreFromRight(packs.get(2), words.subList(scoreLeft.onePastLastUsedIdx(), actualSize));
                    scoreRight.usedWordsOffset += scoreLeft.onePastLastUsedIdx();
                    PartialScoreResult scoreCenter = scoreCenter(packs.get(1), words.subList(scoreLeft.onePastLastUsedIdx(), scoreRight.firstUsedIdx()));
                    scoreCenter.usedWordsOffset += scoreLeft.onePastLastUsedIdx();

                    capturingGroups.add(new ArrayList<>(words.subList(scoreLeft.onePastLastUsedIdx(), scoreCenter.firstUsedIdx())));
                    capturingGroups.add(new ArrayList<>(words.subList(scoreCenter.onePastLastUsedIdx(), scoreRight.firstUsedIdx())));
                    score = (scoreLeft.score() + scoreCenter.score() + scoreRight.score()) / 3;

                }

                score = penalizeIfMissingCapturingGroup(capturingGroups.get(0).isEmpty(), score);
                score = penalizeIfMissingCapturingGroup(capturingGroups.get(1).isEmpty(), score);
                break;
        }

        return score;
    }


    //////////
    // Data //
    //////////

    int nrCapturingGroups() {
        return packs.size() - 1;
    }

    ArrayList<String> getCapturingGroup(int i) {
        return capturingGroups.get(i);
    }
}
