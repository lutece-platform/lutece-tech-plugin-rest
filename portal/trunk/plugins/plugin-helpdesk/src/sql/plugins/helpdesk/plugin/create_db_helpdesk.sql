--
-- Structure for table `helpdesk_question_answer`
--

DROP TABLE IF EXISTS `helpdesk_question_answer`;
CREATE TABLE `helpdesk_question_answer` (
  `id_qa` int(11) NOT NULL,
  `question` text,
  `answer` text,
  `id_subject` tinyint(4) NOT NULL default '1',
  `status` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`id_qa`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `helpdesk_question_topic`
--

DROP TABLE IF EXISTS `helpdesk_question_topic`;
CREATE TABLE `helpdesk_question_topic` (
  `id_question_topic` tinyint(4) NOT NULL,
  `question_topic` varchar(50) NOT NULL default '',
  `id_question_type` tinyint(4) NOT NULL,
  PRIMARY KEY  (`id_question_topic`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



/*!40000 ALTER TABLE `helpdesk_question_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `helpdesk_question_topic` ENABLE KEYS */;


--
-- Structure for table `helpdesk_question_type`
--

DROP TABLE IF EXISTS `helpdesk_question_type`;
CREATE TABLE `helpdesk_question_type` (
  `id_qt` tinyint(4) NOT NULL,
  `question_type` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id_qt`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `helpdesk_subject`
--

DROP TABLE IF EXISTS `helpdesk_subject`;
CREATE TABLE `helpdesk_subject` (
  `id_subject` tinyint(4) NOT NULL,
  `subject` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id_subject`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Structure for table `helpdesk_visitor_question`
--

DROP TABLE IF EXISTS `helpdesk_visitor_question`;
CREATE TABLE `helpdesk_visitor_question` (
  `id_vq` int(11) NOT NULL,
  `last_name` varchar(50) NOT NULL default '',
  `first_name` varchar(50) NOT NULL default '',
  `email` varchar(80) default NULL,
  `question` text NOT NULL,
  `id_qt` tinyint(4) NOT NULL,
  `answer` text NOT NULL,
  `date_vq` date NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_question_topic` tinyint(4) NOT NULL, 
  PRIMARY KEY  (`id_vq`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


/*!40000 ALTER TABLE `helpdesk_visitor_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `helpdesk_visitor_question` ENABLE KEYS */;
