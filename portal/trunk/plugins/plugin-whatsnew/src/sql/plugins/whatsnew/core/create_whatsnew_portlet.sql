--
-- Structure for table `whatsnew_portlet`
--

DROP TABLE IF EXISTS `whatsnew_portlet`;
CREATE TABLE `whatsnew_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `show_documents` smallint(1) NOT NULL default '0',
  `show_portlets` smallint(1) NOT NULL default '0',
  `show_pages` smallint(1) NOT NULL default '0',
  `period` int(11) NOT NULL default '0',
  `nb_elements_max` int(11) NOT NULL default '0',
  `elements_order` int(11) NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;