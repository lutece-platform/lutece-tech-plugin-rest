--
-- Creation of table `portlet_newsletter_archive`
--
CREATE TABLE portlet_newsletter_archive (  
	id_portlet int(11) NOT NULL default '0',  
	id_sending int(11) NOT NULL default '0',  
	PRIMARY KEY  (id_portlet,id_sending)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Creation of table `portlet_newsletter_subscription`
--
CREATE TABLE portlet_newsletter_subscription (  
	id_portlet int(11) NOT NULL default '0',  
	id_newsletter int(11) NOT NULL default '0',  
	PRIMARY KEY  (id_portlet,id_newsletter)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;