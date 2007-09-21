--
-- Table structure for table `myapps_application`
--

DROP TABLE IF EXISTS `myapps_application`;
CREATE TABLE `myapps_application` (
  `id_application` tinyint(4) NOT NULL default '0',
  `name` tinytext,
  `description` tinytext,
  `url` tinytext,
  `code` tinytext,
  `password` tinytext,
  `data` tinytext,
  `code_heading` tinytext,
  `data_heading` tinytext,
  `icon_content` longblob,
  `icon_mime_type` varchar(255) default NULL,
  PRIMARY KEY  (`id_application`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `myapps_application`
--

LOCK TABLES `myapps_application` WRITE;
/*!40000 ALTER TABLE `myapps_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `myapps_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myapps_user`
--

DROP TABLE IF EXISTS `myapps_user`;
CREATE TABLE `myapps_user` (
  `id_user` tinyint(4) NOT NULL default '0',
  `name` tinytext,
  `id_application` tinyint(4) default NULL,
  `stored_user_name` tinytext,
  `stored_user_password` tinytext,
  `stored_user_data` tinytext,
  PRIMARY KEY  (`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `myapps_user`
--

LOCK TABLES `myapps_user` WRITE;
/*!40000 ALTER TABLE `myapps_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `myapps_user` ENABLE KEYS */;
UNLOCK TABLES;

