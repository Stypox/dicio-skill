package com.dicio.component.output;

import com.dicio.component.output.views.BaseView;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OutputGeneratorTest {
    @Test
    public void testDefaultFunctions() {
        OutputGenerator og = new OutputGenerator() {
            public void calculateOutput() { /* ignore */ }
            public List<BaseView> getGraphicalOutput() { /* ignore */ return null; }
            public String getSpeechOutput() { /* ignore */ return null; }
        };

        assertFalse(og.nextOutputGenerator().isPresent());
        assertFalse(og.nextAssistanceComponents().isPresent());
    }
}