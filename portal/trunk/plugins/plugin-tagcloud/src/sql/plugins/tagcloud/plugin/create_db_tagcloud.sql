--
-- Table structure for table `tagcloud`
--

DROP TABLE IF EXISTS `tagcloud`;
CREATE TABLE `tagcloud` (
  `id_cloud` int(11) NOT NULL,
  `cloud_description` varchar(255) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`id_cloud`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `tagcloud_tag`
--

DROP TABLE IF EXISTS `tagcloud_tag`;
CREATE TABLE `tagcloud_tag` (
  `id_cloud` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL,
  `tag_name` tinytext collate utf8_unicode_ci NOT NULL,
  `tag_weight` varchar(30) collate utf8_unicode_ci NOT NULL default '',
  `tag_url` varchar(255) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`id_cloud`,`id_tag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

