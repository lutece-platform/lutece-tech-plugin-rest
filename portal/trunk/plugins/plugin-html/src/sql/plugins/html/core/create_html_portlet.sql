--
-- Structure for table `html_portlet`
--

DROP TABLE IF EXISTS `html_portlet`;
CREATE TABLE `html_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `html` text,
  PRIMARY KEY  (`id_portlet`),
  KEY `index_portlet_html` (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
