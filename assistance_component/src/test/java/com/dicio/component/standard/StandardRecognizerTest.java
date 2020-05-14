package com.dicio.component.standard;

import com.dicio.component.InputRecognizer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StandardRecognizerTest {

    @Test
    public void testSpecificity() {
        StandardRecognizer sr = new StandardRecognizer(
                new StandardRecognizerData(InputRecognizer.Specificity.high, new Sentence[]{}));
        assertEquals(InputRecognizer.Specificity.high, sr.specificity());
    }
}