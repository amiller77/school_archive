/**
* Author: Alexander Miller
* File: speedreader.js
* Description: JS file for speedreader
* Functionality: makes buttons work, allows for toggling speed and font size,
* generates program output based on user input
*/
"use strict";

(function () {

  // PACKAGE GLOBAL VARIABLES
  let interval; // the time interval, w/ default value 171
  let timerID; // the interval id variable
  let contentArray; // the body of text in the textarea, saved into JS
  /*
  * note: contentArray is global, so that start can resume from where it
  * left off if stop is called
  */
  let PUNCTUATION = ["'","\"","\\",";",":",",",".","<",">","?","[",
  "]","{","}","|","/",")","(","*","-","!"];

  /**
  * START UP
  * Description: associates event-handlers with buttons and menus
  */
  function startup() {
    console.log("starting up...");
    // stop button setup
    let stopButton = document.getElementById("stop");
    stopButton.onclick = stop;
    enableElement("stop",false);
    // start button setup
    let startButton = document.getElementById("start");
    startButton.onclick=start;
    // speed setup
    let speedSelect = document.getElementById("speed");
    speedSelect.onchange=speed;
    // size setup
    let mediumSelect = document.getElementById("medium");
    let bigSelect = document.getElementById("big");
    let biggerSelect = document.getElementById("bigger");
    mediumSelect.onclick = size;
    bigSelect.onclick=size;
    biggerSelect.onclick=size;
    console.log("set up.");
  }

  // EVENT-HANDLERS

  /**
  * START
  * Description: runs when start is clicked
  * Functionality: disables start button and textarea, enables stop buttons
  * calls GATHER CONTENTS to pull input, calls the interval to operate
  */
  function start() {
    console.log("running start button...");
    // disable the start button, enable stop, disable textarea
    enableElement("start",false);
    enableElement("stop",true);
    enableElement("textarea",false);
    // gather input, validate, etc.
    let operate = gatherContents();
    // validate:
    if (operate == false) {
      return;
    }
    // find interval, and set and run timer
    let speedSelect = document.getElementById("speed");
    interval = speedSelect.value;
    timerID = setInterval(iterate,interval);
    console.log("finished start button");
  }

  /**
  * GATHER CONTENTS
  * if the textarea is nonempty, pull in, split on whitespace,
  * trim off terminal punctuation, double duration for those words
  * returns the signal on if start should continue running or not
  */
  function gatherContents() {
    console.log("gathering contents...");
    let textArea = document.getElementById("textarea");
    // return if input empty
    if (textArea.value==="") {
      console.log("no contents to gather.");
      return false;
    }
    let contents=textArea.value;
    contentArray = contents.split(/[ \t\n]+/);
    // iterate over words, if terminal index is in punctuation,
    // then insert the word again, after stripping
    // note: using a while loop here, so I can modify i in the body
    let i = 0;
    while (i<contentArray.length) {
      i = checkPunctuation(i);
    }
    console.log("contents gathered and validated.");
    return true;
  }

  /**
  * CHECK PUNCTUATION - SUBFUNCTION FOR GATHER CONTENTS
  * Description: checks last index of string against the PUNCTUATION constant
  * if it ends in punctuation, remove the puncutation, then insert the string
  * a second time in order to double time
  * Iterates and returns loop iterator, i, based on operation performed
  */
  function checkPunctuation(i) {
    let word = contentArray[i];
    for (let j=0; j<PUNCTUATION.length; j++) {
      // if terminal char is punctuation
      if (word[word.length-1]==PUNCTUATION[j]) {
        console.log("found punctuation match for "+word);
        // strip the char, replace original, and add the word again to the array
        let newWord = word.substring(0,word.length-1);
        contentArray[i]=newWord;
        contentArray.splice(i+1,0,newWord);
        return i+2;// moves us past the inserted word
      }
    }
    return i+1;
  }

  /**
  * ITERATE
  * executes if there's still contents to run on, else calls STOP
  * pushes the next word to display, as called by the interval
  */
  function iterate() {
    let displayDiv = document.getElementById("display");
    // only execute if there's stuff to execute on, else stop
    if (contentArray.length != 0) {
      let word = contentArray.shift();
      displayDiv.innerHTML = word;
    } else {
      stop();
    }
  }

  /**
  * STOP
  * disables stop button, enables start button and textarea
  * wipes the display
  * kills the timer
  */
  function stop() {
    console.log("running stop button...");
    // disable the stop button, enable start, enable textarea
    enableElement("stop",false);
    enableElement("start",true);
    enableElement("textarea",true);
    // wipe the displayDiv
    let displayDiv = document.getElementById("display");
    displayDiv.innerHTML = "";
    // kill the interval
    clearInterval(timerID);
    console.log("finished stop button");
  }

  /**
  * SPEED
  * sets a new interval speed, based on value of drop-down
  */
  function speed() {
    console.log("running speed...");
    interval = this.value;
    console.log("finished speed");
  }

  /**
  * SIZE
  * selects new display font size based on radio selection
  */
  function size() {
    console.log("running size...");
    let displayDiv = document.getElementById("display");
    displayDiv.style.fontSize=this.value+"pt";
    console.log("finished size");
  }

  // LOGISTICAL FUNCTIONS:

  /**
  * ENABLE ELEMENT
  * takes an element name and a boolean parameter
  * enables element if true, else -> disables and grays out
  */
  function enableElement(elementName,bool) {
    console.log("Enabling element: "+elementName+" - "+bool);
    let element = document.getElementById(elementName);
    element.disabled=!bool;
    if (bool) {
      element.style.backgroundColor="white";
    } else {
      element.style.backgroundColor="lightgray";
    }
  }


  window.onload = startup;

})();
