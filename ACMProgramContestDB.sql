DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS PROBLEM;
DROP TABLE IF EXISTS SUBMISSION;

CREATE TABLE USER
    (uID            INTEGER,
     name           VARCHAR(15)         NOT NULL,
     passwd         VARCHAR(15)         NOT NULL,
     MAC            VARCHAR(17)         DEFAULT NULL,
     isAdmin        BOOL                DEFAULT FALSE,
     PRIMARY KEY (uID),
     UNIQUE (name));
CREATE TABLE PROBLEM
    (probID         INTEGER,
     title          VARCHAR(100)        DEFAULT NULL,
     description    TEXT                DEFAULT NULL,
     pointVal       INT                 DEFAULT 0,
     phase          INT                 NOT NULL,
     "order"	    INT			NOT NULL,
     PRIMARY KEY (probID));
CREATE TABLE SUBMISSION
    (time           INTEGER             NOT NULL,
     uID            INTEGER,
     probID         INTEGER             NOT NULL,
       -- status values should be one of {success,failure}
     status         TEXT 		DEFAULT grading NOT NULL,
     relPathServer  VARCHAR(25)         DEFAULT NULL,
     PRIMARY KEY (time, uID, probID),
     FOREIGN KEY (uID) REFERENCES USER(uID),
     FOREIGN KEY (probID) REFERENCES PROBLEM(probID));

-- insert some users
INSERT INTO USER(uID, name, passwd, isAdmin) VALUES
  (0, "admin", "bluemonkey", 1);
INSERT INTO USER(uID, name, passwd, isAdmin) VALUES
  (1, "user1", "user1", 0);
INSERT INTO USER(uID, name, passwd, isAdmin) VALUES
  (2, "user2", "user2", 0);

-- insert the two basic problems
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (0,
    "Problem0",
    "This problem is very simple, therefore worth few points.",
    5, 0, 1);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (1,
    "Simple Problem 2",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (2,
    "Filler Problem 1",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (3,
    "Filler Problem 2",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (4,
    "Filler Problem 3",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (5,
    "Filler Problem 4",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (6,
    "Filler Problem 5",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (7,
    "Filler Problem 6",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (8,
    "Filler Problem 7",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (9,
    "Filler Problem 8",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (10,
    "Filler Problem 9",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);
INSERT INTO PROBLEM(probID,title,description,pointVal,phase,"order") VALUES
  (11,
    "Filler Problem 10",
    "This problem is similar to the first one, but just a bit harder.",
    7, 0, 2);

-- now time for some fake data!!!
INSERT INTO SUBMISSION(relPathServer, uID, probID, status, time) VALUES
  ("", 1, 0, "failure", 60*60*5);
INSERT INTO SUBMISSION(relPathServer, uID, probID, status, time) VALUES
  ("", 1, 0, "failure", 60*60*12);
INSERT INTO SUBMISSION(relPathServer, uID, probID, status, time) VALUES
  ("", 1, 0, "success", 60*60*20);
INSERT INTO SUBMISSION(relPathServer, uID, probID, status, time) VALUES
  ("", 2, 0, "failure", 60*60*2);
INSERT INTO SUBMISSION(relPathServer, uID, probID, status, time) VALUES
  ("", 2, 1, "success", 60*60*11);
