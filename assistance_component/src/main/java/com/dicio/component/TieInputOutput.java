package com.dicio.component;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.OutputGenerator;
import com.dicio.component.output.views.ViewList;

import java.util.List;
import java.util.Optional;

public class TieInputOutput<IR extends InputRecognizer> implements AssistanceComponent {
    private IR inputRecognitionUnit;
    private OutputGenerator<IR> outputGenerator;

    TieInputOutput(IR inputRecognitionUnit, OutputGenerator<IR> outputGenerator) {
        this.inputRecognitionUnit = inputRecognitionUnit;
        this.outputGenerator = outputGenerator;
    }


    @Override
    public InputRecognizer.Specificity specificity() {
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
        outputGenerator.calculateOutput(inputRecognitionUnit);
    }

    @Override
    public ViewList getGraphicalOutput() {
        return outputGenerator.getGraphicalOutput();
    }

    @Override
    public String getSpeechOutput() {
        return outputGenerator.getSpeechOutput();
    }

    @Override
    public Optional<OutputGenerator> nextOutputGenerator() {
        return outputGenerator.nextOutputGenerator();
    }

    @Override
    public Optional<List<AssistanceComponent>> nextAssistanceComponents() {
        return outputGenerator.nextAssistanceComponents();
    }
}
