package com.dicio.input_recognition.standard;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StandardRecognitionUnitTest {

    @Test
    public void testPriority() {
        StandardRecognitionUnit siru = new StandardRecognitionUnit(0.7f, new Sentence[]{});
        assertEquals(0.7f, siru.priority(), 0.0);
    }

    @Test
    public void testGetSetInput() {
        StandardRecognitionUnit siru = new StandardRecognitionUnit(0.0f, new Sentence[]{});
        assertArrayEquals(new String[]{}, siru.getInput().toArray());

        ArrayList<String> input = new ArrayList<String>(){{ add("a"); add("b"); }};
        siru.setInput(input);
        assertArrayEquals(input.toArray(), siru.getInput().toArray());
    }
}