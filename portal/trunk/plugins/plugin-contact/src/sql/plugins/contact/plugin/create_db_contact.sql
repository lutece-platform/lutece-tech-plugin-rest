--
-- Structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id_contact` int(11) NOT NULL default '0',
  `description` varchar(50) NOT NULL default '',
  `email` varchar(100) NOT NULL default '',
  `contact_order` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_contact`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
