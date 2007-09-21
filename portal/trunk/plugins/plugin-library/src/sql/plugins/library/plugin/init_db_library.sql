--
-- Dumping data for table `library_media`
--

/*!40000 ALTER TABLE `library_media` DISABLE KEYS */;
INSERT INTO `library_media` (`id_media`,`name`,`description`,`stylesheet`) VALUES 
 (1,'image','Insertion de documents Image ',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D65646961223E0D0A20202020202020203C696D67207372633D227B7372637D2220616C743D227B616C747D22206865696768743D227B6865696768747D222077696474683D227B77696474687D222F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E);
/*!40000 ALTER TABLE `library_media` ENABLE KEYS */;

--
-- Dumping data for table `library_mapping`
--

/*!40000 ALTER TABLE `library_mapping` DISABLE KEYS */;
INSERT INTO `library_mapping` (`id_mapping`,`id_media`,`code_document_type`) VALUES 
 (1,1,'image');
/*!40000 ALTER TABLE `library_mapping` ENABLE KEYS */;



--
-- Dumping data for table `library_mapping_attribute`
--

/*!40000 ALTER TABLE `library_mapping_attribute` DISABLE KEYS */;
INSERT INTO `library_mapping_attribute` (`id_mapping`,`id_media_attribute`,`id_document_attribute`) VALUES 
 (1,2,44),
 (1,1,43);
/*!40000 ALTER TABLE `library_mapping_attribute` ENABLE KEYS */;




--
-- Dumping data for table `library_media_attribute`
--

/*!40000 ALTER TABLE `library_media_attribute` DISABLE KEYS */;
INSERT INTO `library_media_attribute` (`id_attribute`,`id_media`,`code`,`description`,`type`, `default_value`) VALUES 
 (1,1,'src','Source de l\'image binaire',2,''),
 (2,1,'alt','Element alt',1,''),
 (3,1,'height','Valeur de la hauteur de l\'image',0,'50'),
 (4,1,'width','Valeur de la largeur de l\'image',0,'50');
/*!40000 ALTER TABLE `library_media_attribute` ENABLE KEYS */;
