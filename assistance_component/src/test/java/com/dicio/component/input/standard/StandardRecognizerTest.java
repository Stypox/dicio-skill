package com.dicio.component.input.standard;

import com.dicio.component.input.InputRecognizer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StandardRecognizerTest {

    @Test
    public void testSpecificity() {
        StandardRecognizer sr = new StandardRecognizer(new StandardRecognizerData(InputRecognizer.Specificity.high, new Sentence[]{}));
        assertEquals(InputRecognizer.Specificity.high, sr.specificity());
    }

    @Test
    public void testGetSetInput() {
        StandardRecognizer sr = new StandardRecognizer(InputRecognizer.Specificity.low, new Sentence[]{});
        assertArrayEquals(new String[]{}, sr.getInput().toArray());
        assertEquals(InputRecognizer.Specificity.low, sr.specificity());

        ArrayList<String> input = new ArrayList<String>(){{ add("a"); add("b"); }};
        sr.setInput(input);
        assertArrayEquals(input.toArray(), sr.getInput().toArray());
        assertEquals(InputRecognizer.Specificity.low, sr.specificity());
    }
}