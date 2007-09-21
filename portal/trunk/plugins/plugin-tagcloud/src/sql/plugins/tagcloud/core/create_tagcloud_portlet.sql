--
-- Table structure for table `tagcloud_portlet`
--

DROP TABLE IF EXISTS `tagcloud_portlet`;
CREATE TABLE `tagcloud_portlet` (
  `id_portlet` int(11) NOT NULL,
  `id_cloud` varchar(255) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

