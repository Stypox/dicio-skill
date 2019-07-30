package com.dicio.output.views;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageTest {
    private int nrClickListenerCalled;

    @Test
    public void testOnClickListeners() {
        final String source = "a/b";
        final Image.SourceType type = Image.SourceType.local;

        Image image = new Image(source, type);

        nrClickListenerCalled = 0;
        image.setOnClickListener(new Image.OnClickListener() {
            @Override
            public void onClick(String imageSource, Image.SourceType sourceType) {
                assertEquals(source, imageSource);
                assertEquals(type, sourceType);
                ++nrClickListenerCalled;
            }
        });

        image.onClick();
        assertEquals(1, nrClickListenerCalled);
    }

    @Test
    public void testConstructorAndGetters() {
        final String source = "a.b";
        final Image.SourceType type = Image.SourceType.url;

        Image image = new Image(source, type);

        assertEquals(source, image.getSource());
        assertEquals(type, image.getSourceType());
    }
}