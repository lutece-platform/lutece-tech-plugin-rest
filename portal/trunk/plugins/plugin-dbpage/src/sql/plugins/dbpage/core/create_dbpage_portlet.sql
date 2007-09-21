--
-- Structure for table `dbpage_portlet`
--

DROP TABLE IF EXISTS `dbpage_portlet`;
CREATE TABLE `dbpage_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `dbpage_name` varchar(50) collate utf8_unicode_ci default NULL,
  `dbpage_values` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_portlet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- data for table `core_portlet_type`
--

insert into `core_portlet_type` values 

('DBPAGE_PORTLET','dbpage.portlet.name','plugins/dbpage/CreatePortletDbPage.jsp','plugins/dbpage/ModifyPortletDbPage.jsp','fr.paris.lutece.plugins.dbpage.business.portlet.DbPagePortletHome','dbpage','plugins/dbpage/DoCreatePortletDbPage.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/dbpage/portlet/portlet_dbpage_type.html','','plugins/dbpage/DoModifyPortletDbPage.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/dbpage/portlet/portlet_dbpage_type.html','');

--
-- data for table `core_style`
--

insert into `core_style` values 

(252,'Default','DBPAGE_PORTLET',0);

--
-- data for table `core_style_mode_stylesheet`
--

insert into `core_style_mode_stylesheet` values 

(252,0,280);

--
-- data for table `core_stylesheet`
--

insert into `core_stylesheet` values 

(280,'Default','portlet_dbpage.xsl','<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n\r\n\r\n<xsl:template match=\"portlet\">\r\n	<div class=\"portlet\">\r\n	    <xsl:if test=\"not(string(display-portlet-title)=\'1\')\">\r\n			<h3 class=\"portlet-header\">\r\n				<xsl:value-of disable-output-escaping=\"yes\" select=\"portlet-name\" />\r\n			</h3>\r\n        </xsl:if>\r\n\r\n		<div class=\"portlet-content\">\r\n	        <xsl:apply-templates select=\"dbpage-portlet\" />\r\n		</div>\r\n	</div>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"dbpage-portlet\">\r\n	<xsl:apply-templates select=\"dbpage-portlet-content\" />\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"dbpage-portlet-content\">\r\n	<xsl:value-of disable-output-escaping=\"yes\" select=\".\" />\r\n\r\n</xsl:template>\r\n\r\n\r\n</xsl:stylesheet>\r\n');

--
-- data for table `core_user_right`
--

insert into `core_user_right` values 
('DBPAGE_MANAGEMENT',1);

--
-- data for table `core_admin_right`
--

insert into `core_admin_right` values 

('DBPAGE_MANAGEMENT','dbpage.adminFeature.dbpage_management.name',1,'jsp/admin/plugins/dbpage/ManageDbPages.jsp','dbpage.adminFeature.dbpage_management.description',0,'dbpage',NULL,NULL);
