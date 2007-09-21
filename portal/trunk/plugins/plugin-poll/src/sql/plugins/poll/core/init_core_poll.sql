--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
('POLL_MANAGEMENT','poll.adminFeature.poll_management.name',2,'jsp/admin/plugins/poll/ManagePolls.jsp','poll.adminFeature.poll_management.description',0,'poll','APPLICATIONS',NULL);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_role`
--

/*!40000 ALTER TABLE `core_admin_role` DISABLE KEYS */;
INSERT INTO `core_admin_role` (`role_key`,`role_description`) VALUES 
 ('poll_manager','Gestion des sondages');
/*!40000 ALTER TABLE `core_admin_role` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_role_resource`
--

/*!40000 ALTER TABLE `core_admin_role_resource` DISABLE KEYS */;
INSERT INTO `core_admin_role_resource` (`rbac_id`,`role_key`,`resource_type`,`resource_id`,`permission`) VALUES 
 (103,'poll_manager','POLL','*','*');
/*!40000 ALTER TABLE `core_admin_role_resource` ENABLE KEYS */;

--
-- Dumping data for table `core_user_right`
--

/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('POLL_MANAGEMENT',2),
 ('POLL_MANAGEMENT',6);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;


--
-- Dumping data for table `core_user_role`
--

/*!40000 ALTER TABLE `core_user_role` DISABLE KEYS */;
INSERT INTO `core_user_role` (`role_key`,`id_user`) VALUES 
 ('poll_manager',2),
 ('poll_manager',5),
 ('poll_manager',6);
/*!40000 ALTER TABLE `core_user_role` ENABLE KEYS */;