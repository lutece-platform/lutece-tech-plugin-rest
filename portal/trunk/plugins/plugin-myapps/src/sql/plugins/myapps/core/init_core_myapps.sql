--
-- Dumping data for table `core_admin_right`
--

INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES
 ('MYAPPS_MANAGEMENT','myapps.adminFeature.myapps_management.name',1,'jsp/admin/plugins/myapps/ManageMyApps.jsp','myapps.adminFeature.myapps_management.description',0,'myapps','APPLICATIONS',NULL);


--
-- Dumping data for table `core_user_right`
--

INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES
 ('MYAPPS_MANAGEMENT',1),
 ('MYAPPS_MANAGEMENT',2),
 ('MYAPPS_MANAGEMENT',5);



--
-- Dumping data for table `core_portlet_type`
--


INSERT INTO `core_portlet_type` (`id_portlet_type`,`name`,`url_creation`,`url_update`,`home_class`,`plugin_name`,`url_docreate`,`create_script`,`create_specific`,`create_specific_form`,`url_domodify`,`modify_script`,`modify_specific`,`modify_specific_form`) VALUES 
 ('MYAPPS_PORTLET','myapps.portlet.name','plugins/myapps/CreatePortletMyApps.jsp','plugins/myapps/ModifyPortletMyApps.jsp','fr.paris.lutece.plugins.myapps.business.portlet.MyAppsPortletHome','myapps','plugins/myapps/DoCreatePortletMyApps.jsp','/admin/portlet/script_create_portlet.html','','','plugins/myapps/DoModifyPortletMyApps.jsp','/admin/portlet/script_modify_portlet.html','','');


--
-- Dumping data for table `core_style`
--

INSERT INTO `core_style` (`id_style`,`description_style`,`id_portlet_type`,`id_portal_component`) VALUES
 (600,'Default','MYAPPS_PORTLET',0);


--
-- Dumping data for table `core_style_mode_stylesheet`
--


INSERT INTO `core_style_mode_stylesheet` (`id_style`,`id_mode`,`id_stylesheet`) VALUES
 (600,0,314);



--
-- Dumping data for table `core_stylesheet`
--

INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES
(314,'Rubrique MyApps - Default','portlet_myapps.xsl','<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n\r\n\r\n<xsl:template match=\"portlet\">\r\n	<div class=\"portlet\">\r\n    	<xsl:if test=\"not(string(display-portlet-title)=\'1\')\">\r\n			<h3 class=\"portlet-header\">\r\n				<xsl:value-of disable-output-escaping=\"yes\" select=\"portlet-name\" />\r\n			</h3>\r\n        </xsl:if>\r\n		<div class=\"portlet-content\">\r\n		<br/>	<xsl:apply-templates select=\"myapps-list\" />\r\n		</div>\r\n	</div>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"myapps-list\">\r\n	<table class=\"admin\" cellpadding=\"0\" cellspacing=\"0\"  width=\"100%\">\r\n		<tr>\r\n        	<td>\r\n            	<ul>\r\n                	<xsl:apply-templates select=\"myapp\" />\r\n               		<xsl:value-of select=\"message\" />\r\n               	</ul>\r\n             </td>\r\n        </tr>\r\n		<tr>\r\n			<td align=\"center\" width=\"100%\">\r\n				<form action=\"\" name=\"XPage\" target=\"_top\">\r\n                	<input type=\"hidden\" name=\"page\" value=\"myapps\" />\r\n     <xsl:text disable-output-escaping=\"yes\"><![CDATA[<input type=\"submit\" value=\"]]></xsl:text><xsl:value-of disable-output-escaping=\"yes\" select=\"myapp-button\" />\r\n <xsl:text disable-output-escaping=\"yes\"><![CDATA[\"/>]]></xsl:text>\r\n			</form>\r\n             </td>\r\n         </tr>\r\n	</table>\r\n</xsl:template>\r\n\r\n<xsl:template match=\"myapp\" >\r\n	<br />\r\n   	<li>\r\n    	<b>\r\n	        <a href=\"{myapp-link}&amp;plugin_name=myapps\" target=\"_blank\">\r\n       			<xsl:value-of select=\"myapp-name\" />\r\n			</a>\r\n		</b>\r\n	    <br />\r\n		    <small>\r\n		        <xsl:value-of select=\"myapp-description\" />\r\n		    </small>\r\n	</li>\r\n    <br />\r\n</xsl:template>\r\n\r\n</xsl:stylesheet>');


