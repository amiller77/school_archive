/*
* Document: chatit.js
* Author: Alexander Miller
* Description: sends to and requests info from chatit_service.js and injects it into chatit.html
* Behavior:
*   on load:
*     -> request all comments from server, insert name and comment in own tags
*   every 5 seconds:
*     -> refresh comments (remove all comments from page, then follow on load behavior)
*   on send button click:
*     -> contents of "name box" and "comment box" sent as POST to server
*     -> write to page whether or not the send was wuccessful
* Course: CSC 337
* Date: 10/31
*/

"use strict";
( function  () {

  /**
  * START UP
  * launches on load
  * sets event-handlers, and pulls messages from server, get 5 second interval in operation
  */
  function startup() {
    console.log("starting up...");
    // set event-handler
    let submitButton = document.getElementById("submitButton");
    submitButton.onclick = buttonHandler;
    // make GET REQUEST
    makeGetRequest();
    // set interval etc.
    setInterval(makeGetRequest,5000);
    console.log("start up finished.");
  }

  /**
  * MAKE GET REQUEST
  * makes get request and pastes to page
  */
  function makeGetRequest() {
    console.log("making get request");
    // first clear the existing comments:
    document.getElementById("chatFeed").innerHTML="";
    // then fetch:
    let url = "http://localhost:3000";
    fetch(url)
      .then(checkStatus)
      .then(function(responseText){
        // "objectify" contents, iterate over and add to page
        let responseObject = JSON.parse(responseText);
        for (let i = 0; i < responseObject.messages.length; i++) {
          let name = responseObject.messages[i].name;
          let comment = responseObject.messages[i].comment;
          let para = document.createElement("p");
          let spanA = document.createElement("span");
          let spanB = document.createElement("span");
          spanA.innerHTML=name+": ";
          spanB.innerHTML=comment;
          para.appendChild(spanA);
          para.appendChild(spanB);
          document.getElementById("chatFeed").appendChild(para);
        }

      })
      .catch(function(error) {
        // post error message:
        console.log(error);
        document.getElementById("chatFeed").innerHTML=
          "Server down. No chatting today. Developers, see console.";
      });
  }

  /**
  * BUTTON HANDLER
  * takes textarea input, clears it, then makes post request
  * doesn't clear name field to make multiple posts easier
  */
  function buttonHandler() {
    console.log("button clicked.");
    let name = document.getElementById("nameInput").value;
    let comment = document.getElementById("commentInput").value;
    // clear textArea, but not name
    document.getElementById("commentInput").value="";
    // make post request
    makePostRequest(name,comment);
  }

  /**
  * MAKE POST REQUEST
  * makes post request; pastes response to page
  * after 3 seconds, that response disappears so next one can show
  */
  function makePostRequest(name, comment) {
    console.log("making post request.");
    // wrap message in JS object to stringify
    let message = {name: name, comment: comment};
    // wrap fetch parameter in JS object
    let fetchParameter = {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(message)
    };
    // make the query, passing fetch param
    fetch("http://localhost:3000",fetchParameter)
      .then(checkStatus)
      .then(function(responseText){
        // post response to page
        document.getElementById("messageReceived").innerHTML=responseText;
        // reset after 3 seconds
        setTimeout( function() {
          document.getElementById("messageReceived").innerHTML="";
        }, 3000);
      })
      .catch(function(error) {
        // post response to page
        document.getElementById("messageReceived").innerHTML=error;
      });
  }

  /**
  * CHECK STATUS
  */
  function checkStatus(response) {
    if (response.status >= 200 && response.status<300) {
      return response.text();
    } else {
      return Promise.reject(new Error(response.status+":"+response.statusText));
    }
  }

  window.onload = startup;

}) ();
