package com.dicio.component.output.views;

public class Description implements BaseView {
    private final String text;
    private final boolean isHtmlEnabled;

    /**
     * Constructs a description. The displayed text is smaller
     * than the one in {@link Header}.
     * @param text text to display, basic html is enabled if
     * {@code isHtmlEnabled} is set to {@code true}
     * @param isHtmlEnabled if set to {@code true}, parse the
     * provided {@code text} as html, otherwise as plain text
     */
    public Description(String text, boolean isHtmlEnabled) {
        this.text = text;
        this.isHtmlEnabled = isHtmlEnabled;
    }

    /**
     * @return text to be displayed
     */
    public String getText() {
        return text;
    }

    /**
     * @return {@code true} if the text should be treated as
     * plain text, {@code false} otherwise.
     */
    public boolean getIsHtmlEnabled() {
        return isHtmlEnabled;
    }
}
