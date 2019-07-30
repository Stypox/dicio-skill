package com.dicio.output.views;

import org.junit.Test;

import static org.junit.Assert.*;

public class HeaderTest {

    @Test
    public void testConstructorAndGetters() {
        final String text = "header";
        Header header = new Header(text);
        assertEquals(text, header.getText());
    }
}