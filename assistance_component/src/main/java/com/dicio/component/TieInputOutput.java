package com.dicio.component;

import com.dicio.component.input.InputRecognitionUnit;
import com.dicio.component.output.OutputGenerationUnit;
import com.dicio.component.output.views.ViewList;

import java.util.List;
import java.util.Optional;

public class TieInputOutput<IRU extends InputRecognitionUnit> implements AssistanceComponent {
    private IRU inputRecognitionUnit;
    private OutputGenerationUnit<IRU> outputGenerationUnit;

    TieInputOutput(IRU inputRecognitionUnit, OutputGenerationUnit<IRU> outputGenerationUnit) {
        this.inputRecognitionUnit = inputRecognitionUnit;
        this.outputGenerationUnit = outputGenerationUnit;
    }


    @Override
    public InputRecognitionUnit.Specificity specificity() {
        return inputRecognitionUnit.specificity();
    }

    @Override
    public void setInput(List<String> words) {
        inputRecognitionUnit.setInput(words);
    }

    @Override
    public List<String> getInput() {
        return inputRecognitionUnit.getInput();
    }

    @Override
    public float score() {
        return inputRecognitionUnit.score();
    }


    @Override
    public void calculateOutput() {
        outputGenerationUnit.calculateOutput(inputRecognitionUnit);
    }

    @Override
    public ViewList getGraphicalOutput() {
        return outputGenerationUnit.getGraphicalOutput();
    }

    @Override
    public String getSpeechOutput() {
        return outputGenerationUnit.getSpeechOutput();
    }

    @Override
    public Optional<OutputGenerationUnit> nextOutputGenerator() {
        return outputGenerationUnit.nextOutputGenerator();
    }

    @Override
    public Optional<List<AssistanceComponent>> nextAssistanceComponents() {
        return outputGenerationUnit.nextAssistanceComponents();
    }
}
