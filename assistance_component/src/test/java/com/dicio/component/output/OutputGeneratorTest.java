package com.dicio.component.output;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.views.ViewList;

import org.junit.Test;

import static org.junit.Assert.*;

public class OutputGeneratorTest {
    @Test
    public void testDefaultFunctions() {
        OutputGenerator og = new OutputGenerator() {
            public void calculateOutput(InputRecognizer inputRecognitionUnit) { /* ignore */ }
            public ViewList getGraphicalOutput() { /* ignore */ return null; }
            public String getSpeechOutput() { /* ignore */ return null; }
        };

        assertFalse(og.nextOutputGenerator().isPresent());
        assertFalse(og.nextAssistanceComponents().isPresent());
    }
}