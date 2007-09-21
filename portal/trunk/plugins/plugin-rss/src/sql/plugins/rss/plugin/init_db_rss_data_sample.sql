--
-- Dumping data for table `rss_feed`
--

/*!40000 ALTER TABLE `rss_feed` DISABLE KEYS */;
INSERT INTO `rss_feed` (`id_rss_feed`,`name`,`url`,`last_fetch_date`,`last_fetch_status`,`last_fetch_error`) VALUES 
 (1,'Apache Jakarta','http://jakarta.apache.org/site/rss.xml','2007-03-15 16:06:13',0,''),
 (2,'Lutece','http://fr.lutece.paris.fr/fr/plugins/rss/lutece.xml','2007-03-15 16:06:13',0,'');
/*!40000 ALTER TABLE `rss_feed` ENABLE KEYS */;


--
-- Dumping data for table `rss_generation`
--

/*!40000 ALTER TABLE `rss_generation` DISABLE KEYS */;
INSERT INTO `rss_generation` (`id_rss`,`id_portlet`,`name`,`description`,`state`,`date_update`) VALUES 
 (1,29,'liste_acteurs.xml','Liste des acteurs locaux',0,'2007-03-15 16:05:17');
/*!40000 ALTER TABLE `rss_generation` ENABLE KEYS */;
