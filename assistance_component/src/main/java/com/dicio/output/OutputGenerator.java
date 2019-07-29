package com.dicio.output;

import com.dicio.AssistanceComponent;
import com.dicio.output.constructs.BaseConstruct;

import java.util.List;
import java.util.Optional;

public interface OutputGenerator {
    BaseConstruct getOutput();
    Optional<OutputGenerator> nextOutputGenerator();
    Optional<List<AssistanceComponent>> nextAssistanceComponents();
}
