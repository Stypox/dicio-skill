package com.dicio.output.views;

public class Description implements BaseView {
    private final String text;

    /**
     * Constructs a description. The displayed text is smaller
     * than the one in {@link Header}.
     * @param htmlText text to display, basic html is allowed
     */
    Description(String htmlText) {
        this.text = htmlText;
    }

    /**
     * @return html text to be displayed
     */
    public String getText() {
        return text;
    }
}
