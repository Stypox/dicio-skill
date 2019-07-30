package com.dicio.output;

import com.dicio.AssistanceComponent;
import com.dicio.output.views.ViewList;

import java.util.List;
import java.util.Optional;

public interface OutputGenerator {
    /**
     * Calculates what is needed to generate the output to be
     * displayed or spoken.
     * <p>
     * In the process of generating output, it has to be called before
     * any other function in {@link OutputGenerator}, since it calculates
     * what's needed for the other functions.
     * <p>
     * For example it could make internet requests, read local files
     * or get information from anywhere.
     *
     * @see #getGraphicalOutput()
     * @see #getSpeechOutput()
     */
    void calculateOutput();

    /**
     * Using the info calculated by {@link #calculateOutput()},
     * generates a graphical output.
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput()} but before {@link #nextOutputGenerator()}
     * and {@link #nextAssistanceComponents()}, otherwise the output would
     * probably be ignored. It makes no sense to proceed before giving some
     * feedback to the user.
     *
     * @see com.dicio.output.views
     * @return a list of basic views
     */
    ViewList getGraphicalOutput();

    /**
     * Using the info calculated by {@link #calculateOutput()},
     * generates a speech output, which is going to be handled by
     * a text-to-speech engine
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput()} but before {@link #nextOutputGenerator()}
     * and {@link #nextAssistanceComponents()}, otherwise the output would
     * probably be ignored. It makes no sense to proceed before giving some
     * feedback to the user.
     *
     * @return input for the TTS engine
     */
    String getSpeechOutput();

    /**
     * If the output calculated by {@link #calculateOutput()} was
     * partial, returns another {@link OutputGenerator}.
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput()}, {@link #getGraphicalOutput()}
     * and {@link #getSpeechOutput()} but before
     * {@link #nextAssistanceComponents()}, because if there is more
     * output on the way the next {@link OutputGenerator} should handle
     * user interaction. So if a value is returned
     * {@link #nextAssistanceComponents()} must not be called.
     * <p>
     * Useful to try to generate the output in different ways and
     * give feedback about the process.
     *
     * @return another {@link OutputGenerator}, if needed
     */
    Optional<OutputGenerator> nextOutputGenerator();

    /**
     * If the output calculated by {@link #calculateOutput()}
     * contained a question for the user, returns a list of
     * {@link AssistanceComponent}s to handle the answer and
     * generate more output.
     * <p>
     * In the process of generating output, it has to be called after
     * {@link #calculateOutput()}, {@link #getGraphicalOutput()}
     * and {@link #getSpeechOutput()} and {@link #nextOutputGenerator()}.
     * It must not be called if {@link #nextOutputGenerator()} returned
     * a value, because if there is more output on the way the next
     * {@link OutputGenerator} should handle user interaction.
     * <p>
     * Useful when more information is needed to answer the user's
     * original question.
     *
     * TODO when a lambda assistance component is added, list it here
     * @return a list of {@link AssistanceComponent}s, if needed
     */
    Optional<List<AssistanceComponent>> nextAssistanceComponents();
}
