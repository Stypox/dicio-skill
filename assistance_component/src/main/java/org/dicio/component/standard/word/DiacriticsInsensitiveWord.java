package org.dicio.component.standard.word;

import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

public final class DiacriticsInsensitiveWord extends StringWord {

    private static final Collator collator = buildCollator();

    private static Collator buildCollator() {
        final Collator collator = Collator.getInstance(Locale.ENGLISH);
        collator.setStrength(Collator.PRIMARY);
        // note: this is not FULL_COMPOSITION, some accented characters could not be considered the same
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
        return collator;
    }

    public static byte[] getCollationKey(final String inputWord) {
        return collator.getCollationKey(inputWord).toByteArray();
    }


    private final byte[] valueCollationKey;

    /**
     * A word in a sentence with the indices of all possible subsequent words. When matching,
     * diacritics and accents will be checked (see e.g. CTRL+F -> Match Diacritics in Firefox). For
     * diacritics insensitive matching see {@link DiacriticsInsensitiveWord}.
     *
     * @param valueCollationKey a byte array containing the pre-calculated collation key of the
     *                          value for this word, built under the {@link Locale#ENGLISH} locale
     * @param minimumSkippedWordsToEnd the minimum number of subsequent words that have to be
     *                                 skipped to reach the end of the sentence. Used in case the
     *                                 end of input is reached on this word. Capturing groups count
     *                                 as if two words were skipped.
     * @param nextIndices the indices of all possible subsequent words in the owning sentence; it
     *                    must always contain a value; use the length of the word array to represent
     */
    public DiacriticsInsensitiveWord(final byte[] valueCollationKey,
                                     final int minimumSkippedWordsToEnd,
                                     final int... nextIndices) {
        super(minimumSkippedWordsToEnd, nextIndices);
        this.valueCollationKey = valueCollationKey;
    }

    @Override
    public boolean matches(final String inputWord, final byte[] inputWordCollationKey) {
        return Arrays.equals(valueCollationKey, inputWordCollationKey);
    }
}
