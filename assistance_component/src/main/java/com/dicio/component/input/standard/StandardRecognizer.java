package com.dicio.component.input.standard;

import com.dicio.component.input.InputRecognizer;

import java.util.ArrayList;
import java.util.List;

public class StandardRecognizer implements InputRecognizer {
    private final Specificity specificity_;
    private List<String> input;

    private Sentence[] sentences;
    private Sentence bestSentenceSoFar;


    /////////////////
    // Constructor //
    /////////////////

    public StandardRecognizer(Specificity specificity, Sentence[] sentences) {
        this.specificity_ = specificity;
        this.input = new ArrayList<>();
        this.sentences = sentences;
    }


    ///////////////////////////////
    // InputRecognizer overrides //
    ///////////////////////////////

    @Override
    public Specificity specificity() {
        return this.specificity_;
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

        for (Sentence sentence : sentences) {
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
