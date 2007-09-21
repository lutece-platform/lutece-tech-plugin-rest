--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
('SYSTEMINFO_MANAGEMENT','systeminfo.adminFeature.systeminfo_management.name',0,'jsp/admin/plugins/systeminfo/ManageSystemInfo.jsp','systeminfo.adminFeature.systeminfo_management.description',0,'systeminfo','SYSTEM',NULL);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;

--
-- Dumping data for table `core_user_right`
--

/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('SYSTEMINFO_MANAGEMENT',1);
 /*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;