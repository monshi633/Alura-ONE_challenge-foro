CREATE TABLE answers
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   body VARCHAR (5000) NOT NULL,
   creation_date DATETIME NOT NULL,
   last_updated DATETIME NOT NULL,
   solution TINYINT NOT NULL,
   deleted TINYINT NOT NULL,
   user_id BIGINT NOT NULL,
   topic_id BIGINT NOT NULL,
   PRIMARY KEY (id)
);