CREATE SCHEMA IF NOT EXISTS crs;
CREATE USER 'api'@'%' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON crs.* to 'api'@'%';

USE crs;

CREATE TABLE ala
(
    ma int NOT NULL,
    kota int NOT NULL,
    PRIMARY KEY (ma)
);

INSERT INTO ala(ma, kota) VALUES (666, 777);
