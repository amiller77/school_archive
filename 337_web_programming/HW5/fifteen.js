/**
* fifteen.js
* @author Alexander Miller
* this Javascript file creates tiles, stylizes them, adds them to the page,
* handles events for hovering over and clicking on tiles, and provides mechanism
* to shuffle the board.
*/
"use strict";

( function() {

  // CONSTANTS
  let BGPICCOORDINATES = ["0px 0px","-100px 0px","-200px 0px","-300px 0px",
    "0px -100px", "-100px -100px","-200px -100px","-300px -100px",
    "0px -200px","-100px -200px","-200px -200px","-300px -200px",
    "0px -300px","-100px -300px","-200px -300px"];

  // EVENT-HANDLING VARIABLES
  let tileValidator = false;
  let tileIndex=null;
  let emptyIndex=null;

  /**
  * START UP
  * calls TILE SETUP to construct tiles
  * sets event handlers for the tiles, and the button
  */
  function startup () {
    console.log("Starting up...");
    // set up tiles:
    let puzzleArea = document.getElementById("puzzlearea");
    tileSetup(puzzleArea);
    // add event handlers to tiles: these are offset by 3, due to 3
    // intial non-tile children of the puzzle area
    let tiles = puzzleArea.childNodes;
    for (let i = 3; i<19; i++) {
      let tile = tiles[i];
      tile.onmouseover = hover;
      tile.onmouseout = exit;
      tile.onclick = push;
    }
    // add event-handler for button
    document.getElementById("shufflebutton").onclick=shuffle;
    console.log("Start up finished.");
  }

  /**
  * TILE SETUP
  * creates and stylizes the tiles, adds them to the background in a grid pattern
  * @param puzzleArea the puzzle area to which items will be appended
  */
  function tileSetup(puzzleArea) {
    console.log("Setting up tiles...");
    // configure the tileSetup
    puzzleArea.style.display="grid";
    puzzleArea.style.gridTemplate="repeat(4,1fr) / repeat(4,1fr)";
    // create the tiles, set style, and location
    for (let i = 0; i<15; i++) {
      // create the tile:
      let tile = document.createElement("div");
      tile.className="tile";
      // stylize the div:
      tile.innerHTML=i+1;
      tile.style.width="90px";
      tile.style.height="90px";
      tile.style.border="5px solid black";
      tile.style.fontSize="40pt";
      tile.style.color="rgb(155,0,155)";
      // add the div:
      puzzleArea.appendChild(tile);
      // place background image:
      tile.style.backgroundImage="url(\"background.jpg\")";
      tile.style.backgroundPosition=BGPICCOORDINATES[i];
    }
    // add an empty tile
    let tile = document.createElement("div");
    tile.className="tile";
    tile.id="empty";
    tile.style.width="100px";
    tile.style.height="100px";
    puzzleArea.appendChild(tile);
    console.log("Tiles set up.");
  }

  // ***** EVENT HANDLERS *****

  /**
  * HOVER ~ TILE EVENT HANDLER
  * handles hover effects, and also validates the tile for PUSH
  */
  function hover() {
    // validate the tile it was called on:
    validateTile(this);
    // if the tile is valid, color the border and number red, change cursor
    if (tileValidator) {
      this.style.color="red";
      this.style.borderColor="red";
      this.style.cursor="pointer";
    }
  }

  /**
  * EXIT ~ TILE EVENT HANDLERS
  * turns off tile validation, restylizes the tile to defaults
  */
  function exit() {
    tileValidator=false;
    this.style.color="rgb(155,0,155)";
    this.style.borderColor="black";
    this.style.cursor="default";
  }

  /**
  * PUSH ~ TILE EVENT HANDLER
  * note: requires prior validation by HOVER to function correctly
  */
  function push() {
    console.log("Pushing...");
    if (!tileValidator) {
      console.log("Push invalid.");
      return;
    }
    let puzzleArea = document.getElementById("puzzlearea");
    let tiles = puzzleArea.childNodes;
    console.log("Swapping: \""+tiles[tileIndex].innerHTML+
    "\" with \""+tiles[emptyIndex].innerHTML+"\"");
    // swap out the empty tile and the other tile, both with placeholder divs
    // which keep the lengths of the array constant, so that relative positions don't change
    let tileBuffer = document.createElement("div");
    tileBuffer.id="tileBuffer";
    let emptyBuffer = document.createElement("div");
    emptyBuffer.id="emptyBuffer";
    puzzleArea.insertBefore(emptyBuffer,tiles[emptyIndex]);
    let emptyTile = puzzleArea.removeChild(tiles[emptyIndex+1]);
    puzzleArea.insertBefore(tileBuffer,tiles[tileIndex]);
    let tile = puzzleArea.removeChild(tiles[tileIndex+1]);
    puzzleArea.replaceChild(emptyTile,tiles[tileIndex]);
    puzzleArea.replaceChild(tile,tiles[emptyIndex]);
  }

  // ****** HELPER FUNCTIONS ***** //
  /**
  * VALIDATE TILE
  * flips tileValidator based on whether or not the tile is valid for a swap
  * sets the module global variables tileIndex and emptyIndex
  * @param tile , takes a tile to be validated
  */
  function validateTile(tile) {
    console.log("Validating tile...");
    // the first 3 children of puzzleArea are not tiles and have no innerHTML
    // also, the empty tile has no innerHTML and is invalid for
    if (tile.innerHTML == undefined || tile.innerHTML=="") {
      tileValidator = false;
      console.log("Validate: false.");
      return;
    }
    // consider the children of puzzle area to compare against:
    let tiles = document.getElementById("puzzlearea").childNodes;
    tileIndex = null;
    // iterate over tiles, find tile by matching innerHTML
    for (let i = 3; i<19; i++) {
      if (tiles[i].innerHTML == tile.innerHTML) {
        tileIndex=i;
      }
    }
    if (tileIndex == null) {
      console.log("Error: tile not found.");
      return;
    }
    // left, right, top, bottom:
    let adjacentTiles = [tileIndex-1,tileIndex+1,tileIndex-4,tileIndex+4];
    // iterate over the neighboring tiles:
    for (let i = 0; i<adjacentTiles.length; i++) {
      // only look at valid coordinates:
      if ((adjacentTiles[i] < 19) && (adjacentTiles[i]>2)) {
        // if next to empty tile, then we're good:
        if (tiles[adjacentTiles[i]].id=="empty") {
          tileValidator=true;
          console.log("Validate: true.");
          emptyIndex = adjacentTiles[i];
          return;
        }
      }
    }
    tileValidator=false;
    console.log("Validate: false.");
  }

  /**
  * SHUFFLE
  * makes 1000 random, but valid moves, in order to shuffle the board
  */
  function shuffle () {
    console.log("Shuffling...");
    let tiles = document.getElementById("puzzlearea").childNodes;
    for (let i =0; i<1000; i++) {
      // find empty tile:
      emptyIndex = null;
      for (let j =3; j<19; j++) {
        if (tiles[j].id == "empty") {
          emptyIndex = j;
        }
      }
      // get adjacent tiles, by first finding indices, then validating them
      let adjacentIndices = [emptyIndex-1,emptyIndex+1,emptyIndex-4,emptyIndex+4];
      let adjacentTiles =[];
      let i =0;
      for (let k=0; k<adjacentIndices.length; k++) {
        if (adjacentIndices[k]<19 && adjacentIndices[k]>2) {
          adjacentTiles[i]=adjacentIndices[k];
          i++;
        }
      }
      let randomNeighbor = Math.floor(Math.random()*adjacentTiles.length);
      // validateTile (updates the tile and emptyTile indices for push), then push
      validateTile(tiles[adjacentTiles[randomNeighbor]]);
      push();
    }
    console.log("Deck shuffled.");
  }


  window.onload=startup;

})();
