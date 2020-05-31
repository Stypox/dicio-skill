package com.dicio.component.standard;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SentenceTest {
    // [1-3]p means [1-3] packs, that is, [0-2] capturing groups (e.g. test1p=testOnePack)

    private static final float floatEqualsDelta = 0.0001f;


    private List<String> split(String str) {
        List<String> splitStr = new ArrayList<>(Arrays.asList(str.split(" ")));
        splitStr.removeAll(Collections.singletonList(""));
        return splitStr;
    }


    private void addAllWords(final String pack, final List<Word> words) {
        final List<String> packWords = split(pack);
        for (int i = 0; i < packWords.size(); ++i) {
            words.add(new Word(packWords.get(i), false, words.size() + 1));
        }
    }

    private void addCapturingGroup(int index, final List<Word> words) {
        words.add(new Word(Integer.toString(index), true, words.size() + 1));
    }


    private class SentenceInfo {
        final int wordCount;
        final Sentence sentence;

        SentenceInfo(final String pack1) {
            List<Word> words = new ArrayList<>();
            addAllWords(pack1, words);
            wordCount = words.size();
            sentence = new Sentence("", words.toArray(new Word[0]));
        }

        SentenceInfo(final String pack1, final String pack2) {
            List<Word> words = new ArrayList<>();
            addAllWords(pack1, words);
            addCapturingGroup(0, words);
            addAllWords(pack2, words);
            wordCount = words.size();
            sentence = new Sentence("", words.toArray(new Word[0]));
        }

        SentenceInfo(final String pack1, final String pack2, final String pack3) {
            List<Word> words = new ArrayList<>();
            addAllWords(pack1, words);
            addCapturingGroup(0, words);
            addAllWords(pack2, words);
            addCapturingGroup(1, words);
            addAllWords(pack3, words);
            wordCount = words.size();
            sentence = new Sentence("", words.toArray(new Word[0]));
        }
    }



    private void assertCapturingGroup(final List<String> inputWords,
                                      final InputWordRange range, final String captGr) {
        if (captGr == null) {
            assertNull(range);
            return;
        }

        List<String> captGrWords = split(captGr);
        List<String> actualCaptGrWords = new ArrayList<>();
        for (int i = range.from(); i < range.to(); ++i) {
            actualCaptGrWords.add(inputWords.get(i));
        }

        assertThat(actualCaptGrWords, CoreMatchers.is(captGrWords));
    }
    
    private void assertSentence(final SentenceInfo s, final String input,
                                final float a, final float b,
                                final String captGr0, final String captGr1) {
        List<String> inputWords = split(input);
        float score = s.sentence.score(inputWords);
        PartialScoreResult scoreResult = s.sentence.bestScore(0, 0, false);

        assertEquals(score, scoreResult.value(s.wordCount, inputWords.size()), 0.0f);
        if (a == b) {
            assertEquals("Score " + score + " " + scoreResult + " is not equal to " + a, a, score, floatEqualsDelta);
        } else {
            assertTrue("Score " + score + " " + scoreResult + " is not in range [" + a + ", " + b + "]", a <= score && score <= b);
        }

        StandardResult r = s.sentence.toStandardResult();
        assertEquals((captGr0 != null ? 1 : 0) + (captGr1 != null ? 1 : 0),
                r.getCapturingGroups().size());
        assertCapturingGroup(inputWords, r.getCapturingGroups().get("0"), captGr0);
        assertCapturingGroup(inputWords, r.getCapturingGroups().get("1"), captGr1);
    }



    @Test
    public void testSentenceId() {
        String sentenceId = "SentenceID";
        Sentence s = new Sentence(sentenceId, new Word("hello", false, 1, 2, 3, 4));
        s.score(new ArrayList<>());
        StandardResult r = s.toStandardResult();

        assertEquals(sentenceId, r.getSentenceId());
    }


    @Test
    public void test1p() {
        SentenceInfo s = new SentenceInfo("hello how are you");

        //assertSentence(s, "hello how are you",     1.0f, 1.0f, null, null);
        assertSentence(s, "hello how is you",      0.7f, 0.8f, null, null);
        assertSentence(s, "hello how are you bob", 0.9f, 1.0f, null, null);
        assertSentence(s, "mary",                  0.0f, 0.0f, null, null);
        assertSentence(s, "",                      0.0f, 0.0f, null, null);
    }


    @Test
    public void test2p() {
        SentenceInfo s = new SentenceInfo("hello", "how are you");

        assertSentence(s, "hello bob how are you",                     1.0f, 1.0f, "bob",             null);
        assertSentence(s, "hello bob and mary how is you",             0.7f, 0.8f, "bob and mary",    null);
        assertSentence(s, "hello mary how are steaks inside you",      0.7f, 0.8f, "mary",            null);
        assertSentence(s, "hello bob how are steaks doing inside you", 0.5f, 0.6f, "bob",             null);
        assertSentence(s, "hi hello bob how are not you",              0.7f, 0.8f, "bob" ,            null);
        assertSentence(s, "hi hello mary and bob how are not you",     0.7f, 0.8f, "mary and bob",    null);
        assertSentence(s, "hello mary",                                0.1f, 0.2f, "mary",            null);
        assertSentence(s, "bob how are you",                           0.8f, 0.9f, "bob",             null);
        assertSentence(s, "mary and bob",                              0.0f, 0.1f, "mary and bob",    null);
        assertSentence(s, "hello how are you",                         0.8f, 0.9f, "how",             null);
        assertSentence(s, "",                                          0.0f, 0.0f, null,              null);
        assertSentence(s, "a a a a hello b how are you",               0.8f, 0.9f, "a a a a hello b", null);
        assertSentence(s, "hello b how a a a a are you",               0.8f, 0.9f, "b how a a a a",   null);
    }

    @Test
    public void test2pLeftEmpty() {
        SentenceInfo s = new SentenceInfo("", "how are you");

        assertSentence(s, "hello bob how are you",                     1.0f, 1.0f, "hello bob",          null);
        assertSentence(s, "hello bob and mary how is you",             0.6f, 0.7f, "hello bob and mary", null);
        assertSentence(s, "hello mary how are steaks inside you",      0.6f, 0.7f, "hello mary",         null);
        assertSentence(s, "hello bob how are steaks doing inside you", 0.3f, 0.4f, "hello bob",          null);
        assertSentence(s, "hi hello bob how are not you",              0.8f, 0.9f, "hi hello bob" ,      null);
        assertSentence(s, "bob how are you",                           1.0f, 1.0f, "bob",                null);
        assertSentence(s, "mary and bob",                              0.0f, 0.1f, "mary and bob",       null);
        assertSentence(s, "how are you",                               0.8f, 0.9f, "how",                null);
        assertSentence(s, "",                                          0.0f, 0.0f, null,                 null);
    }

    @Test
    public void test2pRightEmpty() {
        SentenceInfo s = new SentenceInfo("hello", "");

        assertSentence(s, "hello bob",       1.0f, 1.0f, "bob",             null);
        assertSentence(s, "hi hello bob",    0.3f, 0.4f, "bob",             null);
        assertSentence(s, "hi hi hello bob", 0.2f, 0.3f, "hi hi hello bob", null);
        assertSentence(s, "mary and bob",    0.2f, 0.3f, "mary and bob",    null);
        assertSentence(s, "hello",           0.0f, 0.1f, null,              null);
        assertSentence(s, "",                0.0f, 0.0f, null,              null);
    }


    @Test
    public void test3p() {
        SentenceInfo s = new SentenceInfo("i want", "liters of", "please");

        assertSentence(s, "i want five liters of milk please",                1.0f, 1.0f, "five",            "milk");
        assertSentence(s, "i want five and a half liters of soy milk please", 1.0f, 1.0f, "five and a half", "soy milk");
        assertSentence(s, "i want five liters of milk",                       0.9f, 1.0f, "five",            "milk");
        assertSentence(s, "five and a half liters of soy milk",               0.3f, 0.4f, "five and a half", "soy milk");
        assertSentence(s, "i want one liter of milk please",                  0.9f, 1.0f, "one liter",       "milk");
        assertSentence(s, "i want one liter milk please",                     0.6f, 0.7f, "one liter",       "milk");
        assertSentence(s, "i want one liter soy milk please",                 0.6f, 0.7f, "one liter soy",   "milk");
        assertSentence(s, "i want one liter of milk",                         0.6f, 0.7f, "one liter",       "milk");
        assertSentence(s, "one liter of soy milk",                            0.1f, 0.2f, "one liter",       "soy milk");
        assertSentence(s, "i want milk please",                               0.3f, 0.4f, "milk",            "please");
        assertSentence(s, "i want please",                                    0.1f, 0.2f, "please",          null);
        assertSentence(s, "i do want please",                                 0.3f, 0.4f, "do",              "want");
        assertSentence(s, "i want",                                           0.0f, 0.1f, null,              null);
        assertSentence(s, "you want five liters of milk please",              0.8f, 0.9f, "five",            "milk");
        assertSentence(s, "i need five liters of milk please",                0.9f, 1.0f, "need five",       "milk");
        assertSentence(s, "you need five liters of milk please",              0.6f, 0.7f, "you need five",   "milk");
        assertSentence(s, "i want five liters of soy milk",                   0.9f, 1.0f, "five",            "soy milk");
        assertSentence(s, "i need five liters of soy milk",                   0.6f, 0.7f, "need five",       "soy milk");
        assertSentence(s, "one soy milk",                                     0.0f, 0.1f, "one soy",         "milk");
        assertSentence(s, "milk",                                             0.0f, 0.1f, "milk",            null);
        assertSentence(s, "",                                                 0.0f, 0.0f, null,              null);
        assertSentence(s, "i a a a a want f liters of milk please",           0.9f, 1.0f, "a a a a want f",  "milk");
        assertSentence(s, "i want five liters of m please a a a a",           0.9f, 1.0f, "five",            "m please a a a a");
    }

    @Test
    public void test3pLeftEmpty() {
        SentenceInfo s = new SentenceInfo("", "liters of", "please");

        assertSentence(s, "five liters of milk please",                1.0f, 1.0f, "five",            "milk");
        assertSentence(s, "five and a half liters of soy milk please", 1.0f, 1.0f, "five and a half", "soy milk");
        assertSentence(s, "five liters of milk",                       0.8f, 0.9f, "five",            "milk");
        assertSentence(s, "one liter of milk please",                  0.8f, 0.9f, "one liter",       "milk");
        assertSentence(s, "one liter soy milk please",                 0.3f, 0.4f, "one liter soy",   "milk");
        assertSentence(s, "one liter of milk",                         0.3f, 0.4f, "one liter",       "milk");
        assertSentence(s, "milk please",                               0.1f, 0.2f, "milk",            "please");
        assertSentence(s, "please",                                    0.0f, 0.1f, "please",          null);
        assertSentence(s, "one soy milk",                              0.1f, 0.2f, "one soy",         "milk");
        assertSentence(s, "milk",                                      0.0f, 0.1f, "milk",            null);
        assertSentence(s, "",                                          0.0f, 0.0f, null,              null);
    }

    @Test
    public void test3pRightEmpty() {
        SentenceInfo s = new SentenceInfo("i want", "liters of", "");

        assertSentence(s, "i want five liters of milk",                1.0f, 1.0f, "five",            "milk");
        assertSentence(s, "i want five and a half liters of soy milk", 1.0f, 1.0f, "five and a half", "soy milk");
        assertSentence(s, "five and a half liters of soy milk",        0.5f, 0.6f, "five and a half", "soy milk");
        assertSentence(s, "i want one liter of milk",                  0.9f, 1.0f, "one liter",       "milk");
        assertSentence(s, "i want one liter milk",                     0.5f, 0.6f, "one liter",       "milk");
        assertSentence(s, "one liter of soy milk",                     0.2f, 0.3f, "one liter",       "soy milk");
        assertSentence(s, "i want milk",                               0.2f, 0.3f, "milk",            null);
        assertSentence(s, "i want",                                    0.0f, 0.1f, null,              null);
        assertSentence(s, "you want five liters of milk",              0.8f, 0.9f, "five",            "milk");
        assertSentence(s, "i need five liters of milk",                0.9f, 1.0f, "need five",       "milk");
        assertSentence(s, "you need five liters of milk",              0.5f, 0.6f, "you need five",   "milk");
        assertSentence(s, "one soy milk",                              0.1f, 0.2f, "one soy",         "milk");
        assertSentence(s, "milk",                                      0.0f, 0.1f, "milk",            null);
        assertSentence(s, "",                                          0.0f, 0.0f, null,              null);
    }

    @Test
    public void test3pLeftRightEmpty() {
        SentenceInfo s = new SentenceInfo("", "and", "");

        assertSentence(s, "bob and mary",           1.0f, 1.0f, "bob",          "mary");
        assertSentence(s, "bob and mary and simon", 1.0f, 1.0f, "bob and mary", "simon");
        assertSentence(s, "bob mary",               0.5f, 0.6f, "bob",          "mary");
        assertSentence(s, "and mary",               0.5f, 0.6f, "and",          "mary");
        assertSentence(s, "bob and",                0.1f, 0.2f, "bob and",      null);
        assertSentence(s, "",                       0.0f, 0.0f,  null,          null);
    }


    @Test
    public void testDuplicateWord() {
        SentenceInfo s = new SentenceInfo("how do you do bob");

        assertSentence(s, "how do you do bob", 1.0f, 1.0f, null, null);
        assertSentence(s, "how does you do bob", 0.8f, 0.9f, null, null);
        assertSentence(s, "how does a you do bob", 0.6f, 0.7f, null, null);
    }
}