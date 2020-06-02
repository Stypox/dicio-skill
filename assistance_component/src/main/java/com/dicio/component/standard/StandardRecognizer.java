package com.dicio.component.standard;

import com.dicio.component.InputRecognizer;

import java.util.ArrayList;
import java.util.List;

public class StandardRecognizer implements InputRecognizer<StandardResult> {
    private final StandardRecognizerData data;
    private String input;
    private List<String> inputWords;
    private Sentence bestSentenceSoFar;


    /////////////////
    // Constructor //
    /////////////////

    public StandardRecognizer(final StandardRecognizerData data) {
        this.data = data;
        this.inputWords = new ArrayList<>();
    }

    public StandardRecognizer(final InputRecognizer.Specificity specificity,
                              final Sentence[] sentences) {
        this(new StandardRecognizerData(specificity, sentences));
    }


    ///////////////////////////////
    // InputRecognizer overrides //
    ///////////////////////////////

    @Override
    public Specificity specificity() {
        return data.getSpecificity();
    }

    @Override
    public void setInput(final String input, final List<String> inputWords) {
        this.input = input;
        this.inputWords = inputWords;
    }

    @Override
    public float score() {
        float maxScoreSoFar = 0;

        for (Sentence sentence : data.getSentences()) {
            final float currentScore = sentence.score(inputWords);
            if (currentScore > maxScoreSoFar) {
                maxScoreSoFar = currentScore;
                bestSentenceSoFar = sentence;
            }
        }

        return maxScoreSoFar;
    }

    @Override
    public StandardResult getResult() {
        return bestSentenceSoFar.toStandardResult(input);
    }
}
