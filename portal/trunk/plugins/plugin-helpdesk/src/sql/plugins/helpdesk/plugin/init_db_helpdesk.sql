--
-- Dumping data for table `helpdesk_question_answer`
--

/*!40000 ALTER TABLE `helpdesk_question_answer` DISABLE KEYS */;
INSERT INTO `helpdesk_question_answer` (`id_qa`,`question`,`answer`,`id_subject`,`status`) VALUES 
 (1,'Comment créer un document','<p>Un utilisateur ayant les droits suffisants pour créer document peut choisir entre différents types de documents.\r\n</p>\r\n<p>Par défaut Lutèce est initialisé avec des documents de type article, brèves, fichiers de type pdf, fichiers de type images, fichiers de type video, fichiers de type audio.\r\n</p>\r\n<p>La création des différents type de document est répartie entre différents espaces de travail, afin de mieux organiser les contenus\r\n  <br />\r\n</p>',2,1);
/*!40000 ALTER TABLE `helpdesk_question_answer` ENABLE KEYS */;



--
-- Dumping data for table `helpdesk_question_type`
--

/*!40000 ALTER TABLE `helpdesk_question_type` DISABLE KEYS */;
INSERT INTO `helpdesk_question_type` (`id_qt`,`question_type`) VALUES 
 (1,'Technique'),
 (2,'Contenu');
/*!40000 ALTER TABLE `helpdesk_question_type` ENABLE KEYS */;

--
-- Dumping data for table `helpdesk_subject`
--

/*!40000 ALTER TABLE `helpdesk_subject` DISABLE KEYS */;
INSERT INTO `helpdesk_subject` (`id_subject`,`subject`) VALUES 
 (1,'Publication'),
 (2,'Production');
/*!40000 ALTER TABLE `helpdesk_subject` ENABLE KEYS */;

