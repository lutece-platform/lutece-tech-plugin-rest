--
-- Structure for table `jsr170_portlet`
--
DROP TABLE IF EXISTS `jsr170_portlet`;
CREATE TABLE `jsr170_portlet` (
  `id_portlet` int(10) unsigned NOT NULL default '0',
  `id_view` int(10) unsigned default NULL,
  PRIMARY KEY  (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

