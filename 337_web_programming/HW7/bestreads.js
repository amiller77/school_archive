/*
* BEST READS JS
* Description: Browser-side code that queries bestreads_service.js in order
* to update bestreads.html; also performs some event-handling for user interface
*   -> on load:
*       fetch all books from web service, display each cover and titles
*   -> event-handlers:
*       on home clicked -> launch "on load" ; ensure multiple clicks doesn't break
*       on book clicked -> wipe, page, show only single book, request and display
          info, description, and reviews for that book
* Author: Alexander Miller
* Course: CSC-337
* Date: Oct 22, 2018
*/
"use strict";
( function () {

  // START UP
  // all on load functionality: setting event-handlers, resetting page, etc.
  function startup() {
    // set event-handlers:
    document.getElementById("back").onclick = startup;
    // clear all books, hide single book div, show all books
    let allBooks = document.getElementById("allbooks");
    allBooks.innerHTML="";
    allBooks.style.visibility="visible";
    document.getElementById("singlebook").style.visibility="hidden";
    // make server query:
    getBookList();
  }

  // GET BOOK LIST
  // queries the server for the books and their images; uploads the page with them
  function getBookList() {
    fetch("http://localhost:3000/?mode=books")
      .then(checkStatus)
      .then(function(responseText) {
        // create a div for each book, add to page with title and img
        let data = JSON.parse(responseText);
        for (let i = 0; i<data.books.length;i++) {
          // create cover photo:
          let coverPhoto = document.createElement("img");
          coverPhoto.src=data.books[i].img;
          coverPhoto.alt="Cover Photo";
          // create title paragraph:
          let titleParagraph = document.createElement("p");
          titleParagraph.innerHTML=data.books[i].title;
          // create bookDiv and add paragraph and photo:
          let bookDiv = document.createElement("div");
          bookDiv.id=data.books[i].folder+" "+coverPhoto.src;
          bookDiv.appendChild(coverPhoto);
          bookDiv.appendChild(titleParagraph);
          // add event-handler to bookDiv
          bookDiv.onclick = bookClickHandler;
          // add bookDiv to All Books
          document.getElementById("allbooks").appendChild(bookDiv);
        }
      })
      .catch(function(error) {
      });
  }

  // BOOK CLICK HANDLER
  // event handler for books on the page being clicked
  // when clicked -> wipes the page, and calls methods to query and display for next stage
  function bookClickHandler() {
    // folder name and picture src from div before deleting it
    let bookInfo = this.id.split(" ");
    let folderName = bookInfo[0];
    let imgSrc = bookInfo[1];
    // add the pic to the single book div; clear reviews; show:
    document.getElementById("reviews").innerHTML="";
    document.getElementById("cover").src=imgSrc;
    document.getElementById("singlebook").style.visibility="visible";
    // clear and hide the all books div:
    let allBooks = document.getElementById("allbooks");
    allBooks.innerHTML="";
    allBooks.style.visibility="hidden";
    // query the server for the last information we need:
    getDescription(folderName);
    getInfo(folderName);
    getReviews(folderName);
  }

  // GET DESCRIPTION
  // queries the server for the book description; adds to page
  function getDescription(folderName) {
    fetch("http://localhost:3000/?mode=description&title="+folderName)
      .then(checkStatus)
      .then(function(responseText) {
        document.getElementById("description").innerHTML=responseText;
      })
      .catch(function(error) {
      });
  }

  // GET INFO
  // queries the server for the book title, author, ^ stars; adds to page
  function getInfo(folderName) {
    fetch("http://localhost:3000/?mode=info&title="+folderName)
      .then(checkStatus)
      .then(function(responseText) {
        let data = JSON.parse(responseText);
        document.getElementById("title").innerHTML=data.title;
        document.getElementById("author").innerHTML=data.author;
        document.getElementById("stars").innerHTML=data.stars;
      })
      .catch(function(error) {
      });
  }

  // GET REVIEWS
  // queries the server for the book reviews; adds to page
  function getReviews(folderName) {
    fetch("http://localhost:3000/?mode=reviews&title="+folderName)
      .then(checkStatus)
      .then(function(responseText) {
        let reviews = JSON.parse(responseText).reviews;
        for (let i = 0; i<reviews.length; i++) {
          // put the stars in span
          let span = document.createElement("span");
          span.innerHTML = " "+reviews[i].stars;
          // put reviewer in h3; add span to h3
          let h3 = document.createElement("h3");
          h3.innerHTML=reviews[i].name;
          h3.appendChild(span);
          // put review in paragraph
          let paragraph = document.createElement("p");
          paragraph.innerHTML = reviews[i].review;
          // add to the reviews div
          document.getElementById("reviews").appendChild(h3);
          document.getElementById("reviews").appendChild(paragraph);
        }
      })
      .catch(function(error) {
      });
  }


  // CHECK STATUS
  // status checker for server queries
  function checkStatus(response) {
    if (response.status >= 200 && response.status<300) {
      return response.text();
    } else {
      return Promise.reject(new Error(response.status+":"+response.statusText));
    }
  }

  window.onload = startup;

})();
