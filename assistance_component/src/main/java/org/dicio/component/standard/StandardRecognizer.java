package org.dicio.component.standard;

import org.dicio.component.InputRecognizer;

import java.util.Collections;
import java.util.List;

public class StandardRecognizer implements InputRecognizer<StandardResult> {
    private final StandardRecognizerData data;
    private String input;
    private List<String> inputWords;
    private List<byte[]> inputWordCollationKeys;

    private PartialScoreResult bestResultSoFar;
    private String bestSentenceIdSoFar;


    /////////////////
    // Constructor //
    /////////////////

    public StandardRecognizer(final StandardRecognizerData data) {
        this.data = data;
        this.inputWords = Collections.emptyList();
        this.inputWordCollationKeys = Collections.emptyList();
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
                         final List<byte[]> inputWordCollationKeys) {

        this.input = input;
        this.inputWords = inputWords;
        this.inputWordCollationKeys = inputWordCollationKeys;
    }

    @Override
    public float score() {
        bestResultSoFar = data.getSentences()[0].score(inputWords, inputWordCollationKeys);
        bestSentenceIdSoFar = data.getSentences()[0].getSentenceId();
        for (int i = 1; i < data.getSentences().length; ++i) {
            final PartialScoreResult result =
                    data.getSentences()[i].score(inputWords, inputWordCollationKeys);
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
        inputWordCollationKeys = null;
        bestResultSoFar = null;
        bestSentenceIdSoFar = null;
    }
}
