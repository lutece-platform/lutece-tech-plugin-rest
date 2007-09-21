--
-- Structure for table `reporting_project`
--

DROP TABLE IF EXISTS `reporting_project`;
CREATE TABLE `reporting_project` (
  `id_project` int(11) NOT NULL default '0',
  `name` varchar(256) default NULL,
  `id_catia` int(11) NOT NULL default '0',
  `project_manager` varchar(256) default NULL,
  `follow_up` boolean default 0,
  `date_creation` date default NULL,
  `workgroup_key` varchar(256) default NULL,
  PRIMARY KEY  (`id_project`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `reporting_period`
--

DROP TABLE IF EXISTS `reporting_period`;
CREATE TABLE `reporting_period` (
  `id_period` int(11) NOT NULL default '0',
  `name` varchar(256) default NULL,
  `current` boolean NOT NULL default 1,
  PRIMARY KEY  (`id_period`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `reporting_fiche`
--

DROP TABLE IF EXISTS `reporting_fiche`;
CREATE TABLE `reporting_fiche` (
  `id_fiche` int(11) NOT NULL default '0',
  `id_project` int(11) NOT NULL default '0',
  `current_tasks` LONGTEXT default NULL,
  `completed_tasks` LONGTEXT default NULL,
  `coming_tasks` LONGTEXT default NULL,
  `tendency` int(11) NOT NULL default '0',
  `risk` int(11) NOT NULL default '0',
  `date_update` TIMESTAMP default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id_fiche`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Structure for table `reporting_monthly`
--

DROP TABLE IF EXISTS `reporting_monthly`;
CREATE TABLE `reporting_monthly` (
  `id_monthly` int(11) NOT NULL default '0',
  `id_project` int(11) NOT NULL default '0',
  `id_period` int(11) NOT NULL default '0',
  `fact` LONGTEXT default NULL,
  `event` LONGTEXT default NULL,
  `date_modification` date default NULL,
  `tendency` int(11) NOT NULL default '0',
  `risk` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_monthly`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

