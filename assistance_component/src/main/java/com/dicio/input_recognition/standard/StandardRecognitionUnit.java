package com.dicio.input_recognition.standard;

import com.dicio.input_recognition.InputRecognitionUnit;

import java.util.ArrayList;

public class StandardRecognitionUnit implements InputRecognitionUnit {
    private final Specificity specificity_;
    private ArrayList<String> input;

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
    public void setInput(ArrayList<String> words) {
        this.input = words;
    }

    @Override
    public ArrayList<String> getInput() {
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

    public ArrayList<String> getCapturingGroup(int index) {
        return bestSentenceSoFar.getCapturingGroup(index);
    }
}
