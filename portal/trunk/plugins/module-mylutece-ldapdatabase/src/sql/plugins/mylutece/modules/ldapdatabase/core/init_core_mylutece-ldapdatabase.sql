--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
('LDAPDATABASE_MANAGEMENT_USERS', 'module.mylutece.ldapdatabase.adminFeature.ldapdatabase_management_user.name', 3, 'jsp/admin/plugins/mylutece/modules/ldapdatabase/ManageUsers.jsp', 'module.mylutece.ldapdatabase.adminFeature.ldapdatabase_management_user.description', 0, 'mylutece-ldapdatabase', 'USERS', NULL);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;


--
-- Dumping data for table `core_user_right`
--

/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('LDAPDATABASE_MANAGEMENT_USERS',1);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;