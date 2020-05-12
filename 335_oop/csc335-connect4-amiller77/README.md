# CSC 335: Connect 4

Connect 4 is a tic-tac-toe based two-player game. In the physical game, there are two colors
of tokens, yellow for the first player and red for the second. Each player takes turns 
dropping a token into an upright board which only allows for dropping your token on top of 
column (as in a stack), reducing the freedom on each turn from that of traditional tic tac toe.

As the name implies, you win when you create a connected line of 4 of your tokens, either
horizontally, vertically, or diagonally. Games can end in a tie if neither player makes a line
of 4 connected tokens.

For more information on the game, you can see <https://en.wikipedia.org/wiki/Connect_Four>

## Implementation

Your job for this project is to implement the game of Connect 4 using all of the tools we have
learned so far. That means you should follow the MVC design, use UML to plan and document your
class structure, provide full source code documentation using ``javadoc``, a complete test suite
that tests your controller to 100% branch coverage, and, of course, works.

Rather than using red and yellow colors, we will represent the human player as an X, the computer
player as an O, and a blank as an underscore ( _ ).  

The board will be 6 rows high by 7 columns wide. 

The computer will be a random player, with no intelligence. Have it choose a legal move randomly.

A run of the program will look like:

```

Welcome to Connect 4

_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
0 1 2 3 4 5 6 

You are X

What column would you like to place your token in?
2
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ X _ _ _ _ 
0 1 2 3 4 5 6 

_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
O _ X _ _ _ _ 
0 1 2 3 4 5 6 

What column would you like to place your token in?
3

_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
O _ X X _ _ _ 
0 1 2 3 4 5 6 

_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
O _ X X _ O _ 
0 1 2 3 4 5 6 

What column would you like to place your token in?
2

...

_ _ _ _ _ _ _ 
_ _ _ _ _ _ _ 
_ _ X _ _ _ _ 
_ _ X _ _ _ _ 
O _ X _ _ O _ 
O _ X X _ O _ 
0 1 2 3 4 5 6 

_ _ _ _ _ _ _ 
_ _ O _ _ _ _ 
_ _ X _ _ _ _ 
_ _ X _ _ _ _ 
O _ X _ _ O _ 
O _ X X _ O _ 
0 1 2 3 4 5 6 

You win!

```

## Requirements

As part of your submission, you must provide:

- At least four classes, Connect4, Connect4View, Connect4Controller, and Connect4Model. The Connect4 class will have ``main``, separating
it from the view this time. You may have as many additional classes as you need.

- Complete javadoc for every class and method, using the ``@author, @param, @return,`` and ``@throws`` javadoc tags. Generate
your documentation into a docs folder.

- A complete UML diagram for your design, drawn using <http://draw.io> and the xml file committed as part of your repository

- Test cases for your model and controller with 100% branch coverage

Your code must follow the MVC architecture as we have described it in class. That means:

- No input or output code except in the View

- A model that represents the state of the game but guards access through public methods

- A controller that allows the view to interact indirectly with the model, providing the abstracted operations of your game
    - Including a ``humanTurn(int row)`` method and a ``computerTurn()`` method that represent the turns
    - A set of methods that determine the end of game and the winner
    - Some way to access the board (not via the model) to be able to display it as part of the view
    - As few public methods as possible, with helper methods being private and all non-final fields being private
    
- Your UML diagram should be used to plan out the program and will be a Section Lab grade taken as part of your final submission

  
## Submission

Make sure to periodically commit and push your changes to github. 

We will grade the last commit that was pushed prior to the deadline.

