package com.dicio.component;

import java.util.List;

/**
 * Recognizes input by giving a score to it.
 */
public interface InputRecognizer<ResultType> {
    enum Specificity {
        high,
        medium,
        low,
    }

    /**
     * The specificity of this input recognizer
     * @return {@link Specificity#high high} for specific things (e.g. weather);<br>
     * {@link Specificity#medium medium} for not-too-specific things (e.g. calculator that parses numbers);<br>
     * {@link Specificity#low low} for broad things (e.g. omniscient API);<br>
     */
    Specificity specificity();

    /**
     * Sets the current input for the recognizer,
     * to be used when {@link #score() score()} is called
     * @param words input to be recognized
     */
    void setInput(List<String> words);

    /**
     * The score of the input previously set with {@link #setInput(List) setInput()}
     * for this input recognizer
     * @return a number in range [0.0, 1.0]
     */
    float score();

    /**
     * If this input recognizer has the highest score, this function is called to generate a result
     * based on the input previously set with {@link #setInput(List) setInput()}
     * @return a result useful for the next step of the computation
     */
    ResultType getResult();
}
