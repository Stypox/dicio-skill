package com.dicio.input_recognition.standard;

import com.dicio.input_recognition.InputRecognitionUnit;

import java.util.ArrayList;

public class StandardRecognitionUnit implements InputRecognitionUnit {
    final float mPriority;
    ArrayList<String> input;

    Sentence[] sentences;
    Sentence bestSentenceSoFar;

    public StandardRecognitionUnit(float priority, Sentence[] sentences) {
        this.mPriority = priority;
        this.input = new ArrayList<>();
        this.sentences = sentences;
    }

    @Override
    public float priority() {
        return mPriority;
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
}
