//testA_node.js

/*
* search for names containing a certain letter a certain # of times
* case-insensitive search
* text file: peeps.txt containing first and last names, space del.
* query param: letter ; times
*/

let express = require("express");
let fs = require("fs");
let app = express();

app.get('/',function(req,res) {
    let letter = req.query.letter;
    let times = req.query.times;
    if (letter == undefined || times == undefined || times<0 ||
        letter.length < 0 || letter.length > 1) {
        res.send(400);
        return;
    }
    let trimmedAnswer = letter.match(/[a-zA-Z]/);
    if (trimmedAnswer == "" || trimmedAnswer == null) {
        res.send(400);
        return;
    }
    let solution = {
        "names":[]
    };
    let fileContents = fs.readFileSync("peeps.txt",'utf8');
    let fileContentsArray = fileContents.split("\n");
    for (let i = 0; i< fileContentsArray.length; i++) {
        let counter = 0;
        let line = fileContentsArray[i];
        let lineArray = line.split(" ");
        for (let k = 0; k<2; k++) {
            let name = lineArray[k];
            for (let j = 0; j<name.length; j++) {
                if (name.charAt(j)==letter) {
                    counter++;
                }
            }
        }
        if (counter >= times) {
            let entry = {
                "name": line,
                "count": counter
            };
            solution.names.push(entry);
        }
    }
    let data = JSON.stringify(solution);
    res.send(data);

});
