HW6 SQL SOURCE

2.

CREATE TABLE Account (
    AccountNumber INTEGER,
    Name VARCHAR(30),
    DateOfBirth DATE,
    StreetNumber INTEGER,
    Address VARCHAR(30),
    Zipcode INTEGER,
    PRIMARY KEY (AccountNumber)
);

CREATE TABLE Device (
    SerialNumber INTEGER,
    PRIMARY KEY (SerialNumber)
);

CREATE TABLE MediaService (
    ServiceName VARCHAR(30),
    MonthlyFee FLOAT(10),
    HasMusic INTEGER,
    HasVideo INTEGER,
    PRIMARY KEY (ServiceName)
);

CREATE TABLE Subscribes (
    AccountNumber INTEGER,
    SerialNumber INTEGER,
    ServiceName VARCHAR(30),
    FOREIGN KEY (AccountNumber) REFERENCES Account,
    FOREIGN KEY (SerialNumber) REFERENCES Device,
    FOREIGN KEY (ServiceName) REFERENCES MediaService
);

CREATE TABLE Channel (
    ServiceName VARCHAR(30),
    ChannelName VARCHAR(30),
    Price FLOAT(10),
    HasMovies INTEGER,
    HasSports INTEGER,
    HasNews INTEGER,
    IsHiDef INTEGER,
    PRIMARY KEY (ServiceName, ChannelName),
    FOREIGN KEY (ServiceName) REFERENCES MediaService
);



3.

INSERT INTO Account VALUES(1,'John Smith','17-APR-74',8,'Ashmead Place',72314);
INSERT INTO Account VALUES(2,'Jane Smith','10-MAY-78',8,'Ashmead Place',72314);
INSERT INTO Account VALUES(3,'Monika Brown','4-NOV-94',21,'Dorceaster Road',84511);
INSERT INTO Account VALUES(4,'Trisha Bimbo','10-AUG-50',4,'Marion Street',44009);
INSERT INTO Account VALUES(5,'Jack Taylor','30-JUN-60',12,'Guards Street',11490);

INSERT INTO Device VALUES(123456);
INSERT INTO Device VALUES(234567);
INSERT INTO Device VALUES(345678);
INSERT INTO Device VALUES(456789);
INSERT INTO Device VALUES(567890);

INSERT INTO MediaService VALUES('AmazonPrime',10,1,1);
INSERT INTO MediaService VALUES('AppleTV',20,0,1);
INSERT INTO MediaService VALUES('Spotify',5,1,0);
INSERT INTO MediaService VALUES('Youtube',0,1,1);
INSERT INTO MediaService VALUES('Cox',79,0,1);

INSERT INTO Channel VALUES('AmazonPrime','KidsTV',5,1,0,0,1);
INSERT INTO Channel VALUES('AppleTV','PopularFilms',10,1,0,0,1);
INSERT INTO Channel VALUES('Youtube','Backyard Shows',2,1,1,1,0);
INSERT INTO Channel VALUES('Cox','CBS',12,0,1,1,0);
INSERT INTO Channel VALUES('AmazonPrime','HBO',15,1,0,0,1);

INSERT INTO Subscribes VALUES(1,123456,'AmazonPrime');
INSERT INTO Subscribes VALUES(2,234567,'Youtube');
INSERT INTO Subscribes VALUES(3,345678,'Cox');
INSERT INTO Subscribes VALUES(4,456789,'Spotify');
INSERT INTO Subscribes VALUES(5,567890,'AppleTV');


4.

SELECT Name
FROM Account, Subscribes
WHERE SerialNumber = 345678 AND Account.AccountNumber = Subscribes.AccountNumber;

SELECT ChannelName
FROM Account, Subscribes, MediaService, Channel
WHERE Name = 'John Smith'
    AND Account.AccountNumber = Subscribes.AccountNumber
    AND Subscribes.ServiceName = MediaService.ServiceName
    AND Channel.ServiceName = MediaService.ServiceName;


5.

DROP TABLE Channel;
DROP TABLE Subscribes;
DROP TABLE Account;
DROP TABLE Device;
DROP TABLE MediaService;
