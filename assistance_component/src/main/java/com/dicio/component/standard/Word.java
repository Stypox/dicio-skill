package com.dicio.component.standard;

public class Word {
    private final String value;
    private final boolean isCapturingGroup;
    private final int[] nextIndices;

    /**
     * A word in a sentence with the indices of all possible subsequent words
     * @param value the text value of the word, or the id of the capturing group
     * @param isCapturingGroup is this word a capturing group, with its id as value?
     * @param nextIndices the indices of all possible subsequent words in the owning sentence; it
     *                    must always contain a value; use the length of the word array to represent
     *                    that this can be the last word
     */
    public Word(final String value, final boolean isCapturingGroup, final int... nextIndices) {
        this.value = value;
        this.isCapturingGroup = isCapturingGroup;
        this.nextIndices = nextIndices;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean isCapturingGroup() {
        return isCapturingGroup;
    }

    public int[] getNextIndices() {
        return nextIndices;
    }
}
