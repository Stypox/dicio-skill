package com.dicio.input_recognition.standard;

import java.util.ArrayList;
import java.util.Arrays;

public class Sentence {
    private final ArrayList<ArrayList<String>> packs;

    public Sentence(final String[] firstPack, final String[] secondPack, final String[] thirdPack) {
        packs = new ArrayList<ArrayList<String>>() {{
                add(new ArrayList<String>(Arrays.asList(firstPack)));
                add(new ArrayList<String>(Arrays.asList(secondPack)));
                add(new ArrayList<String>(Arrays.asList(thirdPack)));
        }};
    }
    public Sentence(final String[] firstPack, final String[] secondPack) {
        packs = new ArrayList<ArrayList<String>>() {{
            add(new ArrayList<String>(Arrays.asList(firstPack)));
            add(new ArrayList<String>(Arrays.asList(secondPack)));
        }};
    }
    public Sentence(final String[] firstPack) {
        packs = new ArrayList<ArrayList<String>>() {{
            add(new ArrayList<String>(Arrays.asList(firstPack)));
        }};
    }

    float score(ArrayList<String> words) {
        return packs.get(0).equals(words) ? 1 : 0;
    }

    int nrCapturingGroups() {
        return packs.size() - 1;
    }
}
