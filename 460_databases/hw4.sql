DROP TABLE Segment;
DROP TABLE BikeRace;
DROP TABLE Location;




CREATE TABLE Location (
	locName VARCHAR(30),
	latitude FLOAT(20),
	longitude FLOAT(20),
	elevation FLOAT(20),
	PRIMARY KEY (locName)
);

CREATE TABLE BikeRace (
	raceName VARCHAR(30),
	startLoc VARCHAR(30),
	endLoc VARCHAR(30),
	totalMiles FLOAT(10),
	startTime DATE,
	year INTEGER,
	PRIMARY KEY (raceName),
	FOREIGN KEY (startLoc) REFERENCES Location,
	FOREIGN KEY (endLoc) REFERENCES Location
);

CREATE TABLE Segment (
	raceName VARCHAR(30),
	fromLoc VARCHAR(30),
	nextLoc VARCHAR(30),
	distance FLOAT(10),
	PRIMARY KEY (raceName, fromLoc),
	FOREIGN KEY (raceName) REFERENCES BikeRace,
	FOREIGN KEY (fromLoc) REFERENCES Location,
	FOREIGN KEY (nextLoc) REFERENCES Location
);




INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Paris',45,20,350);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('London',60,40,20);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Amsterdam',30,80,0);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Berlin',20,20,500);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Zurich',15,45,2500);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Edinburgh',23,34,100);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('York',33,50,300);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Rheims',20,10,350);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Hamburg',40,50,20);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Strasbourg',10,10,500);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Nancy',12,12,450);
INSERT INTO Location(locName, latitude, longitude, elevation) VALUES('Munich', 4, 80, 1250);


INSERT INTO BikeRace(raceName, startLoc, endLoc, totalMiles, startTime, year)
VALUES('Tour_de_France', 'Paris', 'Paris', 500, TO_DATE('8:00 A.M.','HH:MI A.M.'), 2019);

INSERT INTO BikeRace(raceName, startLoc, endLoc, totalMiles, startTime, year)
VALUES('Tour_de_Saxony', 'Berlin', 'Amsterdam', 450, TO_DATE('10:00 A.M.','HH:MI A.M.'), 2018);

INSERT INTO BikeRace(raceName, startLoc, endLoc, totalMiles, startTime, year)
VALUES('Tour_de_Rhein', 'Amsterdam', 'Zurich', 600, TO_DATE('12:00 P.M.','HH:MI P.M.'), 2017);

INSERT INTO BikeRace(raceName, startLoc, endLoc, totalMiles, startTime, year)
VALUES('Tour_de_Britain', 'London', 'Edinburgh', 430, TO_DATE('7:30 A.M.','HH:MI A.M.'), 2016);

INSERT INTO BikeRace(raceName, startLoc, endLoc, totalMiles, startTime, year)
VALUES('Tour_de_Continent', 'Zurich', 'Zurich', 1600, TO_DATE('6:00 A.M.','HH:MI A.M.'), 2014);



INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_France', 'Paris', 'Rheims', 100);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_France', 'Rheims', 'Strasbourg', 200);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_France', 'Strasbourg', 'Nancy', 100);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_France', 'Nancy', 'Paris', 100);


INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Saxony', 'Berlin', 'Hamburg', 200);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Saxony', 'Hamburg', 'Amsterdam', 250);


INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Rhein', 'Amsterdam', 'Strasbourg', 250);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Rhein', 'Strasbourg', 'Zurich', 350);


INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Britain', 'London', 'York', 180);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Britain', 'York', 'Edinburgh', 250);


INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Continent', 'Zurich', 'Berlin', 1200);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Continent', 'Berlin', 'Hamburg', 100);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Continent', 'Hamburg', 'Strasbourg', 100);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Continent', 'Strasbourg', 'Munich', 100);

INSERT INTO Segment(raceName, fromLoc, nextLoc, distance)
VALUES('Tour_de_Continent', 'Munich', 'Zurich', 100);





SELECT raceName
FROM BikeRace
WHERE startLoc = endLoc;



SELECT l1.locName
FROM Location l1
MINUS
SELECT l2.locName
FROM Location l2
WHERE l2.locName IN (
	SELECT s1.fromLoc
	FROM Segment s1 LEFT OUTER JOIN Segment s2 ON s1.fromLoc = s2.fromLoc
	WHERE s1.raceName <> s2.raceName
) OR l2.locName IN (
	SELECT s1.nextLoc
	FROM Segment s1 LEFT OUTER JOIN Segment s2 ON s1.nextLoc = s2.fromLoc
	WHERE s1.raceName <> s2.raceName
) OR l2.locName IN (
	SELECT s1.fromLoc
	FROM Segment s1 LEFT OUTER JOIN Segment s2 ON s1.fromLoc = s2.nextLoc
	WHERE s1.raceName <> s2.raceName
) OR l2.locName IN (
	SELECT s1.nextLoc
	FROM Segment s1 LEFT OUTER JOIN Segment s2 ON s1.nextLoc = s2.nextLoc
	WHERE s1.raceName <> s2.raceName
);


SELECT DISTINCT raceName
FROM Segment
WHERE distance > 2*( SELECT AVG(distance) FROM Segment );




DROP TABLE Segment;
DROP TABLE BikeRace;
DROP TABLE Location;
