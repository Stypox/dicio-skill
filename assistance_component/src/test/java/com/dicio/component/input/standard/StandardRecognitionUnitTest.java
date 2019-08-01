package com.dicio.component.input.standard;

import com.dicio.component.input.InputRecognitionUnit;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StandardRecognitionUnitTest {

    @Test
    public void testSpecificity() {
        StandardRecognitionUnit siru = new StandardRecognitionUnit(InputRecognitionUnit.Specificity.high, new Sentence[]{});
        assertEquals(InputRecognitionUnit.Specificity.high, siru.specificity());
    }

    @Test
    public void testGetSetInput() {
        StandardRecognitionUnit siru = new StandardRecognitionUnit(InputRecognitionUnit.Specificity.low, new Sentence[]{});
        assertArrayEquals(new String[]{}, siru.getInput().toArray());
        assertEquals(InputRecognitionUnit.Specificity.low, siru.specificity());

        ArrayList<String> input = new ArrayList<String>(){{ add("a"); add("b"); }};
        siru.setInput(input);
        assertArrayEquals(input.toArray(), siru.getInput().toArray());
        assertEquals(InputRecognitionUnit.Specificity.low, siru.specificity());
    }
}