package com.dicio.component;

import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.OutputGenerator;

/**
 * An {@link AssistanceComponent} processes input and
 * provides a corresponding output. This class serves the
 * only purpose of tying together the {@link InputRecognizer}
 * and {@link OutputGenerator} interfaces. It has to be
 * implemented by any class pretending to be a component that
 * provides both input recognition and output generation.
 * <br><br>
 * Extending a class that implements {@link InputRecognizer}
 * or {@link OutputGenerator} while also implementing
 * {@link AssistanceComponent} is supported by Java.<br>
 * For example:
 * <pre>
 * {@code
 * class MyComponent extends StandardRecognizer implements AssistanceComponent
 * }
 * </pre>
 */
public interface AssistanceComponent extends InputRecognizer, OutputGenerator {}
