--
-- Dumping data for table `poll`
--

/*!40000 ALTER TABLE `poll` DISABLE KEYS */;
INSERT INTO `poll` (`id_poll`,`name`,`status`,`workgroup_key`) VALUES 
 (1,'La nouvelle version Lutèce v2',1,'groupe2');
/*!40000 ALTER TABLE `poll` ENABLE KEYS */;


--
-- Dumping data for table `poll_choice`
--

/*!40000 ALTER TABLE `poll_choice` DISABLE KEYS */;
INSERT INTO `poll_choice` (`id_choice`,`id_question`,`label_choice`,`score`) VALUES 
 (1,1,'L\'ergonomie du back-office',1),
 (2,1,'Le passage en UTF-8',0),
 (3,1,'L\'internationalisation du back-office',0),
 (4,1,'La gestion par document',0);
/*!40000 ALTER TABLE `poll_choice` ENABLE KEYS */;

--
-- Dumping data for table `poll_question`
--

/*!40000 ALTER TABLE `poll_question` DISABLE KEYS */;
INSERT INTO `poll_question` (`id_question`,`id_poll`,`label_question`) VALUES 
 (1,1,'Quelle nouveauté appréciez-vous le plus dans la v2 ?');
/*!40000 ALTER TABLE `poll_question` ENABLE KEYS */;
