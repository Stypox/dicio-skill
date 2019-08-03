# Assistance component for Dicio assistant
This tool provides an interface to create **multiplatform assistance components**. An assistant component is something that tries to provide an **answer to a request** from the user. It provides a fitness score based on user input (i.e. is it a query this component is able to answer?) and generates speech and graphical output, when required to.

## Input recognizer
The `InputRecognizer` interface **processes input** from the user and determines if it **matches a given set of** (*custom*) **rules**, calculating a fitness score. The score is a value ranging from `0.0` to `1.0`, where 0 means "it does not match at all" and 1 means "it matches perfectly". Values between `0.85` and `1.0` usually describe a good match, while values below `0.7` are considered a bad match.  

Every input recognizer has a specificity (`low`, `medium` or `high`) that represents **how specific the set of rules is**. E.g. one that matches only queries about phone calls is very specific, while one that matches every question about famous people has a lower specificity. The specificity is needed to prevent conflicts between two components that both match with an high score: the most specific is preferred.

## Output generator
The `OutputGenerator` interface calculates and generates **graphical and speech output**. It has access to its corresponding `InputRecognizer` so that it *can* get informations about the input from there. While calculating the output, it is ok for an output generator to make **internet requests**, **access files/apps** on the user's device... Once it is done calculating it has to provide a string/sentence to be spoken to the user by voice. It can also optionally generate a list of views to display to the user (e.g. an image, search results...).  

Every view in the graphical output can be one of:
- `Header`: rendered to **big-sized text**. Useful as a title.
- `Description`: rendered to **small-sized text**. Useful as subtitle/description to add further information to something. Basic html is supported.
- `Image`: an **image** with online or local source.
- `DescribedImage`: an item that contains a **header** and a **description** on one side of the screen and an **image** on the other, similarly to contact views in messaging apps. Useful to provide search results with an image, display contacts...

## Assistance component
The `AssistanceComponent` interface is responsible for both input and output. It has basically the merged functionalities of the **input recognizer** and of the **output generator**. The `TieInputOutput` class is useful to create an assistance component from an `InputRecognizer` and an `OutputGenerator`.

## Standard input recognizer
The `StandardRecognizer` class implements `InputRecognizer` by providing a simple way to calculate the fitness score based on a list of sentences to match. E.g. `what is the weather` matches: itself with maximum score; `what is the weather like` with high score; `what is he doing` with low score; `how are you` with minimum score.  

Every `Sentence` has a **sentence id** accessible to the output generator, useful to answer the question with the most apprepriate language.  
The standard recognizer also supports up to two **capturing groups**, i.e. a variable-length list of any word that is captured to be later processed by the output generator. E.g. `how are you <CAPTURING_GROUP>` matches `how are you Tom` with maximum score and `Tom` is captured in the first capturing group.  

Use [dicio-sentences-compiler](https://github.com/Stypox/dicio-sentences-compiler) to generate the sentences for an input recognizer in a simple and fast way. It enables to pack sentences toghether, e.g. `hello|hi how (are you doing?)|(is it going)` unfolds to `hello how are you`, `hello how are you doing`, `hello how is it going`, `hi how are you`, `hi how are you doing`, `hi how is it going`. See? Six sentences have been packed into one!