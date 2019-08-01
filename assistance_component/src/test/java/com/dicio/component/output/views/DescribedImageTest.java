package com.dicio.component.output.views;

import org.junit.Test;

import static org.junit.Assert.*;

public class DescribedImageTest {
    private int nrClickListenerCalled;

    @Test
    public void testOnClickListeners() {
        final String source = "a.b";
        final Image.SourceType type = Image.SourceType.url;
        final String header = "header";
        final String description = "desc";

        DescribedImage di = new DescribedImage(source, type, header, description);

        nrClickListenerCalled = 0;
        di.setOnClickListener(new DescribedImage.OnClickListener() {
            @Override
            public void onClick(String imageSource, Image.SourceType sourceType, String headerText, String descriptionText) {
                assertEquals(source, imageSource);
                assertEquals(type, sourceType);
                assertEquals(header, headerText);
                assertEquals(description, descriptionText);
                ++nrClickListenerCalled;
            }
        });

        di.onClick();
        assertEquals(1, nrClickListenerCalled);
    }

    @Test
    public void testConstructorAndGetters() {
        final String source = "a,b";
        final Image.SourceType type = Image.SourceType.other;
        final String header = "header";
        final String description = "desc";

        DescribedImage di = new DescribedImage(source, type, header, description);

        assertEquals(source, di.getImageSource());
        assertEquals(type, di.getImageSourceType());
        assertEquals(header, di.getHeaderText());
        assertEquals(description, di.getDescriptionText());
    }
}