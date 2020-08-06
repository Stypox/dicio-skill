package org.dicio.component.util;

import org.dicio.component.standard.InputWordRange;
import org.dicio.component.standard.word.DiacriticsInsensitiveWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordExtractor {

    private WordExtractor() {
    }


    /**
     * Splits the input into words at every non-letter character, and making every word lowercase.
     * <br>
     * For example, "Hello, how Àrè you? " becomes {"hello", "how, "àrè", "you"}
     * @param input the input from which to extract words
     * @return the list of extracted words in order
     */
    public static List<String> extractWords(final String input) {
        // match all non-letter characters
        final String[] splitInput = input.split("[^\\p{L}]+");

        final List<String> inputWords = new ArrayList<>();
        for(final String word : splitInput) {
            final String normalized = normalizeWord(word);
            if (normalized != null) {
                inputWords.add(normalized);
            }
        }

        return inputWords;
    }

    /**
     * Builds the list of collation keys for the provided input words using
     * {@link DiacriticsInsensitiveWord#getCollationKey(String)}
     * @param inputWords the lowercase words to build the collation key list from
     * @return the collation keys in order
     */
    public static List<byte[]> getCollationKeys(final List<String> inputWords) {
        final List<byte[]> inputWordCollationKeys = new ArrayList<>(inputWords.size());
        for (final String inputWord : inputWords) {
            inputWordCollationKeys.add(DiacriticsInsensitiveWord.getCollationKey(inputWord));
        }
        return inputWordCollationKeys;
    }

    /**
     * Extracts a capturing group from the input containing the provided word range. Special
     * characters before and after the range are kept. The case and diacritics of letters are also
     * preserved.<br>
     * For example, extracting [1,3) from "a b, c; d " yields " b, c; " (note how spaces are also
     * kept).
     * @param input the original raw input from the user
     * @param range the range of words representing those captured in the capturing group
     * @return the content of the capturing group
     */
    public static String extractCapturingGroup(final String input, final InputWordRange range) {
        final Pattern pattern = Pattern.compile("^(?:[^\\p{L}]*\\p{L}+){" + range.from()
                + "}((?:[^\\p{L}]*\\p{L}+){" + (range.to() - range.from()) + "}[^\\p{L}]*)");
        final Matcher matcher = pattern.matcher(input);
        final boolean foundMatch = matcher.find();

        if (foundMatch) {
            return matcher.group(1);
        } else {
            return null; // unreachable, hopefully
        }
    }

    /**
     * @param word any string
     * @return the word to lowercase and null if the word is null or empty.
     */
    private static String normalizeWord(final String word) {
        if(word == null || word.isEmpty()) {
            return null;
        }

        return word.toLowerCase(Locale.ENGLISH);
    }
}
