--
-- Dumping data for table `newsletter`
--

LOCK TABLES `newsletter` WRITE;
INSERT INTO `newsletter` values (1,'2007-05-16 13:03:30','Lettre d\'information','Content',1,2,'all','TRUE','lutece@lutece.fr','lutece@lutece.fr');
UNLOCK TABLES;


--
-- Dumping data for table `newsletter_template`
--

LOCK TABLES `newsletter_template` WRITE;
/*!40000 ALTER TABLE `newsletter_template` DISABLE KEYS */;
INSERT INTO `newsletter_template` VALUES (1,0,'newsletter_model','model_newsletter.html','newsletter.png'),(2,1,'document_model','model_document.html','document.png');
/*!40000 ALTER TABLE `newsletter_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `subscriber`
--

--
-- Dumping data for table `theme_newsletter`
--

LOCK TABLES `theme_newsletter` WRITE;
/*!40000 ALTER TABLE `theme_newsletter` DISABLE KEYS */;
INSERT INTO `theme_newsletter` VALUES (29,1);
/*!40000 ALTER TABLE `theme_newsletter` ENABLE KEYS */;
UNLOCK TABLES;