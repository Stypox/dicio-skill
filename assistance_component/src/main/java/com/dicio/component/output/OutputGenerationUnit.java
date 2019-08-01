package com.dicio.component.output;

import com.dicio.component.AssistanceComponent;
import com.dicio.component.TieInputOutput;
import com.dicio.component.input.InputRecognitionUnit;
import com.dicio.component.output.views.ViewList;

import java.util.List;
import java.util.Optional;

/**
 * Generates graphical and speech output (possibily based on input)
 * @param <IRU> type of corresponding input recognition unit
 */
public interface OutputGenerationUnit<IRU extends InputRecognitionUnit> {

    /**
     * Calculates what is needed to generate the output to be
     * displayed or spoken.
     * <p>
     * In the process of generating output, it has to be called before
     * any other function in {@link OutputGenerationUnit}, since it calculates
     * what's needed for the other functions.
     * <p>
     * For example it could make internet requests, read local files
     * or get information from anywhere.
     *
     * @see #getGraphicalOutput()
     * @see #getSpeechOutput()
     * @param inputRecognitionUnit the corresponding input recognition unit
     * for this output generator, useful to access input information.
     */
    void calculateOutput(IRU inputRecognitionUnit);

    /**
     * Using the info calculated by {@link #calculateOutput(InputRecognitionUnit)},
     * generates a graphical output.
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput(InputRecognitionUnit)} but before {@link #nextOutputGenerator()}
     * and {@link #nextAssistanceComponents()}, otherwise the output would
     * probably be ignored. It makes no sense to proceed before giving some
     * feedback to the user.
     *
     * @see com.dicio.component.output.views
     * @return a list of basic views
     */
    ViewList getGraphicalOutput();

    /**
     * Using the info calculated by {@link #calculateOutput(InputRecognitionUnit)},
     * generates a speech output, which is going to be handled by
     * a text-to-speech engine
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput(InputRecognitionUnit)} but before
     * {@link #nextOutputGenerator()} and {@link #nextAssistanceComponents()},
     * otherwise the output would probably be ignored. It makes no sense to
     * proceed before giving some feedback to the user.
     *
     * @return input for the TTS engine
     */
    String getSpeechOutput();

    /**
     * If the output calculated by {@link #calculateOutput(InputRecognitionUnit)} was
     * partial, returns another {@link OutputGenerationUnit}.
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput(InputRecognitionUnit)},
     * {@link #getGraphicalOutput()} and {@link #getSpeechOutput()} but before
     * {@link #nextAssistanceComponents()}, because if there is more output on the
     * way the next {@link OutputGenerationUnit} should handle user interaction. So if a
     * value is returned {@link #nextAssistanceComponents()} must not be called.
     * <p>
     * Useful to try to generate the output in different ways and give feedback about
     * the process.
     *
     * @return another {@link OutputGenerationUnit}, if needed
     */
    Optional<OutputGenerationUnit> nextOutputGenerator();

    /**
     * If the output calculated by {@link #calculateOutput(InputRecognitionUnit)}
     * contained a question for the user, returns a list of
     * {@link AssistanceComponent}s to handle the answer and
     * generate more output.
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput(InputRecognitionUnit)}, {@link #getGraphicalOutput()}
     * and {@link #getSpeechOutput()} and {@link #nextOutputGenerator()}. It must not
     * be called if {@link #nextOutputGenerator()} returned a value, because if there
     * is more output on the way the next {@link OutputGenerationUnit} should handle user
     * interaction.
     * <p>
     * Useful when more information is needed to answer the user's original question.
     * For simple and small assistance components use
     * {@link TieInputOutput TieInputOutput}
     *
     * @see TieInputOutput TieInputOutput
     * @return a list of {@link AssistanceComponent}s, if needed
     */
    Optional<List<AssistanceComponent>> nextAssistanceComponents();

}
