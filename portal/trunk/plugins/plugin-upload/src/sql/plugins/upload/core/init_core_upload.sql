--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
('UPLOAD_MANAGEMENT','upload.adminFeature.upload_management.name',3,'jsp/admin/plugins/upload/ManageUpload.jsp','upload.adminFeature.upload_management.description',0,'upload','CONTENT',NULL);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;



/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('UPLOAD_MANAGEMENT',2),
 ('UPLOAD_MANAGEMENT',5),
 ('UPLOAD_MANAGEMENT',6);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;