package com.dicio.component.output.views;

public class Header implements BaseView {
    private final String text;

    /**
     * Constructs a header. It is useful as a title, since the
     * displayed text is bigger than the one in {@link Description}.
     * @param plainText text to display, html is not allowed
     */
    public Header(String plainText) {
        this.text = plainText;
    }

    /**
     * @return plain text to be displayed
     */
    public String getText() {
        return text;
    }
}
