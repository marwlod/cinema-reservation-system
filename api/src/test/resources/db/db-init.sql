CREATE SCHEMA IF NOT EXISTS crs;
CREATE USER 'api'@'%' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON crs.* to 'api'@'%';

USE crs;

SOURCE /docker-entrypoint-initdb.d/sql/create_tables.sql;
SOURCE /docker-entrypoint-initdb.d/sql/insert_data.sql;
