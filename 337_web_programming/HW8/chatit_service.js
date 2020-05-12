/*
* Document: chatit_service.js
* Author: Alexander Miller
* Description: NodeJS service saves new comments and returns data on all comments made
* Behavior:
*   handles GET and POST requests
*   POST allows user to add chat info to records
*   GET returns contents of messages.txt as JSON
*   messages.txt has chat info containing commenter and comment formated as such:
*     commenter:::comment
* Assumptions:
*   messages.txt exists and always has valid content
* Course: CSC 337
* Date: 10/31
*/

// imports, constants, and setup:
const express = require("express");
const bodyParser = require('body-parser');
const fs = require("fs");
const jsonParser = bodyParser.json();
const app = express();
app.use(express.static('public'));

// avoiding preflight CORS error:
app.use(function(req,res,next) {
  res.header("Access-Control-Allow-Origin","*");
  res.header("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
  next();
});

/*
* GET
* upon a get request, send contents of messages.txt as JSON
*/
app.get('/', function(req,res) {
  res.header("Access-Control-Allow-Origin","*");

  console.log("fielding get request");
  // open and get contents of messages.txt
  let fileContents = fs.readFileSync("messages.txt",'utf8');
  console.log(fileContents);
  // create JS object to store data
  let data = {
    messages: []
  };
  // split into lines, and iterate over
  let lines = fileContents.split("\n");
  for (let i = 0; i< lines.length; i++) {
    lines[i]=lines[i].trim().trim("\r");
    // split line into name and comment
    if (lines[i]!="") {
      let contentArray = lines[i].split(":::");
      // add to our data
      data.messages.push(
        {
          name: contentArray[0],
          comment: contentArray[1]
        });
    }
  }
  res.send(JSON.stringify(data));
});


/*
* POST
* parameters: commenter name, comment
*   -> append these to end of messages.txt as new line with name first and comment second
*   -> separate name and comment by ":::"
*   -> send message to client indicating if append was successful (saved correctly)
*/
app.post('/', jsonParser, function(req,res) {
  res.header("Access-Control-Allow-Origin","*");

  console.log("fielding post request");

  // get data
  let name = req.body.name;
  let comment = req.body.comment;
  console.log(name);
  console.log(comment);

  //try file write; send confirmation or else send error
  fs.appendFile("messages.txt",name+":::"+comment+"\n", function(err) {
    if (err) {
      res.send("Message couldn't be saved. Try again.");
    }
    res.send("Message saved!");
  });

});

// listen...
app.listen(3000);
