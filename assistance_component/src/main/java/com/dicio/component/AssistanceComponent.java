package com.dicio.component;

import com.dicio.component.output.OutputGenerationUnit;
import com.dicio.component.input.InputRecognitionUnit;
import com.dicio.component.output.views.ViewList;

import java.util.List;
import java.util.Optional;

public interface AssistanceComponent {
    /**
     * @see InputRecognitionUnit#specificity()
     */
    InputRecognitionUnit.Specificity specificity();

    /**
     * @see InputRecognitionUnit#setInput(List<String>)
     */
    void setInput(List<String> words);

    /**
     * @see InputRecognitionUnit#getInput()
     */
    List<String> getInput();

    /**
     * @see InputRecognitionUnit#score()
     */
    float score();


    /**
     * @see OutputGenerationUnit#calculateOutput(InputRecognitionUnit)
     */
    void calculateOutput();

    /**
     * @see OutputGenerationUnit#getGraphicalOutput()
     */
    ViewList getGraphicalOutput();

    /**
     * @see OutputGenerationUnit#getSpeechOutput()
     */
    String getSpeechOutput();

    /**
     * @see OutputGenerationUnit#nextOutputGenerator()
     */
    Optional<OutputGenerationUnit> nextOutputGenerator();

    /**
     * @see OutputGenerationUnit#nextAssistanceComponents()
     */
    Optional<List<AssistanceComponent>> nextAssistanceComponents();
}
