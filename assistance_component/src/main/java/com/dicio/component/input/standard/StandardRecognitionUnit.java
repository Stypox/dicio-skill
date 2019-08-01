package com.dicio.component.input.standard;

import com.dicio.component.input.InputRecognitionUnit;

import java.util.ArrayList;
import java.util.List;

public class StandardRecognitionUnit implements InputRecognitionUnit {
    private final Specificity specificity_;
    private List<String> input;

    private Sentence[] sentences;
    private Sentence bestSentenceSoFar;


    /////////////////
    // Constructor //
    /////////////////

    public StandardRecognitionUnit(Specificity specificity, Sentence[] sentences) {
        this.specificity_ = specificity;
        this.input = new ArrayList<>();
        this.sentences = sentences;
    }


    ////////////////////////////////////
    // InputRecognitionUnit overrides //
    ////////////////////////////////////

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
