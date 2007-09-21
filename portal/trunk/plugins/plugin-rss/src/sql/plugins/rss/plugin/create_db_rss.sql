--
-- Structure for table `rss_feed`
--

DROP TABLE IF EXISTS `rss_feed`;
CREATE TABLE `rss_feed` (
  `id_rss_feed` int(11) NOT NULL default '0',
  `name` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  `last_fetch_date` datetime default NULL,
  `last_fetch_status` int(11) default NULL,
  `last_fetch_error` varchar(255) default NULL,
  PRIMARY KEY  (`id_rss_feed`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `rss_generation`
--

DROP TABLE IF EXISTS `rss_generation`;
CREATE TABLE `rss_generation` (
  `id_rss` int(11) NOT NULL default '0',
  `id_portlet` int(11) NOT NULL default '0',
  `name` varchar(100) default NULL,
  `description` varchar(255) default NULL,
  `state` smallint(1) default '0',
  `date_update` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id_rss`),
  KEY `index_rss_portlet` (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
