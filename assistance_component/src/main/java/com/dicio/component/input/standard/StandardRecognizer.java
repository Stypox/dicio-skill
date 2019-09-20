package com.dicio.component.input.standard;

import com.dicio.component.input.InputRecognizer;

import java.util.ArrayList;
import java.util.List;

public class StandardRecognizer implements InputRecognizer {
    private StandardRecognizerData data;
    private List<String> input;
    private Sentence bestSentenceSoFar;


    /////////////////
    // Constructor //
    /////////////////

    public StandardRecognizer(StandardRecognizerData data) {
        this.data = data;
        this.input = new ArrayList<>();
    }

    public StandardRecognizer(InputRecognizer.Specificity specificity, Sentence[] sentences) {
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
    public void setInput(List<String> words) {
        this.input = words;
    }

    @Override
    public List<String> getInput() {
        return this.input;
    }

    @Override
    public float score() {
        float maxScoreSoFar = 0;

        for (Sentence sentence : data.getSentences()) {
            float currentScore = sentence.score(this.input);
            if (currentScore > maxScoreSoFar) {
                maxScoreSoFar = currentScore;
                bestSentenceSoFar = sentence;
            }
        }

        return maxScoreSoFar;
    }


    ////////////////////////////////////
    // Data of the last best sentence //
    ////////////////////////////////////

    public String getSentenceId() {
        return bestSentenceSoFar.getSentenceId();
    }

    public int getNrCapturingGroups() {
        return bestSentenceSoFar.getNrCapturingGroups();
    }

    public List<String> getCapturingGroup(int index) {
        return bestSentenceSoFar.getCapturingGroup(index);
    }
}
