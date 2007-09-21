--
-- Structure for table `rss_portlet`
--

DROP TABLE IF EXISTS `rss_portlet`;
CREATE TABLE `rss_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `rss_feed_id` varchar(100) default NULL,
  PRIMARY KEY  (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

