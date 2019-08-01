package com.dicio.component.output.views;

import org.junit.Test;

import static org.junit.Assert.*;

public class DescriptionTest {

    @Test
    public void testConstructorAndGetters() {
        final String text = "desc";
        Description description = new Description(text);
        assertEquals(text, description.getText());
    }
}