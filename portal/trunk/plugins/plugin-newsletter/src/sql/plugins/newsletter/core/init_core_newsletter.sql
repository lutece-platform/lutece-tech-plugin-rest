--
-- Dumping data for table `core_admin_right`
--

LOCK TABLES `core_admin_right` WRITE;
/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` VALUES ('NEWSLETTER_MANAGEMENT','newsletter.adminFeature.newsletter_management.name',2,'jsp/admin/plugins/newsletter/ManageNewsLetter.jsp','newsletter.adminFeature.newsletter_management.description',0,'newsletter','APPLICATIONS','images/admin/skin/plugins/newsletter/newsletter.png');
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `core_admin_role`
--

LOCK TABLES `core_admin_role` WRITE;
/*!40000 ALTER TABLE `core_admin_role` DISABLE KEYS */;
INSERT INTO `core_admin_role` VALUES ('newsletter_manager','The role needed to access a newsletter template');
/*!40000 ALTER TABLE `core_admin_role` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `core_admin_role_resource`
--

LOCK TABLES `core_admin_role_resource` WRITE;
/*!40000 ALTER TABLE `core_admin_role_resource` DISABLE KEYS */;
INSERT INTO `core_admin_role_resource` VALUES (108,'newsletter_manager','NEWSLETTER','*','*');
/*!40000 ALTER TABLE `core_admin_role_resource` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `core_portlet_type`
--

LOCK TABLES `core_portlet_type` WRITE;
/*!40000 ALTER TABLE `core_portlet_type` DISABLE KEYS */;
INSERT INTO `core_portlet_type` VALUES ('NEWSLETTER_ARCHIVE_PORTLET','newsletter.portlet.name','plugins/newsletter/CreatePortletNewsletter.jsp','plugins/newsletter/ModifyPortletNewsletter.jsp','fr.paris.lutece.plugins.newsletter.business.portlet.NewsLetterArchivePortletHome','newsletter','plugins/newsletter/DoCreatePortletNewsletter.jsp','/admin/portlet/script_create_portlet.html','','','plugins/newsletter/DoModifyPortletNewsletter.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/newsletter/newsletter_sending_list.html',''),('NEWSLETTER_SUBSCRIPTION_PORTLET','newsletter.portlet.subscription.name','plugins/newsletter/CreateSubscriptionPortletNewsletter.jsp','plugins/newsletter/ModifySubscriptionPortletNewsletter.jsp','fr.paris.lutece.plugins.newsletter.business.portlet.NewsLetterSubscriptionPortletHome','newsletter','plugins/newsletter/DoCreateSubscriptionPortletNewsletter.jsp','/admin/portlet/script_create_portlet.html','','','plugins/newsletter/DoModifySubscriptionPortletNewsletter.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/newsletter/newsletter_subscription_list.html','');
/*!40000 ALTER TABLE `core_portlet_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `core_style`
--

LOCK TABLES `core_style` WRITE;
/*!40000 ALTER TABLE `core_style` DISABLE KEYS */;
INSERT INTO `core_style` VALUES (1100,'Newsletter-Archives','NEWSLETTER_ARCHIVE_PORTLET',0),(1101,'Newsletter-Subscription','NEWSLETTER_SUBSCRIPTION_PORTLET',0);
/*!40000 ALTER TABLE `core_style` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `core_style_mode_stylesheet`
--

LOCK TABLES `core_style_mode_stylesheet` WRITE;
/*!40000 ALTER TABLE `core_style_mode_stylesheet` DISABLE KEYS */;
INSERT INTO `core_style_mode_stylesheet` VALUES (1100,0,400),(1101,0,401);
/*!40000 ALTER TABLE `core_style_mode_stylesheet` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `core_stylesheet`
--

LOCK TABLES `core_stylesheet` WRITE;
/*!40000 ALTER TABLE `core_stylesheet` DISABLE KEYS */;
INSERT INTO `core_stylesheet` VALUES (400,'Rubrique Newsletter -Archives','portlet_newsletter_archive.xsl','<?xml version=\"1.0\"?>\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n\n  <xsl:param name=\"site-path\" select=\"site-path\" />\n  <xsl:variable name=\"portlet-id\" select=\"portlet/portlet-id\" />\n\n  <xsl:template match=\"portlet\">\n    <div class=\"portlet-background-colored\" >\n      <xsl:if test=\"not(string(display-portlet-title)=\'1\')\">\n        <h3 class=\"portlet-background-colored-header\">\n          <xsl:value-of disable-output-escaping=\"yes\" select=\"portlet-name\" />\n        </h3>\n      </xsl:if>\n      <div class=\"portlet-background-colored-content\" >\n        <xsl:apply-templates select=\"newsletter-sending-list\" />\n      </div>\n    </div>\n  </xsl:template>\n\n\n  <xsl:template match=\"newsletter-sending-list\">\n    <ul>\n      <xsl:apply-templates select=\"newsletter-sending\" />\n    </ul>\n  </xsl:template>\n\n\n  <xsl:template match=\"newsletter-sending\">\n    <li>\n      <xsl:value-of select=\"newsletter-sending-date\" /> :\n\n      <a href=\"{$site-path}?page=newsletter&#38;action=show_archive&#38;sending_id={newsletter-sending-id}\" target=\"_top\">\n        <b><xsl:value-of select=\"newsletter-sending-subject\"/></b>\n      </a>\n      <br />\n    </li>\n  </xsl:template>\n\n</xsl:stylesheet>\n\n'),(401,'Rubrique Newsletter - Subscription','portlet_newsletter_subscription.xsl','<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n\r\n  <xsl:param name=\"site-path\" select=\"site-path\" />\r\n<xsl:variable name=\"portlet-id\" select=\"portlet/portlet-id\" />\r\n<xsl:variable name=\"e-mail-error\" select=\"portlet/newsletter-email-error\" />\r\n<xsl:variable name=\"nochoice-error\" select=\"portlet/subscription-nochoice-error\" />\r\n  <xsl:template match=\"portlet\">\r\n    <div class=\"portlet-background-colored\" >\r\n      <xsl:if test=\"not(string(display-portlet-title)=\'1\')\">\r\n        <h3 class=\"portlet-background-colored-header\">\r\n          <xsl:value-of disable-output-escaping=\"yes\" select=\"portlet-name\" />\r\n        </h3>\r\n      </xsl:if>\r\n      <div class=\"portlet-background-colored-content\" >\r\n<form class=\"default-form\" id=\"newsletter\" action=\"jsp/site/plugins/newsletter/DoSubscribePortletNewsLetter.jsp\" method=\"post\" >\r\n        <xsl:apply-templates select=\"newsletter-subscription-list\" />\r\n	<div>\r\n\r\n<input name=\"plugin_name\" value=\"newsletter\" type=\"hidden\" />\r\n		<label for=\"email\"><xsl:value-of select=\"newsletter-subscription-email\" /></label>\r\n\r\n    <xsl:choose>\r\n        <xsl:when test=\"not($e-mail-error>\'1\')\">\r\n		      <input name=\"email\" id=\"email\" size=\"40\" maxlength=\"100\" type=\"text\" />\r\n		\r\n		        <div>\r\n			           <xsl:value-of select=\"$e-mail-error\" />	\r\n			</div>\r\n		<xsl:choose>\r\n			<xsl:when test=\"not($nochoice-error>\'1\')\">\r\n				<div>\r\n					<xsl:value-of select=\"$nochoice-error\" />	\r\n				</div>\r\n			</xsl:when>\r\n		</xsl:choose>\r\n		\r\n	</xsl:when>\r\n\r\n		<xsl:otherwise>\r\n <input name=\"email\" id=\"email\" size=\"40\" maxlength=\"100\" type=\"text\" />\r\n		</xsl:otherwise>\r\n      </xsl:choose>\r\n		\r\n		 \r\n\r\n  <xsl:text disable-output-escaping=\"yes\"><![CDATA[<input class=\"button\" value=\"]]></xsl:text><xsl:value-of select=\"newsletter-subscription-button\" /> <xsl:text disable-output-escaping=\"yes\"><![CDATA[\" type=\"submit\" />]]></xsl:text>\r\n <xsl:text disable-output-escaping=\"yes\"><![CDATA[<input name=\"portlet_id\" value=\"]]></xsl:text><xsl:value-of select=\"portlet-id\" /><xsl:text disable-output-escaping=\"yes\"><![CDATA[\" type=\"hidden\"/>]]></xsl:text>\r\n</div>\r\n</form>\r\n      </div>\r\n\r\n    </div>\r\n  </xsl:template>\r\n\r\n\r\n  <xsl:template match=\"newsletter-subscription-list\">\r\n    <ul>\r\n      <xsl:apply-templates select=\"newsletter-subscription\" />\r\n    </ul>\r\n  </xsl:template>\r\n\r\n\r\n  <xsl:template match=\"newsletter-subscription\">\r\n    <li>\r\n <xsl:text disable-output-escaping=\"yes\"><![CDATA[<input type=\"checkbox\" class=\"checkbox-field\" name=\"newsletter]]></xsl:text><xsl:value-of select=\"newsletter-subscription-id\" /> <xsl:text disable-output-escaping=\"yes\"><![CDATA[\" id=\"newsletter]]></xsl:text><xsl:value-of select=\"newsletter-subscription-id\" /><xsl:text disable-output-escaping=\"yes\"><![CDATA[\" checked=\"checked\" />]]></xsl:text>\r\n\r\n        <b><xsl:value-of select=\"newsletter-subscription-subject\"/></b>\r\n      <br />\r\n    </li>\r\n  </xsl:template>\r\n\r\n</xsl:stylesheet>\r\n\n');
/*!40000 ALTER TABLE `core_stylesheet` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `core_user_right`
--

LOCK TABLES `core_user_right` WRITE;
/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` VALUES ('NEWSLETTER_MANAGEMENT',2);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `core_user_role`
--

LOCK TABLES `core_user_role` WRITE;
/*!40000 ALTER TABLE `core_user_role` DISABLE KEYS */;
INSERT INTO `core_user_role` VALUES ('newsletter_manager',1),('newsletter_manager',2);
/*!40000 ALTER TABLE `core_user_role` ENABLE KEYS */;
UNLOCK TABLES;
