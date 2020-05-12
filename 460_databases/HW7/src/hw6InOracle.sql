DROP TABLE Account;
DROP TABLE PrimaryAccount;
DROP TABLE DependentAccount;
DROP TABLE Device;
DROP TABLE MediaService;
DROP TABLE Channel;
DROP TABLE SubscribesTo;
DROP TABLE ChannelSelection;


CREATE TABLE Account (
	AccountNumber NUMBER(10),
	Name VARCHAR(255),
	DateOfBirth DATE,
	PRIMARY KEY(AccountNumber)
);

CREATE TABLE PrimaryAccount (
	AccountNumber NUMBER(10),
	StreetNumber VARCHAR(255), /* Since street numbers can have letters in them */
	Address VARCHAR(255),
	ZipCode INTEGER,
	PRIMARY KEY(AccountNumber),
	FOREIGN KEY(AccountNumber) REFERENCES Account
);

CREATE TABLE DependentAccount (
	AccountNumber NUMBER(10),
	DependentName VARCHAR(255),
	PRIMARY KEY(AccountNumber, DependentName),
	FOREIGN KEY(AccountNumber) REFERENCES Account
);

CREATE TABLE Device (
	SerialNumber NUMBER(10),
	PRIMARY KEY(SerialNumber)
);

CREATE TABLE MediaService (
	ServiceName VARCHAR(255),
	MonthlyFee NUMBER(3,2),
	isMusicService CHAR(1) CHECK (isMusicService in ('F','T')),
	isVideoService CHAR(1) check (isVideoService in ('F','T')),
	PRIMARY KEY(ServiceName)
);

CREATE TABLE Channel (
	ChannelName VARCHAR(255),
	ServiceName VARCHAR(255),
	Price NUMBER(3,2),
	isMovie CHAR(1) check (isMovie in ('F','T')),
	inHiDef CHAR(1) check (inHiDef in ('F','T')),
	isSports CHAR(1) check (isSports in ('F','T')),
	isNews CHAR(1) check (isNews in ('F','T')),
	PRIMARY KEY(ServiceName, ChannelName),
	FOREIGN KEY (ServiceName) REFERENCES MediaService
);

CREATE TABLE SubscribesTo (
	AccountNumber NUMBER(10),
	SerialNumber NUMBER(10),
	ServiceName VARCHAR(255),
	PRIMARY KEY (AccountNumber, SerialNumber, ServiceName),
	FOREIGN KEY (AccountNumber) REFERENCES Account,
	FOREIGN KEY (SerialNumber) REFERENCES Device,
	FOREIGN KEY (ServiceName) REFERENCES MediaService
);

CREATE TABLE ChannelSelection (
	AccountNumber NUMBER(10),
	ChannelName VARCHAR(255),
	ServiceName VARCHAR(255),
	PRIMARY KEY (AccountNumber, ServiceName, ChannelName),
	FOREIGN KEY (AccountNumber) REFERENCES Account,
	FOREIGN KEY (ServiceName, ChannelName) REFERENCES Channel
);
