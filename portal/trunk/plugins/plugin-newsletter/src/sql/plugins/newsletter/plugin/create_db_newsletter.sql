--
-- Table structure for table `newsletter`
--

DROP TABLE IF EXISTS `newsletter`;
CREATE TABLE `newsletter` (
  `id_newsletter` int(11) NOT NULL default '0',
  `date_last_send` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `name` varchar(50) collate utf8_unicode_ci default NULL,
  `html` longtext collate utf8_unicode_ci,
  `id_newsletter_template` int(11) default '0',
  `id_document_template` int(11) default '0',
  `workgroup_key` varchar(50) collate utf8_unicode_ci default NULL,
  `unsubscribe` varchar(6) collate utf8_unicode_ci default NULL,
  `test_recipients` varchar(255) collate utf8_unicode_ci default NULL,
  `sender_mail` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_newsletter`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



--
-- Table structure for table `newsletter_template`
--

DROP TABLE IF EXISTS `newsletter_template`;
CREATE TABLE `newsletter_template` (
  `id_template` int(11) NOT NULL default '0',
  `template_type` smallint(1) default '0',
  `description` varchar(50) collate utf8_unicode_ci default NULL,
  `file_name` varchar(100) collate utf8_unicode_ci default NULL,
  `picture` varchar(100) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_template`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



--
-- Table structure for table `sending_newsletter`
--

DROP TABLE IF EXISTS `sending_newsletter`;
CREATE TABLE `sending_newsletter` (
  `id_sending` int(11) NOT NULL default '0',
  `id_newsletter` int(11) NOT NULL default '0',
  `date_sending` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `subscriber_count` int(11) NOT NULL default '0',
  `html` text collate utf8_unicode_ci,
  `email_subject` varchar(200) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_sending`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `subscriber`
--

DROP TABLE IF EXISTS `subscriber`;
CREATE TABLE `subscriber` (
  `id_subscriber` int(11) NOT NULL default '0',
  `email` varchar(100) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_subscriber`),
  KEY `index_subscriber` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



--
-- Table structure for table `subscriber_newsletter`
--

DROP TABLE IF EXISTS `subscriber_newsletter`;
CREATE TABLE `subscriber_newsletter` (
  `id_subscriber` int(11) NOT NULL default '0',
  `id_newsletter` int(11) NOT NULL default '0',
  `date_subscription` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id_newsletter`,`id_subscriber`),
  KEY `index_subscriber_newsletter` (`id_subscriber`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



--
-- Table structure for table `theme_newsletter`
--

DROP TABLE IF EXISTS `theme_newsletter`;
CREATE TABLE `theme_newsletter` (
  `id_theme` int(11) NOT NULL default '0',
  `id_newsletter` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_theme`,`id_newsletter`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


