--
-- Structure for table `poll`
--

DROP TABLE IF EXISTS `poll`;
CREATE TABLE `poll` (
  `id_poll` int(11) NOT NULL default '0',
  `name` varchar(250) default NULL,
  `status` int(1) NOT NULL default '0',
  `workgroup_key` varchar(50) NOT NULL default 'all',
  PRIMARY KEY  (`id_poll`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `poll_choice`
--

DROP TABLE IF EXISTS `poll_choice`;
CREATE TABLE `poll_choice` (
  `id_choice` int(11) NOT NULL default '0',
  `id_question` int(11) NOT NULL default '0',
  `label_choice` varchar(250) default NULL,
  `score` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id_choice`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `poll_question`
--

DROP TABLE IF EXISTS `poll_question`;
CREATE TABLE `poll_question` (
  `id_question` int(11) NOT NULL default '0',
  `id_poll` int(11) NOT NULL default '0',
  `label_question` varchar(250) default NULL,
  PRIMARY KEY  (`id_question`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
