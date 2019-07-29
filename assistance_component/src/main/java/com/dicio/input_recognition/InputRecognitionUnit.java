package com.dicio.input_recognition;

import java.util.ArrayList;

public interface InputRecognitionUnit {
    enum Specificity {
        high,
        medium,
        low,
    }

    /**
     * The specificity of this input recognition unit
     * @return {@link Specificity#high high} for specific things (e.g. weather);<br>
     * {@link Specificity#medium medium} for not-too-specific things (e.g. calculator that parses numbers);<br>
     * {@link Specificity#low low} for broad things (e.g. omniscient API);<br>
     */
    Specificity specificity();

    /**
     * Sets the current input for the recognition unit,
     * to be used when {@link #score() score()} is called
     * @param words input to be recognized
     */
    void setInput(ArrayList<String> words);

    /**
     * @return the last input set with {@link #setInput(ArrayList) setInput()}
     */
    ArrayList<String> getInput();

    /**
     * The score of the last input set with {@link #setInput(ArrayList) setInput()}
     * for this input recognition unit
     * @return a number in range [0.0, 1.0]
     */
    float score();
}
