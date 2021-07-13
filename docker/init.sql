/* Create the database */
CREATE OR REPLACE DATABASE minecraft;

/* Create the table containing the players data */
CREATE TABLE IF NOT EXISTS minecraft.players (
    id int(11) NOT NULL AUTO_INCREMENT,
    identifier varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    uuid varchar(255) DEFAULT NULL,
    username varchar(255) NOT NULL,
    banned tinyint(1) DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uuid (uuid),
    UNIQUE KEY unique_identifier (identifier),
    UNIQUE KEY unique_username (username)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
