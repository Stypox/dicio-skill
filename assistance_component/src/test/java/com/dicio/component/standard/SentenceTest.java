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
        final List<String> splitStr = new ArrayList<>(Arrays.asList(str.split(" ")));
        splitStr.removeAll(Collections.singletonList(""));
        return splitStr;
    }


    private void addAllWords(final List<String> packWords, final List<Word> words, final int minimumSkippedWordsToEnd) {
        for (int i = 0; i < packWords.size(); ++i) {
            words.add(new Word(packWords.get(i), false, minimumSkippedWordsToEnd + packWords.size() - i, words.size() + 1));
        }
    }

    private void addCapturingGroup(int index, final List<Word> words, final int minimumSkippedWordsToEnd) {
        words.add(new Word(Integer.toString(index), true, minimumSkippedWordsToEnd + 2, words.size() + 1));
    }


    private class SentenceInfo {
        final int wordCount;
        final Sentence sentence;

        SentenceInfo(final String pack1) {
            final List<Word> words = new ArrayList<>();
            final List<String> pack1Words = split(pack1);

            addAllWords(pack1Words, words, 0);
            wordCount = words.size();
            sentence = new Sentence("", new int[] {0}, words.toArray(new Word[0]));
        }

        SentenceInfo(final String pack1, final String pack2) {
            final List<Word> words = new ArrayList<>();
            final List<String> pack1Words = split(pack1);
            final List<String> pack2Words = split(pack2);

            addAllWords(pack1Words, words, 2 + pack2Words.size());
            addCapturingGroup(0, words, pack2Words.size());
            addAllWords(pack2Words, words, 0);
            wordCount = words.size();
            sentence = new Sentence("", new int[] {0}, words.toArray(new Word[0]));
        }

        SentenceInfo(final String pack1, final String pack2, final String pack3) {
            final List<Word> words = new ArrayList<>();
            final List<String> pack1Words = split(pack1);
            final List<String> pack2Words = split(pack2);
            final List<String> pack3Words = split(pack3);

            addAllWords(pack1Words, words, 4 + pack2Words.size() + pack3Words.size());
            addCapturingGroup(0, words, 2 + pack2Words.size() + pack3Words.size());
            addAllWords(pack2Words, words, 2 + pack3Words.size());
            addCapturingGroup(1, words, pack3Words.size());
            addAllWords(pack3Words, words, 0);
            wordCount = words.size();
            sentence = new Sentence("", new int[] {0}, words.toArray(new Word[0]));
        }
    }



    private void assertCapturingGroup(final List<String> inputWords,
                                      final InputWordRange range, final String captGr) {
        if (captGr == null) {
            assertNull(range);
            return;
        }

        final List<String> captGrWords = split(captGr);
        final List<String> actualCaptGrWords = new ArrayList<>();
        for (int i = range.from(); i < range.to(); ++i) {
            actualCaptGrWords.add(inputWords.get(i));
        }

        assertThat(actualCaptGrWords, CoreMatchers.is(captGrWords));
    }
    
    private void assertSentence(final SentenceInfo s, final String input,
                                final float a, final float b,
                                final String captGr0, final String captGr1) {
        final List<String> inputWords = split(input);
        float score = s.sentence.score(inputWords);
        final PartialScoreResult scoreResult = s.sentence.getBestScoreResult();

        assertEquals(score, scoreResult.value(inputWords.size()), 0.0f);
        if (a == b) {
            assertEquals("Score " + score + " " + scoreResult + " is not equal to " + a, a, score, floatEqualsDelta);
        } else {
            assertTrue("Score " + score + " " + scoreResult + " is not in range [" + a + ", " + b + "]", a <= score && score <= b);
        }

        final StandardResult r = s.sentence.toStandardResult();
        assertEquals((captGr0 != null ? 1 : 0) + (captGr1 != null ? 1 : 0),
                r.getCapturingGroups().size());
        assertCapturingGroup(inputWords, r.getCapturingGroups().get("0"), captGr0);
        assertCapturingGroup(inputWords, r.getCapturingGroups().get("1"), captGr1);
    }



    @Test
    public void testSentenceId() {
        final String sentenceId = "SentenceID";
        final Sentence s = new Sentence(sentenceId, new int[] {0}, new Word("hello", false, 0, 1));
        s.score(new ArrayList<>());
        final StandardResult r = s.toStandardResult();

        assertEquals(sentenceId, r.getSentenceId());
    }


    @Test
    public void test1p() {
        final SentenceInfo s = new SentenceInfo("hello how are you");

        assertSentence(s, "hello how are you",     1.0f, 1.0f, null, null);
        assertSentence(s, "hello how is you",      0.7f, 0.8f, null, null);
        assertSentence(s, "hello how are you bob", 0.9f, 1.0f, null, null);
        assertSentence(s, "mary",                  0.0f, 0.0f, null, null);
        assertSentence(s, "",                      0.0f, 0.0f, null, null);
    }


    @Test
    public void test2p() {
        final SentenceInfo s = new SentenceInfo("hello", "how are you");

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
        final SentenceInfo s = new SentenceInfo("", "how are you");

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
        final SentenceInfo s = new SentenceInfo("hello", "");

        assertSentence(s, "hello bob",       1.0f, 1.0f, "bob",             null);
        assertSentence(s, "hi hello bob",    0.3f, 0.4f, "bob",             null);
        assertSentence(s, "hi hi hello bob", 0.2f, 0.3f, "hi hi hello bob", null);
        assertSentence(s, "mary and bob",    0.2f, 0.3f, "mary and bob",    null);
        assertSentence(s, "hello",           0.1f, 0.2f, null,              null);
        assertSentence(s, "",                0.0f, 0.0f, null,              null);
    }


    @Test
    public void test3p() {
        final SentenceInfo s = new SentenceInfo("i want", "liters of", "please");

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
        assertSentence(s, "i want five liters of m please a a a a",           0.5f, 0.6f, "five",            "m");
    }

    @Test
    public void test3pLeftEmpty() {
        final SentenceInfo s = new SentenceInfo("", "liters of", "please");

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
        final SentenceInfo s = new SentenceInfo("i want", "liters of", "");

        assertSentence(s, "i want five liters of milk",                1.0f, 1.0f, "five",            "milk");
        assertSentence(s, "i want five and a half liters of soy milk", 1.0f, 1.0f, "five and a half", "soy milk");
        assertSentence(s, "five and a half liters of soy milk",        0.5f, 0.6f, "five and a half", "soy milk");
        assertSentence(s, "i want one liter of milk",                  0.9f, 1.0f, "one liter",       "milk");
        assertSentence(s, "i want one liter milk",                     0.5f, 0.6f, "one liter",       "milk");
        assertSentence(s, "one liter of soy milk",                     0.2f, 0.3f, "one liter",       "soy milk");
        assertSentence(s, "i want milk",                               0.1f, 0.2f, "milk",            null);
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
        final SentenceInfo s = new SentenceInfo("", "and", "");

        assertSentence(s, "bob and mary",           1.0f, 1.0f, "bob",          "mary");
        assertSentence(s, "bob and mary and simon", 1.0f, 1.0f, "bob and mary", "simon");
        assertSentence(s, "bob mary",               0.5f, 0.6f, "bob",          "mary");
        assertSentence(s, "and mary",               0.5f, 0.6f, "and",          "mary");
        assertSentence(s, "bob and",                0.2f, 0.3f, "bob",          null);
        assertSentence(s, "",                       0.0f, 0.0f, null,           null);
    }


    @Test
    public void testDuplicateWord() {
        final SentenceInfo s = new SentenceInfo("how do you do bob");

        assertSentence(s, "how do you do bob",     1.0f, 1.0f, null, null);
        assertSentence(s, "how does you do bob",   0.8f, 0.9f, null, null);
        assertSentence(s, "how does a you do bob", 0.6f, 0.7f, null, null);
    }
}