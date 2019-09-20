package com.dicio.component;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.OutputGenerator;

/**
 * An {@link AssistanceComponent} processes input and
 * provides a corresponding output. This class serves the
 * only purpose of tying together the {@link InputRecognizer}
 * and {@link OutputGenerator} interfaces, but any class
 * implementing both should be considered a component.
 */
public interface AssistanceComponent extends InputRecognizer, OutputGenerator {}
