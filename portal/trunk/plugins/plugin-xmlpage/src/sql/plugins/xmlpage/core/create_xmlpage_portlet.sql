--
-- Structure for table `xmlpage_portlet`
--

DROP TABLE IF EXISTS `xmlpage_portlet`;
CREATE TABLE `xmlpage_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `xmlpage_name` varchar(100) default NULL,
  `xmlpage_style` varchar(10) default NULL,
  PRIMARY KEY  (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;