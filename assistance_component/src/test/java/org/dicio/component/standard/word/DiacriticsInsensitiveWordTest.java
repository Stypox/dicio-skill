package org.dicio.component.standard.word;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.Collator;
import java.util.Locale;

import static org.junit.Assert.*;

public class DiacriticsInsensitiveWordTest {

    private static Collator collator;

    @BeforeClass
    public static void setupCollator() {
        collator = Collator.getInstance(Locale.ENGLISH);
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
    }


    private DiacriticsInsensitiveWord diw(final String value) {
        return new DiacriticsInsensitiveWord(collator.getCollationKey(value).toByteArray(), 0);
    }

    private void assertMatches(final String value, final String... inputWords) {
        final DiacriticsInsensitiveWord diacriticsInsensitiveWord = diw(value);
        for (final String word : inputWords) {
            assertTrue(value + " can't match " + word, diacriticsInsensitiveWord.matches(word));
        }
    }

    private void assertNotMatches(final String value, final String... inputWords) {
        final DiacriticsInsensitiveWord diacriticsInsensitiveWord = diw(value);
        for (final String word : inputWords) {
            assertFalse(diacriticsInsensitiveWord.matches(word));
        }
    }


    @Test
    public void testMatches() {
        assertMatches("hello", "hèllo", "hellò", "héllò");
        assertMatches("ssùlìc", "ßùliç", "ssulic", "ßulìc");
    }

    @Test
    public void testNotMatches() {
        // note: ħ and ł do not match, even with full decompositor
        assertNotMatches("hello", "ħello", "hèłlo", "ħéłłò");
        assertNotMatches("ciao", "ciau", "chiao", "hiao", "chao");
    }
}
