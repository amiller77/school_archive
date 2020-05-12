/**
*Author: Alexander Miller
*Assignment: Homework 3
*Description: this is the JS file for a website with various buttons
*which allow the user to guess a number between certain user-provided
*parameters.
*This file generates auto-numbers between the user-given parameters,
*wipes fields, and performs logic to evaluate user input.
*/
"use strict";
(function(){
  // need this global variable to store the random number, so runGuessButton can access it
  let randomNumber;

  /**
  * startUp
  * Description: performs our opening operations, attributing event-handlers
  * to the necessary buttons and setting default values
  */
  function startUp() {
    console.log("running STARTUP...");
    // pull our buttons, assign their event-handling functions
    let startButton = document.getElementById("startButton");
    let guessButton = document.getElementById("guessButton");
    startButton.onclick=runStartButton;
    guessButton.onclick=runGuessButton;
    setDefaults();
    console.log("quitting STARTUP");
  }

  /**
  * setDefaults
  * Description: create defaults for max and min values at start up
  */
  function setDefaults() {
    console.log("setting default values...");
    let minBox = document.getElementById("minimum");
    minBox.value = "1";
    let maxBox = document.getElementById("maximum");
    maxBox.value = "10";
  }

  /**
  * runStartButton
  * Role: event-handler for startButton
  * Description: calls wipeScreen() and generateRandomNumber() to wipe the screen and
  * generate a random number.
  */
  function runStartButton() {
    console.log("running START BUTTON");
    wipeScreen();
    generateRandomNumber();
    console.log("quitting START BUTTON");
  }

  /**
  * runGuessButton
  * Role: event-handler for guessButton
  * Description: validates user input, calls printFeedback() to print out Feedback
  * to the user based on the values of that input, performs related logic
  */
  function runGuessButton() {
    console.log("running GUESS BUTTON...");
    // validate existence of random number
    if ((randomNumber!=undefined)&&(randomNumber!=null)) {
      let inputNumber = parseInt(document.getElementById("guess").value);
      if (inputNumber===randomNumber) {
        printFeedback("you got it right!");
      } else if (inputNumber < randomNumber) {
        printFeedback("more than "+inputNumber);
      } else if (inputNumber > randomNumber) {
        printFeedback("less than "+inputNumber);
      }
      // otherwise some strange type thing has happened: ignore
    }
    console.log("quitting GUESS BUTTON");
  }


  /**
  * wipeScreen
  * Description: clears fields all fields but max and min, wipe randomNumber
  */
  function wipeScreen() {
    console.log("wiping screen...");
    // clear output and guess:
    document.getElementById("output").innerHTML="";
    document.getElementById("guess").value="";
    // wipe randomNumber:
    randomNumber=null;
  }

  /**
  * printFeedback
  * Description: printing function, which takes a string argument, and prints
  * it to the user output area
  */
  function printFeedback(argument) {
    console.log("printing Feedback: "+argument);
    let paragraph = document.getElementById("output");
    paragraph.innerHTML = argument + "</br>"+paragraph.innerHTML;

  }

  /**
  * generateRandomNumber
  * Description: pulls input numbers for min and max, scales a randomly generated
  * number by the length of the interval, and adds it to the min, to get a
  * randomly-generated number in the appropriate range with the same distribution
  */
  function generateRandomNumber() {
    console.log("generating random number...");
    let minBound = parseInt(document.getElementById("minimum").value);
    let maxBound = parseInt(document.getElementById("maximum").value);
    let interval = maxBound - minBound;
    // scale the random value by the interval, then add to minimum
    randomNumber = Math.floor(Math.random()*interval) + minBound;
    console.log("random number: "+randomNumber);
    return randomNumber;
  }

  window.onload=startUp;
})();
