--
-- Structure for table `library_mapping`
--

DROP TABLE IF EXISTS `library_mapping`;
CREATE TABLE `library_mapping` (
  `id_mapping` int(10) NOT NULL default '0',
  `id_media` int(10) NOT NULL default '0',
  `code_document_type` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`id_mapping`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



--
-- Structure for table `library_mapping_attribute`
--

DROP TABLE IF EXISTS `library_mapping_attribute`;
CREATE TABLE `library_mapping_attribute` (
  `id_mapping` int(10) NOT NULL default '0',
  `id_media_attribute` int(10)  NOT NULL default '0',
  `id_document_attribute` int(10)  NOT NULL default '0',
  PRIMARY KEY  (`id_mapping`,`id_media_attribute`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Structure for table `library_media`
--

DROP TABLE IF EXISTS `library_media`;
CREATE TABLE `library_media` (
  `id_media` int(10) NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  `description` varchar(100) NOT NULL default '',
  `stylesheet` longblob,
  PRIMARY KEY  (`id_media`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `library_media_attribute`
--

DROP TABLE IF EXISTS `library_media_attribute`;
CREATE TABLE `library_media_attribute` (
  `id_attribute` int(10) NOT NULL default '0',
  `id_media` int(10) unsigned NOT NULL default '0',
  `code` varchar(45) NOT NULL default '',
  `description` varchar(100) NOT NULL default '',
  `type` smallint(5) unsigned NOT NULL default '0',
  `default_value` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`id_attribute`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


