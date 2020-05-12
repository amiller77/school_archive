1.

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

2.

INSERT INTO Location VALUES(Paris,45,20,350);
INSERT INTO Location VALUES(London,60,40,20);
INSERT INTO Location VALUES(Amsterdam,30,80,0);
INSERT INTO Location VALUES(Berlin,20,20,500);
INSERT INTO Location VALUES(Zurich,15,45,2500);
INSERT INTO Location VALUES(Edinburg,23,34,100);
INSERT INTO Location VALUES(York,33,50,300);
INSERT INTO Location VALUES(Rheims,20,10,350);
INSERT INTO Location VALUES(Hamburg,40,50,20);
INSERT INTO Location VALUES(Strasbourg,10,10,500);
INSERT INTO Location VALUES(Nancy,12,12,450);
INSERT INTO Location VALUES(Munich, 4, 80, 1250);

INSERT INTO BikeRace VALUES(Tour de France, Paris, Paris, 500, TO_DATE(‘8:00 A.M.’,’HH:MI A.M.’), 2019);
INSERT INTO BikeRace VALUES(Tour de Saxony, Berlin, Amsterdam, 450, TO_DATE(‘10:00 A.M.’,’HH:MI A.M.’), 2018);
INSERT INTO BikeRace VALUES(Tour de Rhein, Amsterdam, Zurich, 600, TO_DATE(‘12:00 P.M.’,’HH:MI P.M.’), 2017);
INSERT INTO BikeRace VALUES(Tour de Britain, London, Edinburgh, 430, TO_DATE(‘7:30 A.M.’,’HH:MI A.M.’), 2016);
INSERT INTO BikeRace VALUES(Tour de Continent, Zurich, Zurich, 1350, TO_DATE(‘6:00 A.M.’,’HH:MI A.M.’), 2014);

INSERT INTO Segment VALUES(Tour de France, Paris, Rheims, 100);
INSERT INTO Segment VALUES(Tour de France, Rheims, Strasbourg, 200);
INSERT INTO Segment VALUES(Tour de France, Strasbourg, Nancy, 100);
INSERT INTO Segment VALUES(Tour de France, Nancy, Paris, 100);

INSERT INTO Segment VALUES(Tour de Saxony, Berlin, Hamburg, 200);
INSERT INTO Segment VALUES(Tour de Saxony, Hamburg, Amsterdam, 250);

INSERT INTO Segment VALUES(Tour de Rhein, Amsterdam, Strasbourg, 250);
INSERT INTO Segment VALUES(Tour de Rhein, Strasbourg, Zurich, 350);

INSERT INTO Segment VALUES(Tour de Britain, London, York, 180);
INSERT INTO Segment VALUES(Tour de Britain, York, Edinburg, 250);

INSERT INTO Segment VALUES(Tour de Continent, Zurich, Strasbourg, 350);
INSERT INTO Segment VALUES(Tour de Continent, Strasbourg, Hamburg, 250);
INSERT INTO Segment VALUES(Tour de Continent, Hamburg, Berlin, 200);
INSERT INTO Segment VALUES(Tour de Continent, Berlin, Munich, 300);
INSERT INTO Segment VALUES(Tour de Continent, Munich, Zurich, 250);


3.

SELECT raceName
FROM BikeRace
WHERE startLoc = endLoc;

Attempt B1.
(
(SELECT DISTINCT locName
FROM Location)
EXCEPT
(SELECT fromLoc
FROM Segment s1 LEFT OUTER JOIN Segment s2 ON s1.fromLoc = s2.fromLoc
WHERE s1.raceName <> s2.raceName)
EXCEPT
(SELECT nextLoc
FROM Segment s3 LEFT OUTER JOIN Segment s4 ON s3.nextLoc = s4.nextLoc
WHERE s3.raceName <> s4.raceName)
);

Attempt B2.
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
	FROM Segment s1 LEFT OUTER JOIN Segment s2 ON s1.nextLoc = s2.nextLoc
	WHERE s3.raceName <> s4.raceName
) OR l2.locName IN (
	SELECT
	FROM Segment s5 LEFT OUTER JOIN Segment s6 ON s5.fromLoc = s
);



SELECT DISTINCT raceName
FROM Segment
WHERE distance > 2*( SELECT AVG(distance) FROM Segment );




4.

DROP TABLE Segment;
DROP TABLE BikeRace;
DROP TABLE Location;

/////////////////////////////////////////////////////////
Notes:
- not sure about time declarations
- be sure to remove any comments or question numbers, and reorder queries
