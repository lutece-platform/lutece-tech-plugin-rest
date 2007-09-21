--
-- Table struture for `mylutece_database_user`
--

DROP TABLE IF EXISTS `mylutece_database_user`;
CREATE TABLE `mylutece_database_user` (
  `mylutece_database_user_id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(100) NOT NULL default '',
  `password` varchar(100) NOT NULL default '',
  `name_given` varchar(100) NOT NULL default '',
  `name_family` varchar(100) NOT NULL default '',
  `email` varchar(100) default NULL,
  PRIMARY KEY  (`mylutece_database_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table struture for `mylutece_database_user_role`
--

DROP TABLE IF EXISTS `mylutece_database_user_role`;
CREATE TABLE `mylutece_database_user_role` (
  `mylutece_database_user_id` int(11) NOT NULL default '0',
  `role_key` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`mylutece_database_user_id`,`role_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table struture for `mylutece_database_user_group`
--
DROP TABLE IF EXISTS `mylutece_database_user_group`;
CREATE TABLE `mylutece_database_user_group` (
  `mylutece_database_user_id` int(11) NOT NULL default '0',
  `group_key` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`mylutece_database_user_id`,`group_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;