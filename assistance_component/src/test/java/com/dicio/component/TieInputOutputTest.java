package com.dicio.component;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.input.standard.Sentence;
import com.dicio.component.input.standard.StandardRecognizer;
import com.dicio.component.output.OutputGenerator;
import com.dicio.component.output.views.BaseView;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TieInputOutputTest {
    @Test
    public void testTieIsCorrect() throws Throwable {
        final List<String> inputs1 = new ArrayList<String>() {{
            add("hello");
            add("guys");
        }};
        final List<String> inputs2 = new ArrayList<String>() {{
            add("hi");
        }};
        final String speechOutput = "yes";


        StandardRecognizer ir = new StandardRecognizer(InputRecognizer.Specificity.medium, new Sentence[]{});
        AssistanceComponent ac = new TieInputOutput<>(ir,
                new OutputGenerator<StandardRecognizer>() {
                    @Override
                    public void calculateOutput(StandardRecognizer inputRecognizer) {
                        assertArrayEquals(inputs1.toArray(), inputRecognizer.getInput().toArray());
                    }

                    @Override
                    public List<BaseView> getGraphicalOutput() {
                        // useless in this test
                        return null;
                    }

                    @Override
                    public String getSpeechOutput() {
                        return speechOutput;
                    }
                });


        ac.setInput(inputs1);
        assertArrayEquals(inputs1.toArray(), ac.getInput().toArray());
        assertArrayEquals(inputs1.toArray(), ir.getInput().toArray());
        ac.calculateOutput();

        ir.setInput(inputs2);
        assertArrayEquals(inputs2.toArray(), ac.getInput().toArray());

        assertEquals(speechOutput, ac.getSpeechOutput());
        assertFalse(ac.nextOutputGenerator().isPresent());
        assertFalse(ac.nextAssistanceComponents().isPresent());
    }

}