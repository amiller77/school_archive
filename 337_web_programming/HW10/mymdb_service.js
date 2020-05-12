/*
* File: mymdb_service.js
* Author: Alexander Miller
* Description: Web server for website allowing search and results of database
* Course: CSC 337
* Assignment: HW 10 ~ Kevin Bacon
* Date: November 14, 2018
*/

// IMPORTS AND CONSTANTS:
const express = require("express");
const app = express();
app.use(express.static('public'));
const mysql = require("mysql");

// GET
app.get('/',function(req,res) {
  console.log("get initiated");
  res.header("Access-Control-Allow-Origin","*");
  let firstName = req.query.first;
  let lastName = req.query.last;
  let context = req.query.context;
  // validate the parameters:
  if (context!="allMovies" && context!="kevinBacon") {
    res.send("Error: Bad Parameters Passed to Server");
  }
  // for valid parameters:
  else {
    // make query to get the actor ID:
    let idQuery = "SELECT id FROM actors"+
      " WHERE first_name LIKE '"+firstName+"%' AND last_name LIKE '"+lastName+"'"+
      " ORDER BY film_count DESC, id LIMIT 1";
    console.log("idQuery = "+idQuery);
    connectToDatabase(idQuery,res,context);
  }
});

// CONNECT TO DATABASE
function connectToDatabase(idQuery,res,context) {
  let con = mysql.createConnection({
    host: "mysql.allisonobourn.com",
    database: "csc337imdb_small",
    user: "csc337homer",
    password: "d0ughnut",
    debug: "true"
  });
  con.connect(function(err){
    console.log("connecting to database");
    if (err) {
      console.log("connection error occured.");
      throw err;
      return;
    }
    else {
      console.log("Connected!");
    }

    // MAKE ACTOR ID QUERY
    let actorID;
    con.query(idQuery,
      function(err,result,fields,actorID) {
        if (err) {
          console.log("query error occurred.");
          throw err;
          return;
        } else {
          if (result==undefined) {
            console.log("UNDEFINED QUERY RESULT ~ ACTOR_ID");
          }
          else if(result[0]==undefined) {
            console.log("UNDEFINED RESULT[0]");
          } else {
            console.log(result);
            console.log("result[0]= "+result[0]);
            actorID = result[0]["id"];
          }
        }
      }
    );

    // WRITE SECOND QUERY
    // write query for allMovies:
    let query;
    if (context=="allMovies") {
      query = "SELECT m.name,m.year FROM movies m "+
      "JOIN roles r ON m.id = r.movie_id "+
      "WHERE actor_id = "+actorID+" "+
      "ORDER BY m.year";
    }
    // or for kevinBacon
    else if (context=="kevinBacon") {
      query = "SELECT DISTINCT m.name, m.year FROM actors1 "+
      "JOIN roles r1 ON a1.id = r1.actor_id "+
      "JOIN movies m ON r1.movie_id = m.id "+
      "JOIN roles r2 on r2.movie_id = m.id "+
      "JOIN actors a2 ON r2.actor_id = a2.id "+
      "WHERE a1.id = "+actorID+" AND a2.first_name LIKE 'kevin%' AND a2.last_name LIKE 'bacon'";
    }
    console.log("query = "+query);
    // MAKE SECOND QUERY, SEND RESULTS
    con.query(query,
      function(err,result2,fields,actorID) {
        if (err) {
          console.log("query error occurred.");
          throw err;
          return;
        } else {
          res.send(JSON.stringify(result2));
        }
      }
    );

  });
}

app.listen(3000);
