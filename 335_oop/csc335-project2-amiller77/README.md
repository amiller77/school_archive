# CSC 335 -- Exceptions and Documentation

For your next project, you will solve the issues that many of you pointed out via your questions: What should we do when the parameters to our methods are invalid?

## Exceptions

The Java mechanism to handle issues of data integrity at runtime is through Exceptions. We want to add to our code base the rejection of any guess or answer that violates our constraints. We also want to enforce programmatically that people use our methods to ask for valid data, and do not do things like ask for the 6th color of our guess.

Java already provides several exception classes that we could use, such as ``IllegalArgumentException`` and ``IndexOutOfBoundsException``. The ``IllegalArugmentException`` is very broad however, and only via its message can we distinguish what the cause of the exception might have been. So we want to create two new exceptions:
1. ``MastermindIllegalColorException``
2. ``MastermindIllegalLengthException``
That should capture the case that a method was provided with a guess or answer that contained a color not part of our original six colors (``MastermindIllegalColorException``). The ``MastermindIllegalLengthException`` should be thrown if a guess or answer is not 4 colors long.

These should be checked exceptions, and the Mastermind class should handle the exceptions to enforce that the user's input is always legal. If it is not, display a message indicating why the input was illegal and ask them to try again. Do **not** count the invalid guess as one of the 10 guesses of the game.

For any other issues your code may encounter at runtime, use one of the standard Java Class Library Exceptions to report it. As part of your testing, you should add new test cases to your JUnit test suite that check that invalid input results in exceptions being thrown. Use the ``assertThrows()`` assertion to test. 

The first project was graded based on a cooperative user. This project will be graded attempting to break your methods. 

## Documentation 
We also want to take the time to fully document our program. We have looked at three ways to document in this course:
1. Javadoc comments
2. UML Diagrams
3. JUnit test cases

We have started this process by having comments and JUnit test cases, but we can do both of these more formally. We also can document our design via a UML diagram. 

### JavaDoc

For this assignment, you are required to use javadoc comments for every class and method to describe their purpose (in a short initial sentence), their design and high-level implementation choices (in a following paragraph), and then to document the way to call our methods, using at minimum the @param, @returns, and @throws tags where necessary.

As part of your submission, you should include a docs/ folder that shows the generated HTML from your javadoc comments

### UML Diagram

We can use UML in two parts of the development process. First, we can use it to plan our design before we write any code at all. Additionally, we can use it to document what we created after the fact. For this project, we will document the design of our Mastermind Game using a UML Class Diagram to model both the classes of our program and their relationships. We do not need to diagram any of the classes from the Java Class Library.

To draw your UML diagram, please use the diagramming tool http://www.draw.io When you are done, save it to your Device as MastermindUML.xml and add it to your git repository. Make sure it is there as part of your final commit to be graded.

## This repository

Do note that this repository is empty of Java code or an Eclipse project. Add your code from Project 1 to your repository and commit. Then modify your code according to the assignment. Remember to periodically commit your changes.

## Submission

The last commit of your code to GitHub prior to the deadline will be your graded submission.
