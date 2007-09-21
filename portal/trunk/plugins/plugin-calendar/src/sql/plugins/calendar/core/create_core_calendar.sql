--
-- Table structure for table `calendar_portlet`
--

DROP TABLE IF EXISTS `calendar_portlet`;
CREATE TABLE `calendar_portlet` (
  `id_portlet` int(11) NOT NULL,
  `date_begin` varchar(8) NOT NULL,
  `date_end` varchar(8) NOT NULL,
  `code_agenda_name` varchar(255) NOT NULL,
  `number_days` int(8) default NULL,
  PRIMARY KEY  (`id_portlet`,`code_agenda_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
