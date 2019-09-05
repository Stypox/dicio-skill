package com.dicio.component;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.OutputGenerator;
import com.dicio.component.output.views.BaseView;

import java.util.List;
import java.util.Optional;

public class TieInputOutput<IR extends InputRecognizer> implements AssistanceComponent {
    private IR inputRecognizer;
    private OutputGenerator<IR> outputGenerator;

    TieInputOutput(IR inputRecognizer, OutputGenerator<IR> outputGenerator) {
        this.inputRecognizer = inputRecognizer;
        this.outputGenerator = outputGenerator;
    }


    @Override
    public InputRecognizer.Specificity specificity() {
        return inputRecognizer.specificity();
    }

    @Override
    public void setInput(List<String> words) {
        inputRecognizer.setInput(words);
    }

    @Override
    public List<String> getInput() {
        return inputRecognizer.getInput();
    }

    @Override
    public float score() {
        return inputRecognizer.score();
    }


    @Override
    public void calculateOutput() throws Throwable {
        outputGenerator.calculateOutput(inputRecognizer);
    }

    @Override
    public List<BaseView> getGraphicalOutput() {
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
