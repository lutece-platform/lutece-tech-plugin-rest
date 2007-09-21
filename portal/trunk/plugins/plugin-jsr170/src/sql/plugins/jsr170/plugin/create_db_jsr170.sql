--
-- Structure for table `jsr170_view`
--
DROP TABLE IF EXISTS `jsr170_view`;
CREATE TABLE  `jsr170_view` (
  `id_view` int(10) unsigned NOT NULL default '0',
  `id_workspace` int(10) unsigned NOT NULL default '0',
  `workgroup` varchar(45) NOT NULL default '',
  `view_name` varchar(45) NOT NULL default '',
  `path` varchar(128) default NULL,
  PRIMARY KEY  (`id_view`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Structure for table `jsr170_workspace`
--

DROP TABLE IF EXISTS `jsr170_workspace`;
CREATE TABLE  `jsr170_workspace` (
  `id_workspace` int(10) unsigned NOT NULL default '0',
  `workspace_name` varchar(45) NOT NULL default '',
  `workgroup` varchar(45) NOT NULL default '',
  `jcr_type` varchar(45) NOT NULL default '',
  `workspace_label` varchar(45) NOT NULL default '',
  `user` varchar(45) NOT NULL default '',
  `password` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`id_workspace`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Structure for table `jsr170_view_role`
--

DROP TABLE IF EXISTS `jsr170_view_role`;
CREATE TABLE  `jsr170_view_role` (
  `id_view` int(10) unsigned NOT NULL default '0',
  `access_right` varchar(45) NOT NULL default '',
  `role` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`id_view`,`access_right`,`role`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
