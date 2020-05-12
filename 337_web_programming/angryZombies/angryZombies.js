
/*
* File: angryZombies.js
* Author: Alexander Miller
* Class: CSC337
* Date: 11/29
* Assignment: Final Project
* Description: JS for browser game, angryZombies
* allows game to pause and resume, load levels, display levels, display points,
* updates points, update and display cannon power, shoot and animate cannon, explosions, zombies
* tracks zombies, deletes zombies, verifies user losing level, sends save and load requests
* to server, validates user input
*/

"use strict";
( function () {
  // ************************* VARIABLES ***********************
  /*
  * note: lot of asynchronous things animating at once on the page, so we need a lot of
  * variables to query the current program state at any time
  */
  let ballStartingX; // coordinates to track ball
  let ballStartingY; // coordinates to track ball
  let ballX; // coordinates to track ball
  let ballY; // coordinates to track ball
  let ballRadius; // coordinates to track ball

  let barrelBottomEndX; // cannon barrel data
  let barrelBottomEndY; // cannon barrel data
  let barrelBottomStartX; // cannon barrel data
  let barrelBottomStartY; // cannon barrel data
  let barrelRadius; // cannon barrel data
  let barrelTopEndX; // cannon barrel data
  let barrelTopEndY; // cannon barrel data
  let barrelTopStartX; // cannon barrel data
  let barrelTopStartY; // cannon barrel data

  let boundingCannonBoxHeight; // bounding cannon box (constant)
  let boundingCannonBoxWidth; // bounding cannon box (constant)
  let boundingCannonBoxX; // bounding cannon box (constant)
  let boundingCannonBoxY; // bounding cannon box (constant)

  let cannonCharged;  // record if charging sequence has been terminated
  let cannonFocalRadius; // constant cannon values
  let cannonLimitFocalX; // variable cannon values
  let cannonLimitFocalY; // variable cannon values
  let cannonLowerLimit; // variable cannon values
  let cannonUpperLimit; // variable cannon values

  let cannonPowerTimer; // timing cannon power
  let cannonPowerTimeCounter; // timing cannon power

  let explosionRadius; // variables to render explosion:
  let explosionRadiusMax; // variables to render explosion:
  let explosionRedAngleInitial; // variables to render explosion:
  let explosionYellowAngleInitial; // variables to render explosion:

  let fireTheta; // coordinates to track ball
  // boolean to register that game is over to prevent corner-case buggy event-handling post gameend
  let gameOver;
  let groundLevel; // background constant
  let initialVelocity; // proxy for force of cannon ball (m/s)
  let level; // difficulty/level settings:

  let mouseClicked; // track mouse
  let mouseDown;  // track mouse

  let points; // track user score
  let pointsPerZombie; // track user score (constant)
  let pointsToProgress; // determine quota to move to next level

  let theta; // track angle of mouse

  let timeCounter; // recording time for physics (ball)
  let timeInterval; // timing events (cannon, ball, cannon power)
  let timer; // timer variable for physics (ball)

  let trueCannonFocalX; // constant cannon value
  let trueCannonFocalY; // constant cannon value

  let xCoordinate; // track coordinates of mouse
  let yCoordinate; // track coordinates of mouse

  let zombieAnimationTimer; // timer var for zombie animation
  let zombieArmToArmAngle; // angle between zombie arms
  let zombieArmToSpineAngle;  // angle between right arm and spine
  let zombieArmLength;  // length of zombie arms (constant)
  let zombieArmy; // tracking zombies
  let zombieBoundingWidth;  // bounding width of zombies (constant)
  let zombieBoundingHeight; // bounding height of zombies (constant)
  let zombieBottomRegionSize; // size of leck region (constant)
  let zombieDeltaT; // amount of time per zombie movement frame
  let zombieDeltaX; // amount of distance per zombie movement frame
  let zombieHeadRadius; // radius of zombie head (constant)
  let zombieLegAngle; // angle between zombie legs
  let zombieLegLength;  // length of zombie legs (constant)
  let zombiesMarkedForDeath;  // records which zombies are currently marked for removal
  let zombieMaxLegAngle;  // max angle between zombie legs
  let zombieMaxTranslation; // max length zombie travels during single animation
  let zombieMovementTotalTime;  // total amount of time allowed for zombie horde to move
  let zombieMiddleRegionSize; // size of torso region (constant)
  let zombieNeckAngle;  // mathematical angle (magnitude) from horizontal of neck
  let zombieNeckRadius; // length of zombie neck (constant)
  let zombieTorsoLength;  // length of zombie torso (constant)
  let zombieTopRegionSize;  // size of head/neck region (constant)
  let zombieTranslation;  // distance traveled during this animation
  let zombieSN; // tracking zombies
  let zombieSpeed; // difficulty/level settings:
  let zombieTimer; // timing zombies:


  // *************************************************************
  // ************** INITIALIZERS AND GUIS ************************

  /**
  * START UP
  * assigns constants and starting values, button event-handlers, load level 1
  * functionality rolling
  */
  function startup() {
    // initialize variables and constants:
    initializeVariablesAndConstants();

    // note: canvas event-handlers initialized at level load,
    // s.t. at game end mode, they are inactive
    // set up event-handlers for level buttons
    document.getElementById("level1").onclick=loadLevelOne;
    document.getElementById("level2").onclick=loadLevelTwo;
    document.getElementById("level3").onclick=loadLevelThree;
    document.getElementById("level4").onclick=loadLevelFour;
    document.getElementById("level5").onclick=loadLevelFive;
    // .... for save and load buttons; initialize to disabled:
    let saveButton = document.getElementById("save");
    saveButton.onclick=saveOrLoad;
    saveButton.disabled = true;
    let loadButton = document.getElementById("load");
    loadButton.onclick=saveOrLoad;
    loadButton.disabled = true;

    // set event-handlers for play/pause buttons
    let pauseButton = document.getElementById("pauseButton");
    pauseButton.onclick = pauseGame;
    pauseButton.disabled = true;
    let playButton = document.getElementById("playButton");
    playButton.onclick = resumeGame;
    playButton.disabled = true;
    // load level 1:
    loadLevelOne();
  }

  /**
  * INITIALIZE VARIABLES AND CONSTANTS
  */
  function initializeVariablesAndConstants() {
    let canvas = document.getElementById("canvas");
    // BACKGROUND CONSTANTS:
    groundLevel = canvas.height-50;
    // (initializes cannon orientation to horizontal)
    // CANNON CONSTANTS
    trueCannonFocalX = 50;
    trueCannonFocalY = canvas.height-75;
    cannonFocalRadius = 20;
    barrelRadius=50;
    boundingCannonBoxX=-5+trueCannonFocalX-cannonFocalRadius;
    boundingCannonBoxY=-5+trueCannonFocalY-(cannonFocalRadius+barrelRadius);
    boundingCannonBoxWidth=10+cannonFocalRadius*2+barrelRadius;
    boundingCannonBoxHeight=7+cannonFocalRadius*2+barrelRadius;
    // CANNON HUB VARIABLES
    theta = 0;
    cannonLowerLimit=Math.PI/2;
    cannonUpperLimit=(3*Math.PI)/2;
    cannonLimitFocalX = cannonFocalRadius+trueCannonFocalX;
    cannonLimitFocalY = trueCannonFocalY;
    // BARREL VARIABLES
    // inverting Ys
    barrelBottomStartX = cannonLimitFocalX;
    barrelBottomStartY = cannonLimitFocalY+cannonFocalRadius;
    barrelTopStartX = cannonLimitFocalX;
    barrelTopStartY = cannonLimitFocalY-cannonFocalRadius;
    barrelBottomEndX = barrelBottomStartX+barrelRadius;
    barrelBottomEndY=barrelBottomStartY;
    barrelTopEndX=barrelTopStartX+barrelRadius;
    barrelTopEndY=barrelTopStartY;
    // BALL VARIABLES
    ballRadius = cannonFocalRadius;
    // EXPLOSION VARIABLES
    explosionRadiusMax = 50;
    explosionRedAngleInitial = (Math.PI/6);
    explosionYellowAngleInitial = (Math.PI/12);

    // TIME VARIABLES
    timeInterval=.05;
    // MOUSE VARIABLES
    mouseClicked=false;
    // USER STATS
    points = 0;

    // initialize hit zombie array
    zombiesMarkedForDeath = [];

    // ZOMBIE BODY PROPORTIONS
    // note: these trickle down, s.t. those declared higher above are referenced below
    zombieBoundingWidth = 30;
    zombieBoundingHeight = 50;

    // body proportions:
    zombieTopRegionSize = (1/3)*zombieBoundingHeight;
    zombieMiddleRegionSize = (1/3)*zombieBoundingHeight;
    zombieBottomRegionSize = (1/3)*zombieBoundingHeight;

    // head and neck:
    zombieHeadRadius = (2/5)*zombieTopRegionSize;
    zombieNeckAngle = Math.PI/3;
    // note: neck radius determined by initial neck angle, but constant thereafter
    // HR + sin(NA)*HR + sin(NA)*NR = TRS   ->  NR = [TRS - HR(1+sin(NA))]/sin(NA)
    zombieNeckRadius = (1/Math.sin(zombieNeckAngle))*
      ( zombieTopRegionSize - zombieHeadRadius*(Math.sin(zombieNeckAngle)) );

    // torso and legs:
    zombieTorsoLength= zombieMiddleRegionSize;
    zombieMaxLegAngle = Math.PI/3;
    zombieLegAngle = zombieMaxLegAngle;
    // r*cos(LA/2) = h  ->  r = h/(cos(LA/2))
    zombieLegLength = zombieBottomRegionSize / (Math.cos(zombieLegAngle/2));

    // arms:
    zombieArmToArmAngle = Math.PI/6;
    zombieArmToSpineAngle = Math.PI/6;
    zombieArmLength = zombieLegLength*(2/3);

  }

  /**
  * DRAW BACKGROUND
  */
  function drawBackground() {
    let canvas = document.getElementById("canvas");
    let gc = canvas.getContext("2d");
    // drawing ground:
    gc.strokeStyle="black";
    gc.beginPath();
    gc.lineWidth=1;
    gc.moveTo(0,groundLevel);
    gc.lineTo(canvas.width,groundLevel);
    gc.stroke();
    // drawing the sun:
    gc.beginPath();
    gc.arc(canvas.width-50,50,20,0,Math.PI*2);
    gc.fillStyle="red";
    gc.fill();
    gc.stroke();
  }

  // *******************************************************************
  // ***** LOADING LEVELS, LEVEL/POINT DISPLAYS, GAME ENDING ***********

  /**
  * END GAME
  * screen goes red, loss message displayed, horde disappears
  */
  function endGame() {
    let canvas = document.getElementById("canvas");
    gameOver=true;
    // pause the game (kills canvas event-handlers); freezes pause button
    pauseGame();
    // freeze play button
    document.getElementById("playButton").disabled = true;
    // freeze the load and save buttons
    document.getElementById("save").disabled = true;
    document.getElementById("load").disabled = true;
    // kill the horde:
    killTheHorde();
    // kill ball timer
    clearInterval(timer);
    // clear the screen
    let gc = canvas.getContext("2d");
    gc.clearRect(0,0,canvas.width,canvas.height);
    // color screen red, and give lose message
    gc.beginPath();
    gc.fillStyle="red";
    gc.fillRect(0,0,canvas.width,canvas.height);
    gc.beginPath();
    gc.fillStyle="black";
    gc.font = "100px serif";
    gc.fillText("EATEN, ANGRILY",canvas.width/14,canvas.height/3);
  }

  /**
  * LEVEL LOADERS ~ LOAD LEVEL ONE
  */
  function loadLevelOne () {
    // LEVEL STATS
    level = 1;
    changeLevelPriviledges(1);
    // DIFFICULTY SETTINGS
    zombieSpeed = 1.4; //(smaller is faster)
    pointsPerZombie=50;
    pointsToProgress=500;
    loadLevel();
  }
  /**
  * LEVEL LOADERS ~ LOAD LEVEL TWO
  */
  function loadLevelTwo () {
    // LEVEL STATS
    level = 2;
    // unlock all levels up thru level 5:
    changeLevelPriviledges(2);
    // DIFFICULTY SETTINGS
    zombieSpeed = 1.2; //(smaller is faster)
    pointsPerZombie=100;
    pointsToProgress=1000;
    loadLevel();
  }
  /**
  * LEVEL LOADERS ~ LOAD LEVEL THREE
  */
  function loadLevelThree() {
    // LEVEL STATS
    level = 3;
    // unlock all levels up thru level 3:
    changeLevelPriviledges(3);
    // DIFFICULTY SETTINGS
    zombieSpeed = 1; //(smaller is faster)
    pointsPerZombie=150;
    pointsToProgress=1500;
    loadLevel();
  }
  /**
  * LEVEL LOADERS ~ LOAD LEVEL FOUR
  */
  function loadLevelFour() {
    // LEVEL STATS
    level = 4;
    changeLevelPriviledges(4);
    // DIFFICULTY SETTINGS
    zombieSpeed = .9; //(smaller is faster)
    pointsPerZombie=200;
    pointsToProgress=2000;
    loadLevel();
  }
  /**
  * LEVEL LOADERS ~ LOAD LEVEL FIVE
  */
  function loadLevelFive() {
    // LEVEL STATS
    level = 5;
    changeLevelPriviledges(5);
    // DIFFICULTY SETTINGS
    zombieSpeed = .8; //(smaller is faster)
    pointsPerZombie=250;
    pointsToProgress=2500;
    loadLevel();
  }

  /**
  * LOAD LEVEL
  * handles common ops for all levels:
  */
  function loadLevel() {
    gameOver=false;
    // ZOMBIE MOVEMENT STATS (due to zombieSpeed, level-dependent)
    zombieMaxTranslation = 90;
    // total time for the operation is zombieSpeed*1000,
    // but we use zombieSpeed * 800 to provide a cushion
    zombieMovementTotalTime = zombieSpeed * 800;
    // displacement of zombies per unit time [scaled to reduce burden]
    zombieDeltaX = 5* zombieMaxTranslation / zombieMovementTotalTime;

    // unfreeze the load and save buttons
    document.getElementById("save").disabled = false;
    document.getElementById("load").disabled = false;
    // reset points
    points = 0;
    // display points:
    updatePointDisplay();
    // update level display:
    updateLevelDisplay();
    // kill horde / prep horde
    killTheHorde();
    // reactivates event-handlers for canvas; deactivates play button
    // // call the zombies!!
    resumeGame();
  }

  /**
  * LOAD NEXT LEVEL
  * loads next level, given current level
  */
  function loadNextLevel() {
    if (level<=0) {
      loadLevelOne();
    } else if (level == 1) {
      loadLevelTwo();
    } else if (level == 2) {
      loadLevelThree();
    } else if (level == 3) {
      loadLevelFour();
    } else { //if (level >= 4)
      loadLevelFive();
    }
  }

  /**
  * CHANGE LEVEL PRIVILEDGES
  * locks all higher levels (in case you load to a lower profile)
  * and unlocks all lower levels (in case you load to a higher profile)
  */
  function changeLevelPriviledges(currentLevel) {
    // unlock all levels up thru currentLevel:
    for (let i = 1; i <= currentLevel; i++) {
      document.getElementById("level"+i).disabled=false;
    }
    if (currentLevel != 5) {
      // lock all higher levels
      for (let i = currentLevel+1; i <= 5; i++) {
        document.getElementById("level"+i).disabled=true;
      }
    }
  }

  /**
  * UPDATE LEVEL DISPLAY
  * updates level display on screen
  */
  function updateLevelDisplay() {
    let levelCanvas = document.getElementById("levelCanvas");
    let gc = levelCanvas.getContext("2d");
    // xCushion: provides xCushion px on left
    let xCushion = 20;
    gc.clearRect(0,0,levelCanvas.width,levelCanvas.height);
    gc.beginPath();
    gc.strokeStyle="black";
    gc.fillStyle="red";
    gc.font = '100px serif';
    gc.fillText(level.toString(),xCushion,levelCanvas.height-xCushion);
    gc.strokeText(level.toString(),xCushion,levelCanvas.height-xCushion);
  }

  /**
  * UPDATE POINT DISPLAY
  * updates the point display on the screen
  * note: this is weird because a 90px digit doesn't take up 9/10
  * of a 100px canvas for some reason...
  * so, we scale up the font size by (3/2) arbitrarily to account for this
  */
  function updatePointDisplay() {
    let pointsCanvas = document.getElementById("pointsCanvas");
    let gc = pointsCanvas.getContext("2d");
    // wipe canvas:
    gc.clearRect(0,0,pointsCanvas.width,pointsCanvas.height);
    // calculate font size s.t. the values will fit regardless of digits (within reason)
    // xCushion: provides xCushion/2 px per side
    let xCushion = 10;
    // find number of digits:
    let numDigits = 1;
    let x = points;
    while (x>=10) {
      numDigits++;
      x = x/10;
    }
    // font size = (canvaswidth-xCushion)/numDigits
    let fontSize = (3/2)*((pointsCanvas.width-xCushion)/numDigits);
    // add new value:
    gc.beginPath();
    gc.strokeStyle="black";
    gc.fillStyle="red";
    gc.font = fontSize+'px serif';
    gc.fillText(points.toString(),xCushion/2,pointsCanvas.height-xCushion/2);
    gc.strokeText(points.toString(),xCushion/2,pointsCanvas.height-xCushion/2);
  }

  // *************************************************************
  // ***************** PLAYING AND PAUSING GAME ******************

  /**
  * PAUSE GAME
  * deactivates canvas event-handlers, pause button, register game as paused
  */
  function pauseGame() {
    // register game as paused
    // update pause status
    document.getElementById("pauseStatus").innerHTML = "GAME PAUSED";
    // freeze the horde
    if (zombieTimer != undefined) {
      clearInterval(zombieTimer);
    }
    // kill canvas event-handlers
    let canvas = document.getElementById("canvas");
    canvas.onmouseover = null;
    canvas.onmousemove = null;
    canvas.onmousedown = null;
    canvas.onmouseup = null;
    // freeze pause button, enable play button
    document.getElementById("pauseButton").disabled = true;
    document.getElementById("playButton").disabled = false;

  }

  /**
  * RESUME GAME
  * activate canvas event-handlers, deactivate play button, activate pause button
  * register game as not paused
  */
  function resumeGame() {
    // update pause status
    document.getElementById("pauseStatus").innerHTML = "";
    // reactivate event-handlers for canvas:
    let canvas = document.getElementById("canvas");
    canvas.onmouseover = updateTheta;
    canvas.onmousemove = drawCannon;
    canvas.onmousedown = powerCannon;
    canvas.onmouseup = mouseUp;
    // freeze play button, enable pause button
    document.getElementById("pauseButton").disabled = false;
    document.getElementById("playButton").disabled = true;
    // get the zombies moving again
    callTheHorde();
  }


  // *******************************************************************
  // ***************** SAVING AND LOADING GAMES ************************

  /**
  * SAVE OR LOAD
  * handles common ops to saving and loading
  */
  function saveOrLoad(event) {
    // freeze the game:
    pauseGame();
    document.getElementById("playButton").disabled = true;
    let saveOp; // we here to save or load?
    if (event.target.id == "save") {
      saveOp = true;
    } else { //  if (event.target.id == "load")
      saveOp = false;
    }
    // get user name from input
    let username = getAndValidateUsername();
    // slap the user on the wrist if the username can't be salvaged
    if (username == null ) {
      document.getElementById("loadFeedback").innerHTML =
      "Please enter a name consisting of word characters.";
      // resume game:
      resumeGame();
      return;
    }
    if (saveOp) {
      saveGame(username);
    } else {
      loadGame(username);
    }
  }

  /**
  * GET AND VALIDATE USERNAME
  * validates user input s.t. username only consists of word chars
  * returns null if name was totally invalid
  */
  function getAndValidateUsername() {
    let username = document.getElementById("username").value;
    let validatedCharacters = username.match(/[a-zA-Z0-9_]/g);
    if (validatedCharacters == null) {
      return null;
    }
    let validatedUsername = "";
    for (let i = 0; i< validatedCharacters.length; i++ ) {
      validatedUsername = validatedUsername+validatedCharacters[i];
    }
    return validatedUsername;
  }

  /**
  * SAVE GAME
  */
  function saveGame(paramUsername) {
    // sets up the post parameters
    let paramLevel = level;
    if (level == undefined) {
      paramLevel = 1;
    }
    let message = {username: paramUsername, level: paramLevel};
    let secondParam = {
      method: 'POST',
      headers: {
        'Accept':'application/json',
        'Content-Type':'application/json'
      },
      body: JSON.stringify(message)
    };

    // fetches from server
    fetch("http://localhost:3000",secondParam)
      .then(checkStatus)
      .then(function(responseText){
        document.getElementById("loadFeedback").innerHTML = responseText;
        // resume game:
        resumeGame();
      })
      .catch(function(error) {
        document.getElementById("loadFeedback").innerHTML = error;
        // resume game:
        resumeGame();
      });
  }


  /**
  * LOAD GAME
  */
  function loadGame(username) {
    fetch("http://localhost:3000?username="+username)
      .then(checkStatus)
      .then(function(responseText) {
        document.getElementById("loadFeedback").innerHTML = "Game loaded.";
        // decrement by 1, so that when we call loadNextLevel, it moves us to correct level
        level = parseInt(responseText)-1;
        loadNextLevel();
      })
      .catch(function(error) {
        document.getElementById("loadFeedback").innerHTML = "Username not found: "+error;
        // resume game:
        resumeGame();
        return;
      });
  }

  /**
  * CHECK STATUS
  */
  function checkStatus(response) {
    if (response.status >= 200 && response.status<300) {
      return response.text();
    } else {
      return Promise.reject(new Error(response.status+":"+response.statusText()));
    }
  }


  // ******************************************************
  // ***************** CANNON METHODS *********************

  /**
  * DRAW CANNON
  */
  function drawCannon(event) {
    // update angles based on mouse position
    updateTheta(event);
    // clear the bounding rectangle, redraw cannon stalk
    clearCannon();
    // render cannon
    renderCannon();
  }

  /**
  * RENDER CANNON
  * handles most of the drawing function, but angles have to have been set already
  * called at page load and by drawCannon
  */
  function renderCannon() {
    let canvas = document.getElementById("canvas");
    let gc = canvas.getContext("2d");
    // draw the cannon hub based on theta
    gc.beginPath();
    gc.strokeStyle="black";
    gc.arc(cannonLimitFocalX,cannonLimitFocalY,cannonFocalRadius,cannonLowerLimit,cannonUpperLimit);
    gc.stroke();
    // draw cannon sides:
    // bottom side:
    gc.beginPath();
    gc.moveTo(barrelBottomStartX,barrelBottomStartY);
    gc.lineTo(barrelBottomEndX,barrelBottomEndY);
    gc.stroke();
    // top side:
    gc.beginPath();
    gc.moveTo(barrelTopStartX,barrelTopStartY);
    gc.lineTo(barrelTopEndX,barrelTopEndY);
    gc.stroke();
    // draw mouth:
    gc.beginPath();
    gc.moveTo(barrelTopEndX,barrelTopEndY);
    gc.lineTo(barrelBottomEndX,barrelBottomEndY);
    gc.stroke();
  }

  /**
  * CLEAR CANNON
  * clears the cannon bounding area and redraws cannon stalk
  */
  function clearCannon() {
    // wipe the bounding box:
    let canvas = document.getElementById("canvas");
    let gc=canvas.getContext("2d");
    gc.strokeStyle="black";
    gc.clearRect(boundingCannonBoxX,boundingCannonBoxY,
      boundingCannonBoxWidth,boundingCannonBoxHeight);
    // draw the stalk back:
    // rear stalk:
    gc.beginPath();
    gc.moveTo(trueCannonFocalX/3,groundLevel);
    gc.lineTo(trueCannonFocalX,trueCannonFocalY);
    gc.stroke();
    // vertical stalk:
    gc.beginPath();
    gc.moveTo(trueCannonFocalX,groundLevel);
    gc.lineTo(trueCannonFocalX,trueCannonFocalY);
    gc.stroke();

  }

  /**
  * MOUSE UP
  * registers that mouse has been lifted
  */
  function mouseUp() {
    mouseDown = false;
  }

  /**
  * POWER CANNON
  * adds power to cannon (in form of initial velocity), draws representation to screen
  */
  function powerCannon() {
    // can't power up cannon if ball still in air
    if (mouseClicked) {
      return;
    }
    // register that mouse is pressed:
    mouseDown = true;
    // reinitialize variables:
    initialVelocity = 0;
    cannonPowerTimeCounter = 0;
    // set the timer to run:
    cannonPowerTimer = setInterval(cannonPowerBuilder,timeInterval*1000);
  }

  /**
  * CANNON POWER BUILDER
  * time-dependent helper of powerCannon; scales initial velocity by duration held
  * max cannon power occurs at 1 s
  */
  function cannonPowerBuilder() {
    // power maxes out after 1s
    if (mouseDown && cannonPowerTimeCounter <= 1) {
      cannonPowerTimeCounter = cannonPowerTimeCounter + timeInterval;
      // draw current power to display:
      displayCannonPower();
    } else {
      // end timer:
      clearInterval(cannonPowerTimer);
      // scale our velocity:
      initialVelocity = cannonPowerTimeCounter*400;
      // register that cannon has been charged:
      cannonCharged = true;
      // note: don't clear canvas until cannon fires... (fireCannon)
      fireCannon();
    }
  }

  /**
  * FIRE CANNON
  * shoots cannon -> can only shoot one ball at a time; allowed to shoot another
  * after explosion done animating
  */
  function fireCannon() {
    if (!mouseClicked && cannonCharged && !gameOver) {
      // clear cannon power canvas:
      clearCannonPowerCanvas();
      // register that we're expending the charge:
      cannonCharged=false;
      // reinitialize variables
      timeCounter=0;
      ballX=null;
      ballY=null;
      // saves original trajectory
      fireTheta=Math.abs(theta);
      // (time interval a constant)
      timer = setInterval(drawBall,timeInterval*1000); // converting to MS
      mouseClicked=true;
    }
    // if game over, clear the cannon power up
    else if (gameOver) {
      clearCannonPowerCanvas();
    }
  }

  /**
  * DISPLAY CANNON POWER
  * paints proportion of power display red equal to proportion of current power to max power
  */
  function displayCannonPower() {
    let cannonPowerCanvas = document.getElementById("cannonPowerCanvas");
    let gc = cannonPowerCanvas.getContext("2d");
    // clear the canvas:
    gc.clearRect(0,0,cannonPowerCanvas.width,cannonPowerCanvas.height);
    // find proportion of canvas equal to ratio of current cannon power to max power
    let canvasProportion = cannonPowerTimeCounter*cannonPowerCanvas.height;
    // find upper visual bound (min graphics y bound) to fill:
    let minGraphicsYVal = cannonPowerCanvas.height-canvasProportion;
    // paint:
    gc.beginPath();
    gc.fillStyle="red";
    gc.fillRect(0,minGraphicsYVal,cannonPowerCanvas.width,cannonPowerCanvas.height);
  }

  /**
  * CLEAR CANNON POWER CANVAS
  */
  function clearCannonPowerCanvas() {
    let cannonPowerCanvas = document.getElementById("cannonPowerCanvas");
    let gc = cannonPowerCanvas.getContext("2d");
    gc.clearRect(0,0,cannonPowerCanvas.width,cannonPowerCanvas.height);
  }

  // ******************************************************
  // ***************** PLANE GEOMETRY *********************

  /**
  * UPDATE THETA
  * allows us to track the mouse
  */
  function updateTheta(event) {
    // UPDATING THETA:
    let oldTheta = theta;
    // get latest mouse coordinates
    updateCoordinates(event);
    let canvas = document.getElementById("canvas");
    let trueY = canvas.height-yCoordinate;
    // have to find theta, then invert to use the drawing tool
    if ( (Math.atan(trueY/xCoordinate)<=Math.PI/2) && (Math.atan(trueY/xCoordinate)>=0) ) {
      theta = -1*Math.atan(trueY/xCoordinate);
    } else {
      return;
    }
    // CALCULATING THE HUB VALUES:
    // update the new canon bounding limits based on theta
    let changeInTheta = theta-oldTheta;
    /*
    * suspect that change in focal angle = change in limiting angles
    */
    cannonLowerLimit = cannonLowerLimit + changeInTheta;
    cannonUpperLimit = cannonUpperLimit + changeInTheta;
    // locate the canonLimitFocal
    // have to invert theta back to get mathematical translation
    let yTranslation = cannonFocalRadius*Math.sin(-1*theta);
    let xTranslation = cannonFocalRadius*Math.cos(-1*theta);
    cannonLimitFocalX = trueCannonFocalX+xTranslation;
    // flip y translation again to draw
    cannonLimitFocalY = trueCannonFocalY-yTranslation;
    // CALCULATING THE BARREL VALUES:
    // believe angle to be theta
    // also have to invert changes in Y
    // multiplying all angles by negative one to flip
    // bottom cannon side:
    barrelBottomStartX=trueCannonFocalX+
      Math.sqrt(2)*cannonFocalRadius*Math.cos(Math.abs(theta)-(Math.PI/4));
    barrelBottomStartY=trueCannonFocalY+
      (-1)*(Math.sqrt(2))*cannonFocalRadius*Math.sin(Math.abs(theta)-(Math.PI/4));
    barrelBottomEndX=barrelBottomStartX+
      barrelRadius*Math.cos(Math.abs(theta));
    barrelBottomEndY=barrelBottomStartY+
      (-1)*barrelRadius*Math.sin(Math.abs(theta));
    // top cannon side:
    barrelTopStartX=trueCannonFocalX+
      Math.sqrt(2)*cannonFocalRadius*Math.cos((Math.PI/4)+Math.abs(theta));
    barrelTopStartY=trueCannonFocalY+
      (-1)*Math.sqrt(2)*cannonFocalRadius*Math.sin((Math.abs(theta)+Math.PI/4));
    barrelTopEndX=barrelTopStartX+
      barrelRadius*Math.cos(Math.abs(theta));
    barrelTopEndY=barrelTopStartY+(-1)*barrelRadius*Math.sin(Math.abs(theta));
  }

  /**
  * UPDATE COORDINATES
  * update coordinates of mouse
  */
  function updateCoordinates(event) {
    let canvas = document.getElementById("canvas");
    let rect = canvas.getBoundingClientRect();
    xCoordinate = event.clientX - rect.left;
    yCoordinate = event.clientY - rect.top;
  }

  // *********************************************************
  // ***************** CANNONBALL METHODS ********************

  /**
  * DRAW BALL
  * draws ball given current time of trajectory, clears last frame
  * checks for collisions with zombies and boundaries -> triggers explosions
  */
  function drawBall() {
    let canvas = document.getElementById("canvas");
    let gc = canvas.getContext("2d");
    // PRELIMINARY OPERATIONS:
    // clear old ball, if necessary:
    if (ballX != null && ballY != null) {
      let oldBallX = ballX;
      let oldBallY=ballY;
      gc.beginPath();
      gc.fillStyle = "white";
      // need slightly larger radius in order to clear entirely (idiosyncratic)
      gc.arc(oldBallX,oldBallY,ballRadius+1,0,Math.PI*2);
      gc.fill();
      // redraw cannon and background if necessary
      clearCannon();
      renderCannon();
      drawBackground();
    } // otherwise, find initial ball position
    else {
      ballStartingY = barrelBottomEndY-Math.abs(barrelTopEndY-barrelBottomEndY)/2;
      ballStartingX = barrelBottomEndX-Math.abs(barrelBottomEndX-barrelTopEndX)/2;
    }
    // PHYSICS:
    // 4.9t^2-kt+c (freefall acceleration)
    // where k is vertical component of initial velocity
    // c is starting point, average of the cannon end points above the bottom
    ballY = 4.9*timeCounter*timeCounter -
      initialVelocity*Math.sin(Math.abs(fireTheta))*timeCounter + ballStartingY;
    // x component of velocity times time + location of cannon mouth centerpoint
    ballX = initialVelocity*Math.cos(Math.abs(fireTheta))*timeCounter + ballStartingX;
    // END CASES:
    // kill the timer if it reaches the ground or edges (sinks in a bit before blowing up)
    // only enable second shot after explosion finished
    if (ballY>=groundLevel-ballRadius*(2/3) ||
     ballX >= canvas.width-ballRadius*(2/3) || ballY <= ballRadius*(2/3) ) {
      clearInterval(timer);
      explosion();
      return;
    }
    // CHECK FOR ZOMBIE COLLISIONS
    if (checkForCollisionWithBall()) {
      clearInterval(timer);
      explosion();
      return;
    }
    // DRAW THE BALL:
    gc.beginPath();
    gc.fillStyle="black";
    gc.arc(ballX,ballY,ballRadius,0,Math.PI*2);
    gc.fill();
    timeCounter = timeCounter+timeInterval;
  }

  /**
  * EXPLOSION
  * pre-condition: ballX and ballY are the origin of explosion
  */
  function explosion() {
    // re-initialize var:
    explosionRadius = 0;
    // assign timed event:
    timer = setInterval(drawExplosionFrame,timeInterval*1000);
  }

  /**
  * DRAW EXPLOSION FRAME
  * called by explosion() to render each frame, according to timer
  * recall that ballX and ballY are the origin of the explosion
  */
  function drawExplosionFrame() {
    // if explosion still growing:
    if (explosionRadius < explosionRadiusMax) {
      // clear last explosion frame (needs to be one more than last value)
      clearExplosionFrame(explosionRadius-3);
      // pull canvas vars:
      let gc = document.getElementById("canvas").getContext("2d");
      // initialize angles
      let explosionRedAngle = explosionRedAngleInitial;
      let explosionYellowAngle = explosionYellowAngleInitial;
      // iterate over circle of rotation to draw explosion:
      while (explosionRedAngle < (13/6)*Math.PI ) {
        // calculate beam components
        let redBeamX = ballX + explosionRadius * Math.cos(explosionRedAngle);
        let redBeamY = ballY + explosionRadius * Math.sin(explosionRedAngle);
        let yellowBeamX = ballX + explosionRadius * Math.cos(explosionYellowAngle);
        let yellowBeamY = ballY + explosionRadius * Math.sin(explosionYellowAngle);
        // create color gradients s.t. the tip represents a cannon particle
        let gradientRed = gc.createLinearGradient(ballX,ballY,redBeamX,redBeamY);
        gradientRed.addColorStop(0,"red");
        gradientRed.addColorStop(.95,"white");
        gradientRed.addColorStop(1,"black");
        let gradientYellow = gc.createLinearGradient(ballX,ballY,yellowBeamX,yellowBeamY);
        gradientYellow.addColorStop(0,"yellow");
        gradientYellow.addColorStop(.95,"white");
        gradientYellow.addColorStop(1,"black");
        // draw red beam:
        gc.lineWidth=3;
        gc.beginPath();
        gc.strokeStyle=gradientRed;
        gc.moveTo(ballX,ballY);
        gc.lineTo(redBeamX,redBeamY);
        gc.stroke();
        // draw yellow beam:
        gc.beginPath();
        gc.strokeStyle=gradientYellow;
        gc.moveTo(ballX,ballY);
        gc.lineTo(yellowBeamX,yellowBeamY);
        gc.stroke();
        gc.lineWidth=1; //reset for other methods use
        // iterate angles
        explosionRedAngle = explosionRedAngle + (Math.PI/6);
        explosionYellowAngle = explosionYellowAngle + (Math.PI/6);
      }
      // increment explosion radius
      explosionRadius = explosionRadius + 4;
    }
    /*
    * otherwise: clear last explosion frame, end animation, allow another cannon shot,
    * and remove any zombies in the blast radius
    */
    else {
      clearInterval(timer);
      clearExplosionFrame(explosionRadius+1);
      mouseClicked=false;
      // check for any hit zombies:
      checkForCollisionWithExplosion();
      // draw cannon back in case we blew it up
      clearCannon();
      renderCannon();

      return;
    }
  }

  /**
  * CLEAR EXPLOSION FRAME
  */
  function clearExplosionFrame(clearRadius) {
    if (clearRadius < 0) {
      return;
    }
    let gc = document.getElementById("canvas").getContext("2d");
    gc.fillStyle="white";
    gc.beginPath();
    gc.arc(ballX,ballY,clearRadius,0,Math.PI*2);
    gc.fill();
    // draw background back
    drawBackground();
  }

  // *********************************************************
  // ***************** ZOMBIE METHODS ********************

  /**
  * KILL THE HORDE
  * stops calling zombies, clears zombie horde metadata
  * should be called before callTheHorde, even on the first iteration
  */
  function killTheHorde() {
    // stop calling zombies if applicable:
    if (zombieTimer != undefined) {
      clearInterval(zombieTimer);
    }
    // reset zombie army stats:
    zombieArmy = [];
    zombieSN = 1;
    // wipe screen:
    let canvas = document.getElementById("canvas");
    let gc = canvas.getContext("2d");
    gc.clearRect(0,0,canvas.width,canvas.height);
    drawBackground();
    clearCannon();  //need this b/c it draws the cannon stalk
    renderCannon();
  }

  /**
  * CALL THE HORDE
  * kicks off the zombie mechanics
  */
  function callTheHorde() {
    zombieTimer = setInterval(spawnAndMoveZombies,zombieSpeed*1000);
  }

  /**
  * SPAWN AND MOVE ZOMBIES
  */
  function spawnAndMoveZombies() {
    // spawn a zombie
    spawnZombie();
    // move zombies (includes clearing and redrawing each one):
    moveAndDrawZombies();
  }

  /**
  * SPAWN ZOMBIE
  * spawns a zombie
  */
  function spawnZombie() {
    let canvas = document.getElementById("canvas");
    // spawn zombie with initialized values:
    let zombie = new Object();
    zombie.xPosition = canvas.width-zombieBoundingWidth;
    zombie.yPosition = groundLevel-zombieBoundingHeight-2;
    zombie.id = zombieSN;
    zombieSN++;
    // add new zombie to army
    zombieArmy.push(zombie);
  }

  /**
  * MOVE ZOMBIES
  * moves and animates a zombie; draws it
  * for movement stats, see level loaders
  */
  function moveAndDrawZombies() {
    // reinitialize variables:
    zombieTranslation = 0;
    // time per iteration = ZDT = TT / numSteps * numZombs ; numSteps = totalDistance / deltaX = TT
    // -> ZDT = 1 / numZombs  // scaled to enhance performance (slower frames)
    zombieDeltaT = 5;
    zombieAnimationTimer = setInterval(moveAndDrawZombiesHelper, zombieDeltaT);
  }

  /**
  * MOVE ZOMBIE HELPER
  * helps move zombie execute
  * note: moves all zombies but the last (which was just added to canvas)
  * the last zombie, it just draws
  */
  function moveAndDrawZombiesHelper() {
    // if we haven't moved the max amount yet:
    if (zombieTranslation <= zombieMaxTranslation) {
      // iterate over zombies and move a frame:
      for (let i = 0; i< zombieArmy.length; i++) {
        let zombieI = zombieArmy[i];
        // only operate if it hasn't been marked for death
        if (!checkForHit(zombieI)) {
          clearZombie(zombieI);
          zombieI.xPosition = zombieI.xPosition - zombieDeltaX;
          drawZombie(zombieI);
        }
      }
      // only increment that the translation has occurred after all zombies moved a frame
      zombieTranslation = zombieTranslation + zombieDeltaX;
    } else {
      clearInterval(zombieAnimationTimer);
    }
  }

  /**
  * CHECK FOR HIT
  * returns true if the zombie's been marked for removal
  */
  function checkForHit(zombieI) {
    for (let i = 0; i< zombiesMarkedForDeath.length; i++) {
      if (zombieI.id == zombiesMarkedForDeath[i].id) {
        return true;
      }
    }
    return false;
  }

  /**
  * CLEAR ZOMBIE
  */
  function clearZombie(zombieI) {
    let canvas = document.getElementById("canvas");
    let gc = canvas.getContext("2d");
    gc.beginPath();
    gc.fillStyle="white";
    gc.fillRect(zombieI.xPosition-1,zombieI.yPosition-1,
      zombieBoundingWidth+2,zombieBoundingHeight+2);
  }

  /**
  * DRAW ZOMBIE
  */
  function drawZombie(zombieI) {
    let gc = document.getElementById("canvas").getContext("2d");

    // draw head:
    gc.beginPath();
    gc.strokeStyle="black";
    let headFocalX = zombieI.xPosition + zombieHeadRadius;
    let headFocalY = zombieI.yPosition + zombieHeadRadius;
    gc.arc(headFocalX,headFocalY,zombieHeadRadius,0,2*Math.PI);
    gc.stroke();

    // draw neck:
    let neckStartX = zombieI.xPosition + zombieHeadRadius +
      Math.cos(zombieNeckAngle)*(zombieHeadRadius+zombieNeckRadius);
    let neckStartY = zombieI.yPosition + zombieTopRegionSize;
    let neckEndX = zombieI.xPosition + zombieHeadRadius +
      Math.cos(zombieNeckAngle)*zombieHeadRadius;
    let neckEndY = zombieI.yPosition + zombieHeadRadius +
      Math.sin(zombieNeckAngle)*zombieHeadRadius;
    gc.beginPath();
    gc.strokeStyle="black";
    gc.moveTo(neckStartX,neckStartY);
    gc.lineTo(neckEndX,neckEndY);
    gc.stroke();

    // draw spine:
    let spineX = neckStartX;
    let spineStartY = neckStartY;
    let spineEndY = spineStartY+zombieTorsoLength;
    gc.beginPath();
    gc.strokeStyle="black";
    gc.moveTo(spineX,spineStartY);
    gc.lineTo(spineX,spineEndY);
    gc.stroke();

    // draw legs:
    let legsStartX = spineX;
    let legsStartY = spineEndY;
    let halfAngle = zombieLegAngle/2; // spine forms a bisection
    let leftLegEndX = legsStartX - Math.sin(halfAngle)*zombieLegLength;
    let rightLegEndX = legsStartX + Math.sin(halfAngle)*zombieLegLength;
    let legsEndY = spineEndY + zombieBottomRegionSize;

    gc.beginPath();
    gc.strokeStyle="black";
    gc.moveTo(legsStartX,legsStartY);
    gc.lineTo(leftLegEndX,legsEndY);
    gc.stroke();
    gc.moveTo(legsStartX,legsStartY);
    gc.lineTo(rightLegEndX,legsEndY);
    gc.stroke();

    // draw arms:
    let armsStartX = spineX;
    let armsStartY = spineStartY;
    let angleUnderTheHorizontal = (Math.PI/2) -
      (zombieArmToArmAngle+zombieArmToSpineAngle);
    let leftArmEndX = armsStartX-
      Math.cos(angleUnderTheHorizontal)*zombieArmLength;
    let leftArmEndY = armsStartY +
      Math.sin(angleUnderTheHorizontal)*zombieArmLength;
    let rightArmEndX = armsStartX -
      Math.cos(angleUnderTheHorizontal+zombieArmToArmAngle)*zombieArmLength;
    let rightArmEndY = armsStartY +
      Math.sin(angleUnderTheHorizontal+zombieArmToArmAngle)*zombieArmLength;
    gc.beginPath();
    gc.strokeStyle="black";
    gc.moveTo(armsStartX,armsStartY);
    gc.lineTo(leftArmEndX,leftArmEndY);
    gc.stroke();
    gc.beginPath();
    gc.strokeStyle="black";
    gc.moveTo(armsStartX,armsStartY);
    gc.lineTo(rightArmEndX,rightArmEndY);
    gc.stroke();

    // check for collision with player
    checkForCollisionWithPlayer(zombieI);
  }

  /**
  * CHECK FOR COLLISION WITH PLAYER
  */
  function checkForCollisionWithPlayer(zombieI) {
    if (zombieI.xPosition<=boundingCannonBoxX+boundingCannonBoxWidth) {
      endGame();
    }
  }

  /**
  * CHECK FOR COLLISION WITH BALL
  * called by drawBall
  */
  function checkForCollisionWithBall() {
    // locate cannon ball radius:
    let radiusLeft = ballX-ballRadius;
    let radiusRight = ballX+ballRadius;
    let radiusTop = ballY-ballRadius;
    let radiusBottom = ballY+ballRadius;
    // if collision with zombie, trigger explosion
    for (let i = 0; i< zombieArmy.length; i++) {
      let zombieI = zombieArmy[i];
      let zombieLeftX = zombieI.xPosition;
      let zombieRightX = zombieI.xPosition+zombieBoundingWidth;
      let zombieTopY = zombieI.yPosition;
      let zombieBottomY = zombieI.yPosition+zombieBoundingHeight;

      // if left of zombie contained within blast, or right of zombie
      if ( (zombieLeftX >= radiusLeft && zombieLeftX <= radiusRight) ||
        (zombieRightX >= radiusLeft && zombieRightX <= radiusRight) ){
        // if top contained within blast, or bottom of zombie
        if ( (zombieTopY>= radiusTop && zombieTopY <= radiusBottom) ||
          (zombieBottomY>= radiusTop && zombieBottomY <= radiusBottom) ) {
          // collision has occurred:
          zombiesMarkedForDeath.push(zombieI);
          return true;
        }
      }
      // check if blast contained within zombie (b/c the cannonball is smaller than zombie)
      if ( (radiusLeft >= zombieLeftX && radiusLeft <= zombieRightX) ||
        (radiusRight>=zombieLeftX && radiusRight<=zombieRightX) ) {
        if ( (radiusTop >= zombieTopY && radiusTop <= zombieBottomY) ||
          (radiusBottom >= zombieTopY && radiusBottom <= zombieBottomY) ) {
            zombiesMarkedForDeath.push(zombieI);
            return true;
        }
      }
    }
    return false;
  }

  /**
  * CHECK FOR COLLISION WITH EXPLOSION
  * called by drawExplosionFrame
  */
  function checkForCollisionWithExplosion() {
    // locate blast radius:
    let blastRadiusLeft = ballX-explosionRadius;
    let blastRadiusRight = ballX+explosionRadius;
    let blastRadiusTop = ballY-explosionRadius;
    let blastRadiusBottom = ballY+explosionRadius;
    // remove all the zombies at once after checking, to prevent array size variability
    for (let i = 0; i<zombieArmy.length; i++) {
      let zombieI = zombieArmy[i];
      let zombieLeftX = zombieI.xPosition;
      let zombieRightX = zombieI.xPosition+zombieBoundingWidth;
      let zombieTopY = zombieI.yPosition;
      let zombieBottomY = zombieI.yPosition+zombieBoundingHeight;
      // if left of zombie contained within blast, or right of zombie
      if ( (zombieLeftX >= blastRadiusLeft && zombieLeftX <= blastRadiusRight) ||
        (zombieRightX >= blastRadiusLeft && zombieRightX <= blastRadiusRight) ){
        // if top contained within blast, or bottom of zombie
        if ( (zombieTopY>= blastRadiusTop && zombieTopY <= blastRadiusBottom) ||
          (zombieBottomY>= blastRadiusTop && zombieBottomY <= blastRadiusBottom) ) {
          // collision has occurred:
          // only add zombie if we haven't added it when hit by ball
          if (!checkForHit(zombieI)) {
            zombiesMarkedForDeath.push(zombieI);
          }
        }
      }
    }
    // now, go back and clear and delete zombies
    for (let i = 0; i<zombiesMarkedForDeath.length; i++) {
      let zombieID = zombiesMarkedForDeath[i].id;
      deleteZombieById(zombieID);
    }
    // reinitialize hit zombies
    zombiesMarkedForDeath = [];
  }

  /**
  * DELETE ZOMBIE BY ID
  * kill a zombie, gain points, check for level progression
  */
  function deleteZombieById(id) {
    for (let i = 0; i<zombieArmy.length; i++) {
      let zombieI = zombieArmy[i];
      if (zombieI.id == id) {
        // clear zombie from screen
        clearZombie(zombieI);
        // delete zombie from zombieArmy
        zombieArmy.splice(i,1);
        // give user points:
        points = points + pointsPerZombie;
        // display improved points:
        updatePointDisplay();
        // check for level progression:
        if (points >= pointsToProgress) {
          loadNextLevel();
        }
        return;
      }
    }
  }


  window.onload=startup;


}) ();
