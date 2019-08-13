package com.dicio.component.output.views;

public class DescribedImage implements BaseView {
    public interface OnClickListener {
        void onClick(String imageSource, Image.SourceType imageSourceType, String headerText, String descriptionText);
    }

    private final String imageSource;
    private final Image.SourceType imageSourceType;
    private final String headerText;
    private final String descriptionText;
    private OnClickListener onClickListener;


    /**
     * Constructs a composite view made of an image with header and description.
     *
     * @see Image.SourceType
     * @param imageSource a string representing the source of the image
     * @param imageSourceType the type of the image source
     * @param headerText text to display in the header, html is not allowed
     * @param descriptionText text to display in the description, html is allowed
     */
    public DescribedImage(String imageSource, Image.SourceType imageSourceType, String headerText, String descriptionText) {
        this.imageSource = imageSource;
        this.imageSourceType = imageSourceType;
        this.headerText = headerText;
        this.descriptionText = descriptionText;
        this.onClickListener = null;
    }


    /**
     * @return a string representing the source of the image to be displayed
     */
    public String getImageSource() {
        return imageSource;
    }

    /**
     * @return the type of the image source
     */
    public Image.SourceType getImageSourceType() {
        return imageSourceType;
    }

    /**
     * @return plain text to be displayed in the header
     */
    public String getHeaderText() {
        return headerText;
    }

    /**
     * @return html text to be displayed in the description
     */
    public String getDescriptionText() {
        return descriptionText;
    }


    /**
     * Set the click listener for the whole described image.
     *
     * @see OnClickListener
     * @param onClickListener lambda to be called on click
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Call the click listener for the whole described image.
     *
     * @see Image.OnClickListener
     * @see #setOnClickListener(OnClickListener)
     */
    public void onClick() {
        if (onClickListener != null) {
            onClickListener.onClick(imageSource, imageSourceType, headerText, descriptionText);
        }
    }
}
