// CSC 337 Project 7 Gerrymandering
// Provided web service
// districts.txt and eligible-voters.txt must be in the same directory for this to work

// DO NOT MODIFY!
// We will use this exact service to test your code.

const express = require("express");
const app = express();
var fs = require('fs');

app.use(express.static('public'));

// reads data asynchronously fromt he passed in file name
// returns the contents of the file as a string
function read_file(file_name) {
	var districts = 0;
	try {
	    districts = fs.readFileSync(file_name, 'utf8');
	    console.log(districts);
	} catch(e) {
	    console.log('Error:', e.stack);
	}
	return districts;
}

// builds a JSON representation of the district data
// containing a state element and a districts element that stores
// a list of lists where each inner list contains the democratic votes and then the
// republican votes from a district.
// takes a line of data as a parameter
// returns this JSON.
function build_json(line) {
	line = line.split(",");
	var data = {"state":line[0]}
	var lis = [];
	var ind = 0;
	for (var i = 1; i < line.length; i += 3) {
		lis[ind] = [parseInt(line[i + 1]), parseInt(line[i + 2])];
		ind += 1;
	}
	data["districts"] = lis;
	return data
}

// takes a string with all state data as a parameter
// splits it into individual lines and then returns the line that contains
// the passed in state name as its first element.
// returns an empty string if it doesn't find the state.
function find_line(districts, state) {
	var lines = districts.split("\n");
	var line = "";
	for (var i = 0; i < lines.length; i++) {
		if(lines[i].toLowerCase().includes(state.toLowerCase())){
			line = lines[i];
		}
	}
	return line;
}

console.log('web service started');
app.get('/', function (req, res) {
	res.header("Access-Control-Allow-Origin", "*");

	var state = req.query.state;
	var type = req.query.type;

	// returns an error if one of the parameters is missing
	if(state == undefined || type == undefined) {
		res.status(400);
		res.send("Missing required parameters");
	}

	var file_name = "eligible_voters.txt";
	if(type == "districts") {
		file_name = "districts.txt";
	}
	var districts = read_file(file_name);
	var line = find_line(districts, state);

	// returns an error if the state wasn't found.
	if(line == "") {
		res.status(410);
		res.send("State was not found");
	}

	if(type == "districts") {
		line = build_json(line);
	} else {
		line = parseInt(line.split(",")[1]);
	}

    res.send(JSON.stringify(line));

})

app.listen(3000);
