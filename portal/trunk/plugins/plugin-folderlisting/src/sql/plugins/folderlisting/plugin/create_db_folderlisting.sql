--
-- Structure for table `folderlisting_folders`
--

DROP TABLE IF EXISTS `folderlisting_folders`;
CREATE TABLE `folderlisting_folders` (
  `id_folder` int(11) NOT NULL default '0',
  `folder_name` varchar(50) default NULL,
  `folder_path` varchar(255) default NULL,
  `workgroup_key` varchar(50) NOT NULL default 'all',
  PRIMARY KEY  (`id_folder`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `folderlisting_folders` ENABLE KEYS */;


