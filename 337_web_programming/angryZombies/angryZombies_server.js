/*
* File: angryZombies_server.js
* Author: Alexander Miller
* Class: CSC337
* Date: 11/29
* Assignment: Final Project
* Description: web server to handle persistence for angryZombies
* server handles loading and saving games based on a username
* usernames are saved in saves.txt on the same line, space delineated, as their lvl progression
* saving is a post request, loading is a get request
* existing profiles are overwritten on save
* loading a non-existent profile throws a 404
*/

// CONSTANTS AND IMPORTS:
const express = require("express");
let app = express();
app.use(express.static('public'));
const fs = require("fs");
let bodyParser = require("body-parser");
// PREVENTING PREFLIGHT CORS
app.use(function(req,res,next){
  res.header("Access-Control-Allow-Origin","*");
  res.header("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");
  next();
})


// GET
// handles loads (get)
app.get('/',function(req,res) {
  res.header("Access-Control-Allow-Origin","*");
  console.log("getting");
  let username = req.query.username;
  // open file saves, level progression for that user
  let fileContents = fs.readFileSync("saves.txt",'utf8');
  let lines = fileContents.split("\n");
  // iterate over lines in file, find the user
  for (let i = 0; i< lines.length; i++) {
    let lineArray = lines[i].split(" ");
    let lineUser = lineArray[0];
    if (lineUser == username) {
      console.log("sending: "+lineArray[1]);
      res.send(lineArray[1]);
    }
  }
  console.log("sending a 404");
  res.sendStatus(404);
});


// POST
// handles saves (post)
let jsonParser = bodyParser.json();
app.post('/',jsonParser, function (req,res) {
  res.header("Access-Control-Allow-Origin","*");
  // get user name and level
  let username = req.body.username;
  let level = req.body.level;

  // read saves file to see if entry already exists
  let fileContents = fs.readFileSync("saves.txt",'utf8');
  let lines = fileContents.split("\n");
  let entryFound = false;
  let lineNumber = null;
  for (let i = 0; i<lines.length; i++) {
    let lineArray = lines[i].split(" ");
    if (lineArray[0] == username) {
      entryFound = true;
      lineNumber = i;
      break;
    }
  }

  // rewrite file with new data if entry already exists
  if (entryFound) {
    lines[lineNumber] = username+" "+level;
    let newFileContents = lines.join("\n");
    fs.writeFile("saves.txt",newFileContents, function(err) {
      if (err) {
        console.log(err);
        res.send(err);
        return;
      }
      console.log("overwrite successful");
      res.send("Game saved as: "+username+" (overwriting old save)");
    });
  }

  // otherwise append old file with new data:
  if (!entryFound) {
    let newLine = username+" "+level+"\n";
    fs.appendFile("saves.txt",newLine,function(err) {
      if (err) {
        console.log(err);
        res.send(err);
        return;
      }
      console.log("append successful");
      res.send("Game saved as: "+username+ " (new save)");
    });
  }

});



app.listen(3000);
