package org.dicio.skill.standard;

import org.dicio.skill.InputRecognizer;

import java.util.Collections;
import java.util.List;

public class StandardRecognizer implements InputRecognizer<StandardResult> {
    private final StandardRecognizerData data;
    private String input;
    private List<String> inputWords;
    private List<String> normalizedInputWords;

    private PartialScoreResult bestResultSoFar;
    private String bestSentenceIdSoFar;


    /////////////////
    // Constructor //
    /////////////////

    public StandardRecognizer(final StandardRecognizerData data) {
        this.data = data;
        this.inputWords = Collections.emptyList();
        this.normalizedInputWords = Collections.emptyList();
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
    public void setInput(final String input,
                         final List<String> inputWords,
                         final List<String> normalizedInputWords) {

        this.input = input;
        this.inputWords = inputWords;
        this.normalizedInputWords = normalizedInputWords;
    }

    @Override
    public float score() {
        bestResultSoFar = data.getSentences()[0].score(inputWords, normalizedInputWords);
        bestSentenceIdSoFar = data.getSentences()[0].getSentenceId();
        for (int i = 1; i < data.getSentences().length; ++i) {
            final PartialScoreResult result =
                    data.getSentences()[i].score(inputWords, normalizedInputWords);
            if (result.value(inputWords.size()) > bestResultSoFar.value(inputWords.size())) {
                bestResultSoFar = result;
                bestSentenceIdSoFar = data.getSentences()[i].getSentenceId();
            }
        }

        return bestResultSoFar.value(inputWords.size());
    }

    @Override
    public StandardResult getResult() {
        return bestResultSoFar.toStandardResult(bestSentenceIdSoFar, input);
    }

    @Override
    public void cleanup() {
        input = null;
        inputWords = null;
        normalizedInputWords = null;
        bestResultSoFar = null;
        bestSentenceIdSoFar = null;
    }
}