package com.dicio.component;

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
