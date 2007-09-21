
/*
Table struture for mylutece_wsso_user
*/

DROP TABLE IF EXISTS mylutece_wsso_user;
CREATE TABLE mylutece_wsso_user (
  mylutece_wsso_user_id int(11) NOT NULL AUTO_INCREMENT,
  guid varchar(40) NOT NULL default '0',
  last_name varchar(100) NOT NULL default '',
  first_name varchar(100) NOT NULL default '',
  email varchar(150) NOT NULL default '',
  PRIMARY KEY  (mylutece_wsso_user_id)
) TYPE=MyISAM;

/*
Table struture for mylutece_wsso_user_role
*/

DROP TABLE IF EXISTS mylutece_wsso_user_role;
CREATE TABLE mylutece_wsso_user_role (
  mylutece_wsso_user_id int(11) NOT NULL,
  role varchar(50) NOT NULL,
  PRIMARY KEY  (mylutece_wsso_user_id, role)
) TYPE=MyISAM;

