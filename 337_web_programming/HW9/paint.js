/**
* File: paint.js
* Author: Alexander Miller
* HMWK: 9, Paint
* Class: 337
* Date: Nov. 6
*/

"use strict";
( function () {
  // VARIABLES
  // coordinates;
  let xCoordinate;
  let yCoordinate;
  // pen data
  let penSize;
  let penColor;
  let penStyle;
  // mouse up or down?
  let mouseDown;

  /**
  * START UP
  * sets event handles, initializes our global vars, colors gradient
  */
  function startup() {
    mouseDown=false;
    console.log("starting up..");
    // initialize penSize to 1 and color to black
    penSize=1;
    renderPenSizeDemo();
    penColor="black";
    penStyle="pen";

    // initialize colorPicker canvas
    initializeColorPicker();
    // set canvas event-handlers
    document.getElementById("canvas").onmousedown=mouseDownHandler;
    document.getElementById("canvas").onmouseup=mouseUpHandler;
    document.getElementById("canvas").onmouseover=updateCoordinates;
    document.getElementById("canvas").onmousemove=draw;
    document.getElementById("canvas").onclick = draw;
    // set button event-handlers
    document.getElementById("pen").onclick = changePen;
    document.getElementById("circles").onclick = changePen;
    document.getElementById("squares").onclick = changePen;
    document.getElementById("lines").onclick = changePen;
    document.getElementById("clear").onclick=clear;
    // set colorPicker event-handler
    document.getElementById("colorPicker").onclick = changeColor;
    document.getElementById("plus").onclick = userIncreasePenSize;
    document.getElementById("minus").onclick = userDecreasePenSize;
    console.log("start up complete");
  }

  /**
  * INITIALIZE COLOR PICKER
  * grunt work for coloring the gradient
  */
  function initializeColorPicker() {
    console.log("initializing colorPicker");
    let colorPicker = document.getElementById("colorPicker");
    let gc = colorPicker.getContext("2d");
    // first gradient -> base color background
    let gradient = gc.createLinearGradient(0,0,colorPicker.width,0);
    gradient.addColorStop(0,"rgb(255,0,0)");
    gradient.addColorStop(.15,"rgb(255,0,255)");
    gradient.addColorStop(.33,"rgb(0,0,255)");
    gradient.addColorStop(.49,"rgb(0,255,255)");
    gradient.addColorStop(.67,"rgb(0,255,0)");
    gradient.addColorStop(.84,"rgb(255,255,0)");
    gradient.addColorStop(1,"rgb(255,0,0)");
    gc.fillStyle=gradient;
    gc.fillRect(0,0,colorPicker.width,colorPicker.height);
    // 2nd gradient -> light/darkness filter
    gradient=gc.createLinearGradient(0,0,0,colorPicker.height);
    gradient.addColorStop(0,"rgba(255,255,255,1)");
    gradient.addColorStop(0.5,"rgba(255,255,255,0)");
    gradient.addColorStop(0.5,"rgba(0,0,0,0)");
    gradient.addColorStop(1,"rgba(0,0,0,1)");
    gc.fillStyle=gradient;
    gc.fillRect(0,0,colorPicker.width,colorPicker.height);
    console.log("colorPicker initialized");
  }

  // ******** PEN METHODS **********

  /**
  * USER INCREASE PEN SIZE
  */
  function userIncreasePenSize() {
    penSize = penSize +1;
    console.log(penSize);
    renderPenSizeDemo();
    console.log("pen size increased.");
  }

  /**
  * USER DECREASE PEN SIZE
  */
  function userDecreasePenSize() {
    console.log(penSize);
    if (penSize>=2) {
      penSize = penSize -1;
    }
    renderPenSizeDemo();
    console.log("pen size decreased.");
  }

  /**
  * RENDER PEN SIZE DEMO
  * draws the pen size on the demo canvas
  */
  function renderPenSizeDemo() {
    let demoCanvas = document.getElementById("penDisplay");
    let demoGC = demoCanvas.getContext("2d");
    demoGC.clearRect(0,0,demoCanvas.width,demoCanvas.height);
    demoGC.beginPath();
    demoGC.arc(demoCanvas.width/2,demoCanvas.height/2,penSize,0,2*Math.PI);
    demoGC.fill();
  }

  /**
  * CHANGE PEN
  * changes our pen style (circle vs. line etc.)
  */
  function changePen() {
    penStyle = this.innerHTML;
  }

  /**
  * CHANGE COLOR
  * changes color by pulling from gradient
  */
  function changeColor(event) {
    // find click
    let rect = document.getElementById("colorPicker").getBoundingClientRect();
    let x = event.clientX - rect.left;
    let y = event.clientY - rect.top;
    // get ImageData object
    let gc = document.getElementById("colorPicker").getContext("2d");
    let imageData = gc.getImageData(x,y,1,1);
    penColor="rgb("+imageData.data[0]+","+imageData.data[1]+","+imageData.data[2]+")";
    console.log("color changed.");
  }

  // ******************** DRAW METHODS **************

  /**
  * DRAW
  * draws on our canvas, depending on the current pen style
  */
  function draw(event) {
    // only draw if mouse is currently pressed, or a click
    if (event.type == "click" || mouseDown) {
      console.log("drawing...");
      let gc=document.getElementById("canvas").getContext("2d");
      // update our stroke width, and color
      gc.lineWidth=penSize;
      gc.strokeStyle = penColor;
      gc.fillStyle=penColor;
      // "pen" style drawing
      if (penStyle=="pen") {
        gc.beginPath();
        // start at existing X,Y ; then update to new X,Y
        gc.moveTo(xCoordinate,yCoordinate);
        updateCoordinates(event);
        gc.lineTo(xCoordinate,yCoordinate);
        gc.stroke();
      } // "circle" style drawing
      else if (penStyle == "circles") {
        gc.beginPath();
        gc.arc(xCoordinate,yCoordinate,5*penSize,0,2*Math.PI);
        gc.stroke();
        updateCoordinates(event);
      } // "square" style drawing
      else if (penStyle == "squares") {
        gc.beginPath();
        // draw rect a bit behind, update coords
        gc.strokeRect(xCoordinate,yCoordinate,10*penSize,10*penSize);
        updateCoordinates(event);
      } // "line" style drawing
      else if (penStyle == "lines") {
        gc.beginPath();
        updateCoordinates(event);
        gc.moveTo(0,0);
        gc.lineTo(xCoordinate,yCoordinate);
        gc.stroke();
      }
      console.log("drawing complete");
    }
  }

  /**
  * CLEAR
  * wipes the canvas
  */
  function clear() {
    let canvas = document.getElementById("canvas");
    let gc = canvas.getContext("2d");
    gc.clearRect(0,0,canvas.width,canvas.height);
    console.log("canvas cleared");
  }

  // ************* OTHER FUNCTIONALITY ****************

  /**
  * MOUSE DOWN HANDLER
  * records that mouse is currently down and update coords
  */
  function mouseDownHandler() {
    updateCoordinates(event);
    mouseDown = true;
    console.log("mouse down.");
  }

  /**
  * MOUSE UP HANDLER
  * records that mouse is currently up
  */
  function mouseUpHandler() {
    mouseDown = false;
    console.log("mouse up.");
  }

  /**
  * GET COORDINATES
  * updates xCoordinate and yCoordinate from event param
  */
  function updateCoordinates(event) {
    let canvas = document.getElementById("canvas");
    let rect = canvas.getBoundingClientRect();
    xCoordinate = event.clientX - rect.left;
    yCoordinate = event.clientY - rect.top;
  }

  window.onload = startup;


})();
