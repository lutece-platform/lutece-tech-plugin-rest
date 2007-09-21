
--
-- Structure for table `document`
--

DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `id_document` int(11) NOT NULL default '0',
  `code_document_type` varchar(30) collate utf8_unicode_ci default NULL,
  `date_creation` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `date_modification` timestamp NOT NULL default '0000-00-00 00:00:00',
  `title` varchar(255) collate utf8_unicode_ci default NULL,
  `id_space` int(11) default NULL,
  `id_state` int(11) default NULL,
  `xml_working_content` text collate utf8_unicode_ci,
  `xml_validated_content` text collate utf8_unicode_ci,
  `summary` text collate utf8_unicode_ci,
  `comment` text collate utf8_unicode_ci,
  `date_validity_begin` timestamp NULL default NULL,
  `date_validity_end` timestamp NULL default NULL,
  `xml_metadata` text collate utf8_unicode_ci,
  `id_creator` int(11) default NULL,
  `accept_site_comments` int(10) unsigned NOT NULL default '0',
  `is_moderated_comment` int(10) unsigned NOT NULL default '0',
  `is_email_notified_comment` int(10) unsigned NOT NULL default '0',
  `id_mailinglist` int(10) unsigned NOT NULL default '0',
  `id_page_template_document` int(10) unsigned NOT NULL default '0',
   PRIMARY KEY  (`id_document`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Structure for table `document_attribute_type`
--

DROP TABLE IF EXISTS `document_attribute_type`;
CREATE TABLE `document_attribute_type` (
  `code_attribute_type` varchar(30) collate utf8_unicode_ci NOT NULL default '',
  `name_key` varchar(100) collate utf8_unicode_ci default NULL,
  `description_key` varchar(255) collate utf8_unicode_ci default NULL,
  `manager_class` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`code_attribute_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Structure for table `document_attribute_type_parameter`
--

DROP TABLE IF EXISTS `document_attribute_type_parameter`;
CREATE TABLE `document_attribute_type_parameter` (
  `code_attribute_type` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `parameter_name` varchar(255) collate utf8_unicode_ci default NULL,
  `parameter_label_key` varchar(255) collate utf8_unicode_ci default NULL,
  `parameter_index` int(11) NOT NULL default '0',
  `parameter_description_key` varchar(255) collate utf8_unicode_ci default NULL,
  `parameter_default_value` text collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`code_attribute_type`,`parameter_index`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `document_category`
--

DROP TABLE IF EXISTS `document_category`;
CREATE TABLE `document_category` (
	`id_category` int(11) NOT NULL,
	`name` varchar(100) NOT NULL,
	`description` varchar(255) DEFAULT NULL,
	`icon_content` blob DEFAULT NULL,
	`icon_mime_type` varchar(100) DEFAULT NULL,
	PRIMARY KEY  (`id_category`)	
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `document_category_link`
--

DROP TABLE IF EXISTS `document_category_link`;
CREATE TABLE `document_category_link` (
	`id_document` int(11) NOT NULL,
	`id_category` int(11) NOT NULL,
	PRIMARY KEY  (`id_document`, `id_category`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `document_category_list_portlet`
--

DROP TABLE IF EXISTS `document_category_list_portlet`;
CREATE TABLE `document_category_list_portlet` (
	`id_portlet` int(11) NOT NULL,
	`id_category` int(11) NOT NULL,
	PRIMARY KEY  (`id_portlet`, `id_category`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Structure for table `document_comment`
--

DROP TABLE IF EXISTS `document_comment`;
CREATE TABLE `document_comment` (
  `id_comment` int(10) NOT NULL default '0',
  `id_document` int(10) NOT NULL default '0',
  `date_comment` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `name` varchar(255) collate utf8_unicode_ci NOT NULL,
  `email` varchar(255) collate utf8_unicode_ci NOT NULL,
  `ip_address` varchar(100) collate utf8_unicode_ci NOT NULL,
  `comment` text collate utf8_unicode_ci NOT NULL,
  `status` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id_comment`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Structure for table `document_content`
--

DROP TABLE IF EXISTS `document_content`;
CREATE TABLE `document_content` (
  `id_document` int(11) NOT NULL default '0',
  `id_document_attribute` int(11) NOT NULL default '0',
  `text_value` longtext collate utf8_unicode_ci,
  `mime_type` varchar(255) collate utf8_unicode_ci default NULL,
  `binary_value` longblob,
  PRIMARY KEY  (`id_document`,`id_document_attribute`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



--
-- Table structure for table `document_history`
--

DROP TABLE IF EXISTS `document_history`;
CREATE TABLE `document_history` (
  `id_document` int(11) NOT NULL default '0',
  `event_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `event_user` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  `event_message_key` varchar(255) collate utf8_unicode_ci NOT NULL default '',
  `document_state_key` varchar(100) collate utf8_unicode_ci default NULL,
  `document_space` varchar(255) collate utf8_unicode_ci default NULL,
  KEY `id_document` (`id_document`),
  KEY `event_user` (`event_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_list_portlet`
--

DROP TABLE IF EXISTS `document_list_portlet`;
CREATE TABLE `document_list_portlet` (
  `id_portlet` int(11) default NULL,
  `code_document_type` varchar(30) collate utf8_unicode_ci default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_page_template`
--
CREATE TABLE `document_page_template` (
  `id_page_template_document` int(11) NOT NULL default '0',
  `page_template_path` varchar(255) collate utf8_unicode_ci default NULL,
  `picture_path` varchar(255) collate utf8_unicode_ci default NULL,
  `description` varchar(255) collate utf8_unicode_ci default NULL,
   PRIMARY KEY  (`id_page_template_document`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
  
--
-- Table structure for table `document_published`
--

DROP TABLE IF EXISTS `document_published`;
CREATE TABLE `document_published` (
  `id_portlet` int(11) default NULL,
  `id_document` int(11) default NULL,
  `document_order` int(11) default NULL,
  `status` smallint(6) NOT NULL default '1',
  `date_publishing` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_rule`
--

DROP TABLE IF EXISTS `document_rule`;
CREATE TABLE `document_rule` (
  `id_rule` int(11) NOT NULL default '0',
  `rule_type` varchar(50) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id_rule`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_rule_attributes`
--

DROP TABLE IF EXISTS `document_rule_attributes`;
CREATE TABLE `document_rule_attributes` (
  `id_rule` int(11) NOT NULL default '0',
  `attribute_name` varchar(255) collate utf8_unicode_ci NOT NULL default '',
  `attribute_value` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_rule`,`attribute_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_space`
--

DROP TABLE IF EXISTS `document_space`;
CREATE TABLE `document_space` (
  `id_space` int(11) NOT NULL default '0',
  `id_parent` int(11) default NULL,
  `name` varchar(100) collate utf8_unicode_ci default NULL,
  `description` varchar(255) collate utf8_unicode_ci default NULL,
  `view` varchar(20) collate utf8_unicode_ci default NULL,
  `id_space_icon` int(11) default NULL,
  `space_order` int(11) default NULL,
  `document_creation_allowed` int(11) default NULL,
  PRIMARY KEY  (`id_space`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_space_action`
--

DROP TABLE IF EXISTS `document_space_action`;
CREATE TABLE `document_space_action` (
  `id_action` int(11) NOT NULL default '0',
  `name_key` varchar(100) collate utf8_unicode_ci default NULL,
  `description_key` varchar(100) collate utf8_unicode_ci default NULL,
  `action_url` varchar(255) collate utf8_unicode_ci default NULL,
  `icon_url` varchar(255) collate utf8_unicode_ci default NULL,
  `action_permission` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_action`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_space_document_type`
--

DROP TABLE IF EXISTS `document_space_document_type`;
CREATE TABLE `document_space_document_type` (
  `id_space` int(11) NOT NULL default '0',
  `code_document_type` varchar(30) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`id_space`,`code_document_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_space_icon`
--

DROP TABLE IF EXISTS `document_space_icon`;
CREATE TABLE `document_space_icon` (
  `id_space_icon` int(11) NOT NULL default '0',
  `icon_url` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_space_icon`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_type`
--

DROP TABLE IF EXISTS `document_type`;
CREATE TABLE `document_type` (
  `code_document_type` varchar(30) collate utf8_unicode_ci NOT NULL default '',
  `name` varchar(100) collate utf8_unicode_ci default NULL,
  `description` varchar(255) collate utf8_unicode_ci default NULL,
  `thumbnail_attribute_id` int(11) default NULL,
  `default_thumbnail_url` varchar(255) collate utf8_unicode_ci default NULL,
  `admin_xsl` longblob,
  `content_service_xsl` longblob,
  `metadata_handler` varchar(100) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`code_document_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_type_attributes`
--

DROP TABLE IF EXISTS `document_type_attributes`;
CREATE TABLE `document_type_attributes` (
  `id_document_attribute` int(11) NOT NULL default '0',
  `code_document_type` varchar(30) collate utf8_unicode_ci default NULL,
  `code_attribute_type` varchar(50) collate utf8_unicode_ci default NULL,
  `code` varchar(50) collate utf8_unicode_ci default NULL,
  `name` varchar(100) collate utf8_unicode_ci default NULL,
  `description` varchar(255) collate utf8_unicode_ci default NULL,
  `attribute_order` int(11) default NULL,
  `required` int(11) default NULL,
  `searchable` int(11) default NULL,
  PRIMARY KEY  (`id_document_attribute`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_type_attributes_parameters`
--

DROP TABLE IF EXISTS `document_type_attributes_parameters`;
CREATE TABLE `document_type_attributes_parameters` (
  `id_document_attribute` int(11) NOT NULL default '0',
  `parameter_name` varchar(255) collate utf8_unicode_ci NOT NULL default '',
  `id_list_parameter` int(11) NOT NULL default '0',
  `parameter_value` text collate utf8_unicode_ci,
  PRIMARY KEY  (`id_document_attribute`,`parameter_name`,`id_list_parameter`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_view`
--

DROP TABLE IF EXISTS `document_view`;
CREATE TABLE `document_view` (
  `code_view` varchar(20) collate utf8_unicode_ci NOT NULL default '',
  `name_key` varchar(100) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`code_view`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_workflow_action`
--

DROP TABLE IF EXISTS `document_workflow_action`;
CREATE TABLE `document_workflow_action` (
  `id_action` int(11) NOT NULL default '0',
  `name_key` varchar(100) collate utf8_unicode_ci default NULL,
  `description_key` varchar(100) collate utf8_unicode_ci default NULL,
  `action_url` varchar(255) collate utf8_unicode_ci default NULL,
  `icon_url` varchar(255) collate utf8_unicode_ci default NULL,
  `action_permission` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_action`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_workflow_state`
--

DROP TABLE IF EXISTS `document_workflow_state`;
CREATE TABLE `document_workflow_state` (
  `id_state` int(11) NOT NULL default '0',
  `name_key` varchar(100) collate utf8_unicode_ci default NULL,
  `description_key` varchar(255) collate utf8_unicode_ci default NULL,
  `state_order` int(11) default NULL,
  PRIMARY KEY  (`id_state`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `document_workflow_transition`
--

DROP TABLE IF EXISTS `document_workflow_transition`;
CREATE TABLE `document_workflow_transition` (
  `id_state` int(11) NOT NULL default '0',
  `id_action` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_state`,`id_action`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
