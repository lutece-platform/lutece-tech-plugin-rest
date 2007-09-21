DROP DATABASE IF EXISTS election;
CREATE DATABASE election;
USE election;

CREATE TABLE number_suffix (
	number_suffix VARCHAR(9) PRIMARY KEY NOT NULL,
	number_suffix_label VARCHAR(9)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE polling_station (
	polling_station_id INT(4) PRIMARY KEY NOT NULL,
	polling_station_urban_district VARCHAR(2),
	polling_station_number VARCHAR(3),
	polling_station_name VARCHAR(255),
	polling_station_location_complement VARCHAR(255),
	polling_station_addr_number VARCHAR(10),
	polling_station_addr VARCHAR(255),
	polling_station_addr_urban_district VARCHAR(15)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE street_type (
	short_street_type VARCHAR(3) PRIMARY KEY NOT NULL,
	long_street_type VARCHAR(255)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE street (
	street_id VARCHAR(5)  NOT NULL,
	street_urban_district VARCHAR(2),
	long_street_name VARCHAR(255),
	short_street_type VARCHAR(3) NOT NULL,
	short_street_name VARCHAR(255) NOT NULL,
	PRIMARY KEY (street_id,street_urban_district)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE address (
	addr_id INT(6) PRIMARY KEY NOT NULL,
	addr_urban_district VARCHAR(2),
	addr_number VARCHAR(10) NOT NULL,
	addr_number_suffix VARCHAR(9),
	street_id VARCHAR(5) NOT NULL,
	addr_street_name VARCHAR(255),
	polling_station_id INT(4) NOT NULL,
	FOREIGN KEY (street_id) REFERENCES street(street_id),
	FOREIGN KEY (polling_station_id) REFERENCES polling_station(polling_station_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;