package com.dicio.component.output.views;

/**
 * It does not implement {@link BaseView} to prevent list inception.
 * <!-- TODO is this ok? -->
 */
public class ViewList {
    private final BaseView[] views;

    /**
     * Constructs a list of views. A list of views cannot
     * contain another list of views.
     * @param views the entries of the list in order
     */
    public ViewList(final BaseView... views) {
        this.views = views;
    }

    /**
     * Get the entries of the list in the order
     * they were provided to the constructor.
     * @return all list entries
     */
    public BaseView[] getViews() {
        return views;
    }
}
