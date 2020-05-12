CREATE TABLE Location (
	locName VARCHAR(30),
	PRIMARY KEY (locName)
);

INSERT INTO Location (locName)
VALUES ('Paris');


SELECT locName
FROM Location;


DROP TABLE Location;
