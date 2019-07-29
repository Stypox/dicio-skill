package com.dicio.output;

import com.dicio.AssistanceComponent;
import com.dicio.output.constructs.BaseConstruct;

import java.util.List;
import java.util.Optional;

public interface OutputGenerator {
    /**
     * Calculates the output to be displayed or spoken.
     * @return output
     */
    BaseConstruct getOutput();

    /**
     * If the output provided by {@link #getOutput()} was partial,
     * returns another {@link OutputGenerator}.<br>
     * Useful to try to generate the output in different ways and
     * give feedback about the process.
     * @return another {@link OutputGenerator}, if needed
     */
    Optional<OutputGenerator> nextOutputGenerator();

    /**
     * If the output provided by {@link #getOutput()} contained a
     * question for the user, returns a list of
     * {@link AssistanceComponent}s to handle
     * the answer and generate more output.<br>
     * Useful when more information is needed to answer the user's
     * original question.
     * @return a list of {@link AssistanceComponent}s, if needed
     */
    Optional<List<AssistanceComponent>> nextAssistanceComponents();
}
