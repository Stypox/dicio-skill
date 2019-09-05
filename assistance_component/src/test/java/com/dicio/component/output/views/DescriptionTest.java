package com.dicio.component.output.views;

import org.junit.Test;

import static org.junit.Assert.*;

public class DescriptionTest {

    @Test
    public void testConstructorAndGetters() {
        final String text = "desc";
        final boolean html = true;

        Description description = new Description(text, html);
        assertEquals(text, description.getText());
        assertEquals(html, description.getIsHtmlEnabled());
    }
}