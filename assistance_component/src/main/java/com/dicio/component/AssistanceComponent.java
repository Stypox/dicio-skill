package com.dicio.component;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.OutputGenerator;
import com.dicio.component.output.views.ViewList;

import java.util.List;
import java.util.Optional;

public interface AssistanceComponent {
    /**
     * @see InputRecognizer#specificity()
     */
    InputRecognizer.Specificity specificity();

    /**
     * @see InputRecognizer#setInput(List<String>)
     */
    void setInput(List<String> words);

    /**
     * @see InputRecognizer#getInput()
     */
    List<String> getInput();

    /**
     * @see InputRecognizer#score()
     */
    float score();


    /**
     * @see OutputGenerator#calculateOutput(InputRecognizer)
     */
    void calculateOutput();

    /**
     * @see OutputGenerator#getGraphicalOutput()
     */
    ViewList getGraphicalOutput();

    /**
     * @see OutputGenerator#getSpeechOutput()
     */
    String getSpeechOutput();

    /**
     * @see OutputGenerator#nextOutputGenerator()
     */
    Optional<OutputGenerator> nextOutputGenerator();

    /**
     * @see OutputGenerator#nextAssistanceComponents()
     */
    Optional<List<AssistanceComponent>> nextAssistanceComponents();
}
