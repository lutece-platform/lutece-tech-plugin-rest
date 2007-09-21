
--
-- Table struture for mylutece_ldapdatabase_user
--

drop table if exists mylutece_ldapdatabase_user;
CREATE TABLE mylutece_ldapdatabase_user (
  mylutece_ldapdatabase_user_id int(11) NOT NULL AUTO_INCREMENT,
  ldap_guid varchar(40) NOT NULL default '',
  name_given varchar(50) NOT NULL default '',
  name_family varchar(50) NOT NULL default '',
  email varchar(100) default NULL,
  PRIMARY KEY  (mylutece_ldapdatabase_user_id)
) TYPE=MyISAM;

--
-- Table struture for mylutece_ldapdatabase_user_role
--

drop table if exists mylutece_ldapdatabase_user_role;
CREATE TABLE mylutece_ldapdatabase_user_role (
  mylutece_ldapdatabase_user_id int(11) NOT NULL,
  role_key varchar(50) NOT NULL default '',
  PRIMARY KEY  (mylutece_ldapdatabase_user_id,role_key)
) TYPE=MyISAM;
