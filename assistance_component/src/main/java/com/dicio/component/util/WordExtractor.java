package com.dicio.component.util;

import com.dicio.component.standard.InputWordRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordExtractor {

    private WordExtractor() {
    }

    public static List<String> extractWords(final String input) {
        // match all non-letter characters
        final String[] splitInput = input.split("[^\\p{L}]+");

        final List<String> inputWords = new ArrayList<>();
        for(String word : splitInput) {
            String normalized = normalizeWord(word);
            if (normalized != null) {
                inputWords.add(normalized);
            }
        }

        return inputWords;
    }

    public static String extractCapturingGroup(final String input, final InputWordRange range) {
        Pattern pattern = Pattern.compile("^(?:[^\\p{L}]*\\p{L}+){" + range.from()
                + "}((?:[^\\p{L}]*\\p{L}+){" + (range.to() - range.from()) + "}[^\\p{L}]*)");
        Matcher matcher = pattern.matcher(input);
        boolean foundMatch = matcher.find();

        if (foundMatch) {
            return matcher.group(1);
        } else {
            return null; // unreachable, hopefully
        }
    }

    private static String normalizeWord(final String word) {
        try {
            if(word.isEmpty()) {
                return null;
            }

            return word.toLowerCase(Locale.ENGLISH);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
