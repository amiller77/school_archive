/*
File: gerrymandering.js
Author: Alexander Miller
Description: JS File to operate the following upon user submitting state name:
  * (h4) eligible voter information fetched and injected into div, #voters
  * (h2) state name added to div, #statename
    ^ capitalization comes from server return, not as user-given
  * for each element in districts list:
    ^ create div, .dem; set width to "dem-votes / (dem-votes + gop-votes)" percent
    ^ create div, .gop
    ^ append .dem div to .gop div; append .gop div to statedata div
  * determine gerrymandering from districts data:
    ^ "wasted vote": any votes gained by loser
      ~ any votes for winner beyond (half of the total number) + 1
    ^ compare the "wasted votes" for each party
    ^ district gerrymandered if one is larger by 7% than the other
    ^ (h3) add this data to the top of the statedata div as follows:
      ~ "Gerrymandered to favor the <favored party> Party"
      ~ "Not gerrymandered"
  * if missing data (410 error code), add missing data message to div, #errors
    ^ any non-410 error (consider 404) should add a descriptive error message to the same div
  * between searches, wipe screen
*/


"use strict";
( function () {

  /**
  * STARTUP
  * function to run on load; assigns button event-handler
  */
  function startup() {
    console.log("starting up...");
    // set event-handlers:
    let button = document.getElementById("search");
    button.onclick = search;
    console.log("start-up complete");
  }

  /**
  * SEARCH
  * event-handler for button; clears page and makes server calls based on input
  */
  function search() {
    console.log("searching...");
    // clear page html
    document.getElementById("statename").innerHTML="";
    document.getElementById("voters").innerHTML="";
    document.getElementById("statedata").innerHTML="";
    document.getElementById("errors").innerHTML="";
    // gather input from textbox, clear text box:
    let textbox = document.getElementById("box");
    let inputValue = textbox.value;
    textbox.value="";
    // VALIDATE INPUT VALUE?
    // make the query:
    let votersUrl = "http://localhost:3000?state="+inputValue+"&type=voters";
    let districtsUrl= "http://localhost:3000?state="+inputValue+"&type=districts";
    serverCall(votersUrl,0);
    serverCall(districtsUrl,1);
    console.log("search complete.");
  }

  /**
  * SERVER CALL
  * makes makes call to server based on url and type of call
  * takes 0 if just a simple string output to GUI, or 1 for the more complicated JSON parse
  * then, processes the response from that call accordingly, including error handling
  */
  function serverCall(url,dataType) {
    console.log("making server call to: "+url);
	  fetch(url)
		    .then(checkStatus)
		    .then(function(responseText){
          // if just the number of voters string:
          if(dataType==0) {
            // create an h4 for it and pop it in the voters section
            let newText = document.createElement("h4");
            newText.innerHTML=responseText+" eligible voters";
            document.getElementById("voters").appendChild(newText);
            // if the districts list:
          } else {
            // parse the info and put the state name as h2 into statename div
            let jsonData = JSON.parse(responseText);
            let stateName = document.createElement("h2");
            stateName.innerHTML=jsonData.state;
            document.getElementById("statename").appendChild(stateName);
            // iterate over children to process the gerrymandering data:
            let data = jsonData.districts;
            let totalDem = 0;
            let totalGop = 0;
            let totalDemWaste = 0;
            let totalGopWaste = 0;
            for (let i = 0; i<data.length; i++) {
              // pull the dem and gop data:
              let demVotes = data[i][0];
              let gopVotes = data[i][1];
              console.log("demVotes: "+demVotes);
              console.log("gopVotes: "+gopVotes);
              // calculate gerrymandering
              totalDem = totalDem + demVotes;
              totalGop = totalGop + gopVotes;
              let wastedDem=0;
              let wastedGop=0;
              if (demVotes > gopVotes) {
                wastedDem = (demVotes -((demVotes+gopVotes)/2) + 1);
                wastedGop = gopVotes;
              } else {
                wastedGop = (gopVotes -((demVotes+gopVotes)/2) + 1);
                wastedDem = demVotes;
              }
              console.log("wastedDem: "+wastedDem);
              console.log("wastedGop: "+wastedGop);
              totalDemWaste=totalDemWaste+wastedDem;
              totalGopWaste=totalGopWaste+wastedGop;
              // create the visual red and blue blocks for the page
              let demDiv = document.createElement("div");
              demDiv.className="dem";
              let width = demVotes/(demVotes+gopVotes);
              demDiv.style.width= (width*100)+"%";
              let gopDiv = document.createElement("div");
              gopDiv.className="gop";
              gopDiv.appendChild(demDiv);
              document.getElementById("statedata").appendChild(gopDiv);
            }
            // calculate the gerrymandering verdict:
            let gerrymanderingResults = document.createElement("h3");
            console.log("totalDemWaste: "+totalDemWaste);
            console.log("totalGopWaste "+totalGopWaste);
            // determine if gerrymandering occured and configure corresponding message:
            if (Math.abs(totalDemWaste-totalGopWaste) >= .07*(totalDemWaste+totalGopWaste)) {
              let favoredParty = "";
              if (totalDemWaste < totalGopWaste) {
                favoredParty = "Democratic";
              } else {
                favoredParty = "Republican";
              }
              gerrymanderingResults.innerHTML = "Gerrymandered to favor the "+favoredParty+" Party";
            } else {
              gerrymanderingResults.innerHTML = "Not gerrymandered";
            }
            // add that message to the page
            let statedata = document.getElementById("statedata");
            statedata.insertBefore(gerrymanderingResults,statedata.firstChild);
          }
		    })
		    .catch(function(error) {
            document.getElementById("errors").innerHTML="The following error occurred: "+error;
		    });
  }

  /**
  * CHECK STATUS
  * checks the response status from the web server
  */
  function checkStatus(response) {
    console.log("server response status check. ");
	   if (response.status >= 200 && response.status<300) {
       return response.text();
	   } else {
       return Promise.reject(new Error(response.status+":"+response.statusText));
	   }
  }

  window.onload = startup;

})();
