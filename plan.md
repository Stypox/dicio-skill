# Components

## Input Recognizer <ResultType> (exactly 1)

Produces a result based on the input.

- Specificity specificity()
- void setInput()
- float score()
- ResultType getResult()

## Intermediate Processor <FromType, ResultType> (0+)

Processes the data obtained from the previous step to produce a result to be passed to the next step.
To be used to connect to the internet and extract things, etc.

- ResultType manipulate(FromType)

## Output Generator <FromType> (exactly 1)

Hosts platform-specific code to produce platform-specific output based on the data obtained from the previous step.
Do here all platform-specific things (e.g file accessing, preference handling, etc.).

*(in Android implementation)*
- void generate(FromType, Context, OutputHandler)

## Usage
*(in Android implementation)*

```java
AssistanceComponent component = new ComponentBuilder()
    .recognize(ir)
    .manipulate(im1)
    .manipulate(im2)
    .manipulate(im3)
    .output(oe);
```