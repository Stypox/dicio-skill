package com.dicio.component;

/**
 * Processes the data from the previous step of computation to produce a result to be passed to the
 * next step. It is made for intermediate calculations, to connect to the internet and extract
 * things, etc.
 * Even though everything could be done in an {@link InputRecognizer}, it is better to keep things
 * separate, so that {@link InputRecognizer}'s only purpose is to collect information from user
 * input.
 * @param <FromType> the type of the data from the previous step of computation (i.e. the input)
 * @param <ResultType> the type of the processed and returned data (i.e. the output)
 */
public interface IntermediateProcessor<FromType, ResultType> {

    /**
     * Processes the data obtained from the previous step to produce a result to be passed to the
     * next step. To be used to make calculations, to connect to the internet and extract things,
     * etc.
     * @param data the data to process
     * @return the result of the data processing
     */
    ResultType process(FromType data);
}
