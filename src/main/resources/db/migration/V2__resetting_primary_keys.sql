ALTER TABLE ASSESSMENT
DROP PRIMARY KEY;

ALTER TABLE ASSESSMENT
DROP COLUMN id;

ALTER TABLE ASSESSMENT
ADD PRIMARY KEY (identifier);

ALTER TABLE USER
DROP PRIMARY KEY;

ALTER TABLE USER
MODIFY COLUMN id INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE COURSE
DROP PRIMARY KEY;

ALTER TABLE COURSE
MODIFY COLUMN id INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE PROJECTION
DROP PRIMARY KEY;

ALTER TABLE PROJECTION
MODIFY COLUMN id INT AUTO_INCREMENT PRIMARY KEY;

