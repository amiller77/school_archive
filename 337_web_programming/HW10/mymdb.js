/*
* File: mymdb.js
* Author: Alexander Miller
* Description: Javascript file for website allowing search and results of database
* Course: CSC 337
* Assignment: HW 10 ~ Kevin Bacon
* Date: November 14, 2018
* Behavior:
*   ^ sort data by year, descending; then by movie title, ascending
*   ^ tables have 3 columns: number starting with 1, title, year
*   ^ columns have style headings (such as bold)
*   ^ rows have alternating colors (zebra-striping)
*/

"use strict";
( function () {
  let firstName;
  let lastName;

  // START UP
  function startup () {
    console.log("starting up...");
    // set event-handlers
    document.getElementById("allMoviesGo").onclick=allMoviesHandler;
    document.getElementById("kevinBaconGo").onclick=kevinBaconHandler;
  }

  // ALL MOVIES HANDLER
  function allMoviesHandler() {
    console.log("all movies handling");
    firstName = document.getElementById("allMoviesFirstName").value.trim();
    lastName = document.getElementById("allMoviesLastName").value.trim();
    console.log("all movies "+firstName);
    console.log("all movies "+lastName);
    // true parameter -> all movies request
    makeServerRequest(true);
  }

  // KEVIN BACON HANDLER
  function kevinBaconHandler() {
    console.log("kevin bacon handling");
    firstName = document.getElementById("kevinBaconFirstName").value;
    lastName = document.getElementById("kevinBaconLastName").value;
    console.log("kevin bacon "+firstName);
    console.log("kevin bacon "+lastName);
    // false parameter -> kevin bacon request
    makeServerRequest(false);
  }

  /*
  * MAKE SERVER REQUEST
  * makes get requests to server
  * takes first and last name of actor as params, as well as "context"
  * "context": true -> all movies query ; false -> kevin bacon query
  */
  function makeServerRequest(context) {
    console.log("making request to server...");
    let url;
    // if all movies request ->
    if (context) {
      url = "http://localhost:3000?first="+firstName+"&last="+lastName+"&context=allMovies";
    } // if kevin bacon request ->
    else {
      url = "http://localhost:3000?first="+firstName+"&last="+lastName+"&context=kevinBacon";
    }
    //fetch
    fetch(url)
      .then(checkStatus)
      .then(function(responseText){
        // code processing valid response
        console.log(responseText);
      })
      .catch(function(error) {
        //code processing error
        console.log(error);
      });
  }

  // CHECK STATUS
  function checkStatus(response) {
    if (response.status >= 200 && response.status < 300) {
      return response.text();
    } else if (response.status == 500) {
      // actor not found -> show this on page
      document.getElementById("results").innerHTML=firstName+" "+lastName+" not found.";
    } else {
      return Promise.reject(new Error(response.status+":"+response.statusText));
    }
  }


  window.onload=startup;
})();
