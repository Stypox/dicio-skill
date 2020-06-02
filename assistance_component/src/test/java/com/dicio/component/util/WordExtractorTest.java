package com.dicio.component.util;

import com.dicio.component.standard.InputWordRange;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WordExtractorTest {

    private static void assertExtractedWords(final String input, final String... words) {
        final List<String> extractedWords = WordExtractor.extractWords(input);
        assertEquals(words.length, extractedWords.size());
        assertArrayEquals(words, extractedWords.toArray());
    }

    private static void assertExtractedCapturingGroup(final String left,
                                                      final String output,
                                                      final String right,
                                                      final int from,
                                                      final int to) {
        final String input = left + output + right;
        assertEquals(output,
                WordExtractor.extractCapturingGroup(input, new InputWordRange(from, to)));
    }

    @Test
    public void extractWordsTest() {
        assertExtractedWords("213heÉlo? \n\t .°- \nHOWè@ç§Ù\n+", "heélo", "howè", "ç", "ù");
        assertExtractedWords("#\tfs ùà@äöü\n°938ßÄÖÜ£&/", "fs", "ùà", "äöü", "ßäöü");
    }

    @Test
    public void extractCapturingGroupTest() {
        assertExtractedCapturingGroup("", " ? - hello =? - how #are\nyou\n\t+ ", "", 0, 4);
        assertExtractedCapturingGroup("&&tàgF/sßl?fg", "+@26àèù§An/dh$ \"'938ßÄÖÜà°", "ç?&", 3, 7);
    }
}
