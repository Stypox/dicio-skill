package org.dicio.component.util;

import org.dicio.component.standard.InputWordRange;

import org.dicio.component.standard.word.DiacriticsInsensitiveWord;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WordExtractorTest {

    private static void assertExtractedWords(final String input, final String... expectedWords) {
        final List<String> actualWords = WordExtractor.extractWords(input);
        assertArrayEquals(expectedWords, actualWords.toArray());
    }

    public static void assertCollationKey(final String baseWord, final String... inputs) {
        final List<byte[]> inputCollationKeys =
                WordExtractor.getCollationKeys(Arrays.asList(inputs));
        final byte[] baseCollationKey = DiacriticsInsensitiveWord.getCollationKey(baseWord);

        assertEquals(inputs.length, inputCollationKeys.size());
        for (int i = 0; i < inputs.length; i++) {
            assertArrayEquals("Collation key is different from that of " + baseWord
                    + ": " + inputs[i], baseCollationKey, inputCollationKeys.get(i));
        }
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
        assertExtractedWords("\n \n\n\n hello\f   \n  \r  \thow-are  \r   you   \r\n", "hello", "how", "are", "you");
        assertExtractedWords("Hello HOW aRe yoU", "hello", "how", "are", "you");
        assertExtractedWords("à è ì ò ù À È Ì Ò Ù", "à","è","ì","ò","ù","à","è","ì","ò","ù");
        assertExtractedWords(" \r\n \f\n \r\t\t+-_");
        assertExtractedWords("");
    }

    @Test
    public void getCollationKeysTest() {
        assertCollationKey("aeiou", "aeiou", "àeiòu", "àéÌOù", "AÉIOU");
        assertCollationKey("sS",    "ss", "ß");
        assertCollationKey("ßèç",   "ssec", "Sseç", "sSèc", "SSèç", "sséc", "sSéç", "ßec", "ßeç", "ßèc", "ßèç", "ßéc", "ßéç");
    }

    @Test
    public void extractCapturingGroupTest() {
        assertExtractedCapturingGroup("", " ? - hello =? - how #are\nyou\n\t+ ", "", 0, 4);
        assertExtractedCapturingGroup("&&tàgF/sßl?fg", "+@26àèù§An/dh$ \"'938ßÄÖÜà°", "ç?&", 3, 7);
    }
}
