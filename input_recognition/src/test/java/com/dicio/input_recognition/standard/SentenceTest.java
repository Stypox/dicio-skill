package com.dicio.input_recognition.standard;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SentenceTest {
    // [1-3]p means [1-3] packs, that is, [0-2] capturing groups (e.g. test1p=testOnePack)

    private static final float floatEqualsDelta = 0.0001f;


    private ArrayList<String> split(String str) {
        ArrayList<String> splitStr = new ArrayList<>(Arrays.asList(str.split(" ")));
        splitStr.removeAll(Arrays.asList(""));
        return splitStr;
    }
    private String[] splitArr(String str) {
        ArrayList<String> splitStr = split(str);
        return splitStr.toArray(new String[0]);
    }
    
    private void assertSentence(Sentence s, String input, float a, float b, String captGr0, String captGr1) {
        float score = s.score(split(input));
        assertEquals((captGr0 != null ? 1 : 0) + (captGr1 != null ? 1 : 0), s.nrCapturingGroups());

        if (a == b) {
            assertEquals("Score " + score + " is not equal to " + a, a, score, floatEqualsDelta);
        } else {
            assertTrue("Score " + score + " is not in range [" + a + ", " + b + "]", a <= score && score <= b);
        }

        if (captGr0 != null) {
            assertArrayEquals(splitArr(captGr0), s.getCapturingGroup(0).toArray());
        }
        if (captGr1 != null) {
            assertArrayEquals(splitArr(captGr1), s.getCapturingGroup(1).toArray());
        }
    }




    @Test
    public void testPartialScoreValues() {
        PartialScoreResult scoreLeft = Sentence.scoreFromLeft(split("hello"), split("bob hello mary"));
        assertEquals(scoreLeft.nrFoundWords, 1);
        assertEquals(scoreLeft.nrExpectedWords, 1);
        assertEquals(scoreLeft.usedWordsOffset, 0);
        assertEquals(scoreLeft.usedWordsSize, 2);

        PartialScoreResult scoreRight = Sentence.scoreFromRight(split("hello"), split("bob hello mary"));
        assertEquals(scoreRight.nrFoundWords, 1);
        assertEquals(scoreRight.nrExpectedWords, 1);
        assertEquals(scoreRight.usedWordsOffset, 1);
        assertEquals(scoreRight.usedWordsSize, 2);
    }

    @Test
    public void testPenalizeIfEmptyCapturingGroup() {
        assertTrue(Sentence.penalizeIfMissingCapturingGroup(true, 0.5f) < 0.5f);
        assertEquals(0.5f, Sentence.penalizeIfMissingCapturingGroup(false, 0.5f), 0.0f);
        assertEquals(0.0f, Sentence.penalizeIfMissingCapturingGroup(true, 0.001f), 0.0f);
    }


    @Test
    public void test1p() {
        Sentence s = new Sentence(new String[] {"hello", "how", "are", "you"});

        assertSentence(s, "hello how are you",     1.0f, 1.0f, null, null);
        assertSentence(s, "hello how is you",      0.7f, 0.8f, null, null);
        assertSentence(s, "hello how are you bob", 0.9f, 1.0f, null, null);
        assertSentence(s, "mary",                  0.0f, 0.0f, null, null);
        assertSentence(s, "",                      0.0f, 0.0f, null, null);
    }


    @Test
    public void test2p() {
        Sentence s = new Sentence(splitArr("hello"), splitArr("how are you"));

        assertSentence(s, "hello bob how are you",                     1.0f,  1.0f,  "bob",          null);
        assertSentence(s, "hello bob and mary how is you",             0.7f,  0.8f,  "bob and mary", null);
        assertSentence(s, "hello mary how are steaks inside you",      0.8f,  0.9f,  "mary",         null);
        assertSentence(s, "hello bob how are steaks doing inside you", 0.6f,  0.7f,  "bob",          null);
        assertSentence(s, "hi hello bob how are not you",              0.6f,  0.7f,  "bob" ,         null);
        assertSentence(s, "hi hello mary and bob how are not you",     0.6f,  0.7f,  "mary and bob", null);
        assertSentence(s, "hello mary",                                0.5f,  0.5f,  "mary",         null);
        assertSentence(s, "bob how are you",                           0.5f,  0.5f,  "bob",          null);
        assertSentence(s, "mary and bob",                              0.0f,  0.0f,  "mary and bob", null);
        assertSentence(s, "hello how are you",                         0.75f, 0.75f, "",             null);
        assertSentence(s, "",                                          0.0f,  0.0f,  "",             null);
    }

    @Test
    public void test2pLeftEmpty() {
        Sentence s = new Sentence(splitArr(""), splitArr("how are you"));

        assertSentence(s, "hello bob how are you",                     1.0f,  1.0f,  "hello bob",          null);
        assertSentence(s, "hello bob and mary how is you",             0.4f,  0.5f,  "hello bob and mary", null);
        assertSentence(s, "hello mary how are steaks inside you",      0.6f,  0.7f,  "hello mary",         null);
        assertSentence(s, "hello bob how are steaks doing inside you", 0.3f,  0.4f,  "hello bob",          null);
        assertSentence(s, "hi hello bob how are not you",              0.8f,  0.9f,  "hi hello bob" ,      null);
        assertSentence(s, "bob how are you",                           1.0f,  1.0f,  "bob",                null);
        assertSentence(s, "mary and bob",                              0.0f,  0.0f,  "mary and bob",       null);
        assertSentence(s, "how are you",                               0.75f, 0.75f, "",                   null);
        assertSentence(s, "",                                          0.0f,  0.0f,  "",                   null);
    }

    @Test
    public void test2pRightEmpty() {
        Sentence s = new Sentence(splitArr("hello"), splitArr(""));

        assertSentence(s, "hello bob",       1.0f,  1.0f,  "bob",          null);
        assertSentence(s, "hi hello bob",    0.3f,  0.4f,  "bob",          null);
        assertSentence(s, "hi hi hello bob", 0.1f,  0.2f,  "bob",          null);
        assertSentence(s, "mary and bob",    0.0f,  0.0f,  "mary and bob", null);
        assertSentence(s, "hello",           0.75f, 0.75f, "",             null);
        assertSentence(s, "",                0.0f,  0.0f,  "",             null);
    }


    @Test
    public void test3p() {
        Sentence s = new Sentence(splitArr("i want"), splitArr("liters of"), splitArr("please"));

        assertSentence(s, "i want five liters of milk please",                1.0f, 1.0f, "five",            "milk");
        assertSentence(s, "i want five and a half liters of soy milk please", 1.0f, 1.0f, "five and a half", "soy milk");
        assertSentence(s, "i want five liters of milk",                       0.6f, 0.7f, "five",            "milk");
        assertSentence(s, "five and a half liters of soy milk",               0.3f, 0.4f, "five and a half", "soy milk");
        assertSentence(s, "i want one liter of milk please",                  0.7f, 0.8f, "one liter",       "milk");
        assertSentence(s, "i want one liter milk please",                     0.6f, 0.7f, "one liter",       "milk");
        assertSentence(s, "i want one liter soy milk please",                 0.6f, 0.7f, "one liter",       "soy milk");
        assertSentence(s, "i want one liter of milk",                         0.4f, 0.5f, "one liter",       "milk");
        assertSentence(s, "one liter of soy milk",                            0.0f, 0.1f, "one liter",       "soy milk");
        assertSentence(s, "i want milk please",                               0.4f, 0.5f, "milk",            "");
        assertSentence(s, "i want please",                                    0.1f, 0.2f, "",                "");
        assertSentence(s, "i do want please",                                 0.0f, 0.1f, "",                "");
        assertSentence(s, "i want",                                           0.0f, 0.0f, "",                "");
        assertSentence(s, "you want five liters of milk please",              0.6f, 0.7f, "five",            "milk");
        assertSentence(s, "i need five liters of milk please",                0.7f, 0.8f, "need five",       "milk");
        assertSentence(s, "you need five liters of milk please",              0.6f, 0.7f, "you need five",   "milk");
        assertSentence(s, "i want five liters of soy milk",                   0.6f, 0.7f, "five",            "soy milk");
        assertSentence(s, "i need five liters of soy milk",                   0.4f, 0.5f, "need five",       "soy milk");
        assertSentence(s, "one soy milk",                                     0.0f, 0.0f, "one soy",         "milk");
        assertSentence(s, "milk",                                             0.0f, 0.0f, "milk",            "");
        assertSentence(s, "",                                                 0.0f, 0.0f, "",                "");
    }

    @Test
    public void test3pLeftEmpty() {
        Sentence s = new Sentence(splitArr(""), splitArr("liters of"), splitArr("please"));

        assertSentence(s, "five liters of milk please",                1.0f,  1.0f,  "five",            "milk");
        assertSentence(s, "five and a half liters of soy milk please", 1.0f,  1.0f,  "five and a half", "soy milk");
        assertSentence(s, "five liters of milk",                       0.5f,  0.5f,  "five",            "milk");
        assertSentence(s, "one liter of milk please",                  0.6f,  0.7f,  "one liter",       "milk");
        assertSentence(s, "one liter soy milk please",                 0.5f,  0.5f,  "one liter",       "soy milk");
        assertSentence(s, "one liter of milk",                         0.1f,  0.2f,  "one liter",       "milk");
        assertSentence(s, "milk please",                               0.25f, 0.25f, "milk",            "");
        assertSentence(s, "please",                                    0.0f,  0.0f,  "",                "");
        assertSentence(s, "one soy milk",                              0.0f,  0.0f,  "one soy",         "milk");
        assertSentence(s, "milk",                                      0.0f,  0.0f,  "milk",            "");
        assertSentence(s, "",                                          0.0f,  0.0f,  "",                "");
    }

    @Test
    public void test3pRightEmpty() {
        Sentence s = new Sentence(splitArr("i want"), splitArr("liters of"), splitArr(""));

        assertSentence(s, "i want five liters of milk",                1.0f,  1.0f,  "five",            "milk");
        assertSentence(s, "i want five and a half liters of soy milk", 1.0f,  1.0f,  "five and a half", "soy milk");
        assertSentence(s, "five and a half liters of soy milk",        0.5f,  0.5f,  "five and a half", "soy milk");
        assertSentence(s, "i want one liter of milk",                  0.6f,  0.7f,  "one liter",       "milk");
        assertSentence(s, "i want one liter milk",                     0.5f,  0.5f,  "one liter",       "milk");
        assertSentence(s, "one liter of soy milk",                     0.1f,  0.2f,  "one liter",       "soy milk");
        assertSentence(s, "i want milk",                               0.25f, 0.25f, "milk",            "");
        assertSentence(s, "i want",                                    0.0f,  0.0f,  "",                "");
        assertSentence(s, "you want five liters of milk",              0.5f,  0.6f,  "five",            "milk");
        assertSentence(s, "i need five liters of milk",                0.6f,  0.7f,  "need five",       "milk");
        assertSentence(s, "you need five liters of milk",              0.5f,  0.5f,  "you need five",   "milk");
        assertSentence(s, "one soy milk",                              0.0f,  0.0f,  "one soy",         "milk");
        assertSentence(s, "milk",                                      0.0f,  0.0f,  "milk",            "");
        assertSentence(s, "",                                          0.0f,  0.0f,  "",                "");
    }

    @Test
    public void test3pLeftRightEmpty() {
        Sentence s = new Sentence(splitArr(""), splitArr("and"), splitArr(""));

        assertSentence(s, "bob and mary",           1.0f,  1.0f,  "bob",          "mary");
        assertSentence(s, "bob and mary and simon", 1.0f,  1.0f,  "bob and mary", "simon");
        assertSentence(s, "bob mary",               0.0f,  0.0f,  "bob",          "mary");
        assertSentence(s, "and mary",               0.75f, 0.75f, "",             "mary");
        assertSentence(s, "bob and",                0.75f, 0.75f, "bob",          "");
        assertSentence(s, "",                       0.0f,  0.0f,  "",             "");
    }
}