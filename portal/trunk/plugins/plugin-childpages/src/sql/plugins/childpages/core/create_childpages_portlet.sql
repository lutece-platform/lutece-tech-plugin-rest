
--
-- Structure for table `childpages_portlet`
--

DROP TABLE IF EXISTS `childpages_portlet`;
CREATE TABLE `childpages_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `id_child_page` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_portlet`,`id_child_page`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `childpages_portlet`
--
