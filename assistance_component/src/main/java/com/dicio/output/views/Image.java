package com.dicio.output.views;

public class Image implements BaseView {
    enum SourceType {
        /**
         * The provided source is a url
         */
        url,
        
        /**
         * The provided source is a local file
         */
        local,
        
        /**
         * The provided source is neither a url nor a local file,
         * but probably a pointer to a platform-dependent resource
         */
        other,
    }
    
    interface OnClickListener {
        void onClick(String imageSource, SourceType sourceType);
    }

    private final String imageSource;
    private final SourceType sourceType;
    private OnClickListener onClickListener;


    /**
     * Constructs an image to be loaded from the provided source
     *
     * @see SourceType
     * @param imageSource a string representing the source of the image
     * @param sourceType the type of the image source
     */
    Image(String imageSource, SourceType sourceType) {
        this.imageSource = imageSource;
        this.sourceType = sourceType;
        this.onClickListener = null;
    }


    /**
     * @return a string representing the source of the image to be loaded
     */
    public String getSource() {
        return imageSource;
    }

    /**
     * @see SourceType
     * @return type of the image source
     */
    public SourceType getSourceType() {
        return sourceType;
    }


    /**
     * Set the click listener for the image.
     *
     * @see OnClickListener
     * @param onClickListener lambda to be called on click
     */
    void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Call the click listener for the image.
     *
     * @see OnClickListener
     * @see #setOnClickListener(OnClickListener)
     */
    public void onClick() {
        if (onClickListener != null) {
            onClickListener.onClick(imageSource, sourceType);
        }
    }
}
