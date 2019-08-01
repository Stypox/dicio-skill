package com.dicio.component.output;

import com.dicio.component.input.InputRecognitionUnit;
import com.dicio.component.output.views.ViewList;

import org.junit.Test;

import static org.junit.Assert.*;

public class OutputGenerationUnitTest {
    @Test
    public void testDefaultFunctions() {
        OutputGenerationUnit ogu = new OutputGenerationUnit() {
            public void calculateOutput(InputRecognitionUnit inputRecognitionUnit) { /* ignore */ }
            public ViewList getGraphicalOutput() { /* ignore */ return null; }
            public String getSpeechOutput() { /* ignore */ return null; }
        };

        assertFalse(ogu.nextOutputGenerator().isPresent());
        assertFalse(ogu.nextAssistanceComponents().isPresent());
    }
}