--
-- Structure for table `linkpages_portlet`
--

DROP TABLE IF EXISTS `linkpages_portlet`;
CREATE TABLE `linkpages_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `id_linkpage` int(11) NOT NULL default '0',
  `linkpage_order` int(11) default '0',
  PRIMARY KEY  (`id_portlet`,`id_linkpage`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
