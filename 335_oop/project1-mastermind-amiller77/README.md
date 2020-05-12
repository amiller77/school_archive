# Mastermind

Your first project is to implement the game of Mastermind. In this game, the computer chooses 4 pegs each with one of 6 colors.  The player's job is then to guess the colors that the computer has chosen in the proper order. After each guess by the player, if the player's guess is not correct, the computer will give two numbers as feedback.  The first number is how many pegs are the proper color and in the proper position. The second number is how many pegs are the proper color, but not in the correct position.

The game ends when the color string is correct -- and the player wins -- or they give 10 incorrect guesses -- and they lose.
What you need to do:

- Generate a random computer guess of four colors out of:
     - **R**ed, **O**range, **Y**ellow, **G**reen, **B**lue, **P**urple
- Read a guess from the user as a string of colors
- Score the guess, and report the two values back to the user
- Allow the user to continue to guess until they get it correct, or reach 10 turns and they lose.
- Allow the user to play the game multiple times

**Example:**

```
Welcome to Mastermind!
Would you like to play? yes

Enter guess number 1: rrrr
Colors in the correct place: 1
Colors correct but in wrong position: 0

Enter guess number 2:
...
```

## Model/View/Controller

A common pattern for writing programs is known as MVC (Model/View/Controller). This pattern applies whenever we have some problem we can model (like a game board) and a user interface that displays and interacts with that model (the view). The controller is the code that manipulates the model in response to actions from the view.

The idea of MVC is that each part of the program is sufficiently abstracted from each other that they can change without needing to modify the other parts. For instance, our view is currently a textual interface, but later in the course, we could alter this to be a graphical user interface. If we did that, ideally we would not need to change the model or the controller, only the view code.

For us, the implementation of the model is very simple: either an array or String of colors (ints, chars, etc.) that represents the randomly-chosen colors we are trying to guess.

The view is a simple text-based program as we've written many times before. It will prompt the user for their guesses and display if the guess is correct, or show the two statistics that we must calculate.

The controller links these two things together. We will then make three classes:

1.    A main class (named Mastermind) that serves as our view, creates the Model and Controller, and deals with user input and output.
2.    A model class (named MastermindModel) that stores the representation of the computer's guess and uses a constructor and accessors to create and query the solution the player is trying to guess, defined as follows:

```Java
class MastermindModel {
	//private variable(s) to store the answer
     
	public MastermindModel() { /* Make the answer */ }
     
	public char getColorAt(int index) {
		/* Return color at position index as a char
		(first converted if stored as a number) */
	}
}
```
 
3. A controller class named (MastermindController) that is defined as follows:

```Java
class MastermindController {

	public MastermindController(MastermindModel model) { ... }

	public boolean isCorrect(String guess) { ... }
 
	public int getRightColorRightPlace(String guess) { ... }

	public int getRightColorWrongPlace(String guess) { ... }

}
```
 

You are to provide the implementation of all three classes, but you must define your controller and model using at least the methods above. Any additional methods or fields you want to add must be private to your classes.

## Unit Testing

As part of the starter code, you'll notice a ``MastermindTest`` class. This class contains a number of unit test stubs, whose job it is to ensure that your controller logic is working. In Test Driven Design (TDD), we write a very small number of test cases (start with just one) that fails and then we fix the code so that the test then passes. Your submission will not be graded on the test cases that you submit. It will be graded on the correctness of your program, so making good test cases that expose the bugs of your logic will mean that your program works -- and you'll get a good grade.

However, we will grade your submitted test cases and count them as a lab grade.

## Hints and Notes

* For right color, wrong place, you will need to not count colors from the guess that are the right color in the right place.
* You also need to avoid double counting a color as being in the wrong position
* To help you solve these issues, you may find it necessary to make some auxiliary arrays that keep track of what you have used already
* You may want to play the game on paper where you consider various guesses and solutions and score them to see the issue. Then turn these into your JUnit test cases.

* The perfect player can always win this game in 7 guesses or less.

## Submission

Make sure to periodically commit and push your changes to github. 

We will grade the last commit that was pushed prior to the deadline.

 
