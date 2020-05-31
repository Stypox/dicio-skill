package com.dicio.component.standard;

import org.junit.Test;

import static org.junit.Assert.*;

public class PartialScoreResultTest {
    private static final float floatEqualsDelta = 0.0001f;
    
    private void assertScoreInRange(int nrFoundWords, int nrActualWords, int nrExpectedWords, float a, float b) {
        /*PartialScoreResult psr = new PartialScoreResult(nrFoundWords, nrExpectedWords, 0, 0);
        float score = psr.score(nrActualWords);
        
        if (a == b) {
            assertEquals("Score " + score + " is not equal to " + a, a, score, floatEqualsDelta);
        } else {
            assertTrue("Score " + score + " is not in range [" + a + ", " + b + "]", a <= score && score <= b);
        }*/
    }

    /*
    @Test
    public void testDropAt0point75() {
        assertEquals(0.0f, PartialScoreResult.dropAt0point75(0.0f), floatEqualsDelta);
        assertEquals(1.0f, PartialScoreResult.dropAt0point75(1.0f), floatEqualsDelta);

        assertTrue(PartialScoreResult.dropAt0point75(0.8f) > 0.8f);
        assertTrue(PartialScoreResult.dropAt0point75(0.7f) < 0.65f);
        assertTrue(PartialScoreResult.dropAt0point75(0.6f) < 0.4f);
    }

    @Test
    public void testDropAt0point6() {
        assertEquals(0.0f, PartialScoreResult.dropAt0point6(0.0f), floatEqualsDelta);
        assertEquals(1.0f, PartialScoreResult.dropAt0point6(1.0f), floatEqualsDelta);

        assertTrue(PartialScoreResult.dropAt0point6(0.7f) > 0.8f);
        assertTrue(PartialScoreResult.dropAt0point6(0.6f) < 0.7f);
        assertTrue(PartialScoreResult.dropAt0point6(0.5f) < 0.4f);
    }*/

    @Test
    public void testScore() {
        // f = a = e
        assertScoreInRange(10, 10, 10, 1.0f, 1.0f);

        // f = 0
        assertScoreInRange( 0, 10, 10, 0.0f, 0.0f);
        assertScoreInRange( 0,  1, 17, 0.0f, 0.0f);
        assertScoreInRange( 0, 17,  1, 0.0f, 0.0f);
        assertScoreInRange( 0,  0,  1, 0.0f, 0.0f);

        // f/a ~= 1 ^ f/e ~= 1
        assertScoreInRange( 8, 10,  8, 0.8f, 1.0f);
        assertScoreInRange( 8,  8, 10, 0.8f, 1.0f);
        assertScoreInRange( 9, 10, 10, 0.8f, 1.0f);
        assertScoreInRange( 8, 10, 10, 0.8f, 1.0f);

        // f/a ~= 0.6 ^ f/e ~= 1
        assertScoreInRange( 7, 10,  8, 0.5f, 0.8f);
        assertScoreInRange( 6, 10,  7, 0.5f, 0.8f);
        assertScoreInRange( 6, 10,  6, 0.5f, 0.8f);
        assertScoreInRange( 6, 10,  8, 0.4f, 0.6f);

        // f/a ~< 0.5 ^ f/e ~< 0.8
        assertScoreInRange( 5, 10,  8, 0.0f, 0.5f);
        assertScoreInRange( 5, 10,  8, 0.0f, 0.5f);
        assertScoreInRange( 8, 15, 10, 0.0f, 0.5f);

        // others
        assertScoreInRange(10, 20, 10, 0.0f, 0.5f);
        assertScoreInRange(10, 10, 16, 0.0f, 0.5f);
    }

    /*
    @Test
    public void testOffsetAndSize() {
        PartialScoreResult psr = new PartialScoreResult(5, 5, 5, 12);
        assertEquals(psr.firstUsedIdx(), 5);
        assertEquals(psr.onePastLastUsedIdx(), 17);

        psr.usedWordsOffset += 7;
        assertEquals(psr.firstUsedIdx(), 12);
        assertEquals(psr.onePastLastUsedIdx(), 24);

        assertEquals(psr.usedWordsSize, 12);
        assertEquals(psr.score(12), psr.score(), floatEqualsDelta);
    }*/
}