--
-- Dumping data for table `core_feature_group`
--

/*!40000 ALTER TABLE `core_feature_group` DISABLE KEYS */;
INSERT INTO `core_feature_group` (`id_feature_group`,`feature_group_description`,`feature_group_label`,`feature_group_order`) VALUES 
 ('REPORTING','reporting.features.group.description','reporting.features.group.label',2);
/*!40000 ALTER TABLE `core_feature_group` ENABLE KEYS */;

--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
('REPORTING_MANAGEMENT','reporting.adminFeature.reporting_management.name',2,'jsp/admin/plugins/reporting/ManageReporting.jsp','reporting.adminFeature.reporting_management.description',0,'reporting','REPORTING','images/admin/skin/plugins/reporting/reporting.png'),
('REPORTING_PROJECT_MANAGEMENT','reporting.adminFeature.project_management.name',2,'jsp/admin/plugins/reporting/ManageProject.jsp','reporting.adminFeature.project_management.description',0,'reporting','REPORTING','images/admin/skin/plugins/reporting/project.png'),
('REPORTING_PERIOD_MANAGEMENT','reporting.adminFeature.period_management.name',2,'jsp/admin/plugins/reporting/ManagePeriod.jsp','reporting.adminFeature.period_management.description',0,'reporting','REPORTING','images/admin/skin/plugins/reporting/period.png');
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;

--
-- Dumping data for table `core_user_right`
--

/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('REPORTING_PROJECT_MANAGEMENT',1),
 ('REPORTING_PROJECT_MANAGEMENT',2),
 ('REPORTING_MANAGEMENT',1),
 ('REPORTING_MANAGEMENT',2),
 ('REPORTING_PERIOD_MANAGEMENT',1);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;

