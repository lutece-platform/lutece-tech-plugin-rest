--
-- Structure for table `folderlisting_portlet`
--

DROP TABLE IF EXISTS `folderlisting_portlet`;
CREATE TABLE `folderlisting_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `code_folder` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

