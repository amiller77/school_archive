/*
* BEST READS SERVICE JS
* Description: JS file that hosts a service, returning book info upon queries by user
* Author: Alexander Miller
* Course: CSC-337
* Date: Oct 22, 2018
*/

// BASIC SETUP
// basic setup, package imports, etc. :
const express = require("express");
const app = express();
const fs = require("fs");
app.use(express.static('public'));

/*
* GET
* Description: Fields user queries
* Query Parameters:
* -> mode: specifies the type of data to return
* -> title: specifies the name of the folder where the book is held
*/
app.get('/',function(req,res) {
  res.header("Access-Control-Allow-Origin","*");
  let mode = req.query.mode
  let book = req.query.title;
  // have JS object to hold our data
  let data = {};
  if (mode == "books") {
    data = booksMode(data);
  } else if (mode == "reviews") {
    data = reviewsMode(data, book);
  } else if (mode == "description") {
    data = descriptionMode(book);
  } else if (mode == "info") {
    data = infoMode(data, book);
  }
  res.send(JSON.stringify(data));
})

/*
* BOOKS MODE
* returning JS object containing all the book titles and their folders
* note: also returning image source for the browser side to use to depict imgs
*/
function booksMode(data) {
  // instantiate an array called "books" as data attribute
  data["books"]=[];
  // open the book directory, and iterate over inside folders:
  let folders = fs.readdirSync("books");
  for (let i = 0; i<folders.length; i++) {
    // open the inside folder, and iterate over files to find info.txt
    // note: iterating to verify we don't get an error if it doesn't exist.
    let folderUrl = "books/"+folders[i];
    let stats = fs.statSync(folderUrl);
    if (stats.isDirectory()) {
      let files = fs.readdirSync(folderUrl);
      for (let k = 0; k < files.length; k++) {
        if (files[k]=="info.txt") {
          let fileContents = fs.readFileSync("books/"+folders[i]+"/info.txt","utf8");
          let fileContentArray = fileContents.split("\n");
          // add JSON object to data book list
          data.books.push({
            title: fileContentArray[0].trim("\r"),
            folder: folders[i],
            img: "books/"+folders[i]+"/cover.jpg"
          });
          break;
        }
      }

    }
  }
  return data;
}

/*
* REVIEWS MODE
* returning a JS object containing all the reviews for the book
*/
function reviewsMode(data, book) {
  // have JS object to hold our data
  data["reviews"]=[];
  // open the book directory, then folder
  let files = fs.readdirSync("books/"+book);
  for (let i =0; i< files.length; i++) {
    if (files[i].startsWith("review") && files[i].endsWith("txt")) {
      let fileContents = fs.readFileSync("books/"+book+"/"+files[i],"utf8");
      let fileContentArray = fileContents.split("\n");
      // encapsulate data in JS object
      data["reviews"].push({
        name: fileContentArray[0].trim("\r"),
        stars: fileContentArray[1].trim("\r"),
        review: fileContentArray[2].trim("\r")
      });
    }
  }
  return data;
  }


/*
* DESCRIPTION MODE
* this time we are outputting our data as plain text rather than JSON
*/
function descriptionMode(book) {
  // open the book and folder directories, then the "description.txt" file
  let fileContents = fs.readFileSync("books/"+book+"/description.txt","utf8");
  return fileContents.trim("\n").trim("\r");
}

/*
* INFO MODE
* returning a JS object containing the title, author, and stars of the book
*/
function infoMode(data, book) {
  // open the book directory
  let fileContents = fs.readFileSync("books/"+book+"/info.txt","utf8");
  let fileContentsArray = fileContents.split("\n");
  data["title"]=fileContentsArray[0].trim("\r");
  data["author"]=fileContentsArray[1].trim("\r");
  data["stars"]=fileContentsArray[2].trim("\r");
  return data;
}


// listen:
app.listen(3000);
