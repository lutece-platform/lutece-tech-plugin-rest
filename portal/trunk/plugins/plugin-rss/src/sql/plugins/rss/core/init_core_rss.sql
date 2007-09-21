--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
 ('RSS_MANAGEMENT','rss.adminFeature.rss_management.name',2,'jsp/admin/plugins/rss/ManageRssFiles.jsp','rss.adminFeature.rss_management.description',0,'rss','APPLICATIONS',NULL),
 ('RSS_FEEDS_MANAGEMENT','rss.adminFeature.rss_feeds_management.name',2,'jsp/admin/plugins/rss/ManageRssFeeds.jsp','rss.adminFeature.rss_feeds_management.description',0,'rss','APPLICATIONS',NULL);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;


--
-- Dumping data for table `core_portlet`
--

/*!40000 ALTER TABLE `core_portlet` DISABLE KEYS */;
INSERT INTO `core_portlet` (`id_portlet`,`id_portlet_type`,`id_page`,`name`,`date_update`,`status`,`portlet_order`,`column_no`,`id_style`,`accept_alias`,`date_creation`,`display_portlet_title`) VALUES 
 (81,'RSS_PORTLET',11,'Portlet RSS','2007-03-15 15:18:33',0,2,1,50,0,'2007-03-15 12:30:09',0);
/*!40000 ALTER TABLE `core_portlet` ENABLE KEYS */;


--
-- Dumping data for table `core_portlet_type`
--

/*!40000 ALTER TABLE `core_portlet_type` DISABLE KEYS */;
INSERT INTO `core_portlet_type` (`id_portlet_type`,`name`,`url_creation`,`url_update`,`home_class`,`plugin_name`,`url_docreate`,`create_script`,`create_specific`,`create_specific_form`,`url_domodify`,`modify_script`,`modify_specific`,`modify_specific_form`) VALUES 
 ('RSS_PORTLET','rss.portlet.name','plugins/rss/CreatePortletRss.jsp','plugins/rss/ModifyPortletRss.jsp','fr.paris.lutece.plugins.rss.business.portlet.RssPortletHome','rss','plugins/rss/DoCreatePortletRss.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/rss/portlet/create_portlet_rss.html','','plugins/rss/DoModifyPortletRss.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/rss/portlet/modify_portlet_rss.html','');
/*!40000 ALTER TABLE `core_portlet_type` ENABLE KEYS */;


--
-- Dumping data for table `core_style`
--

/*!40000 ALTER TABLE `core_style` DISABLE KEYS */;
INSERT INTO `core_style` (`id_style`,`description_style`,`id_portlet_type`,`id_portal_component`) VALUES 
 (50,'Défaut','RSS_PORTLET',0);
/*!40000 ALTER TABLE `core_style` ENABLE KEYS */;


--
-- Dumping data for table `core_style_mode_stylesheet`
--

/*!40000 ALTER TABLE `core_style_mode_stylesheet` DISABLE KEYS */;
INSERT INTO `core_style_mode_stylesheet` (`id_style`,`id_mode`,`id_stylesheet`) VALUES 
 (50,0,308);
/*!40000 ALTER TABLE `core_style_mode_stylesheet` ENABLE KEYS */;


--
-- Dumping data for table `core_stylesheet`
--

/*!40000 ALTER TABLE `core_stylesheet` DISABLE KEYS */;
INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES 
 (308,'Rubrique RSS - Défaut','portlet_rss.xsl',0x3C3F786D6C2076657273696F6E3D22312E3022203F3E0D0A0D0A3C212D2D0D0A417574686F723A20204B6576696E204120427572746F6E2028627572746F6E406170616368652E6F7267290D0A417574686F723A202053616E746961676F2047616C6120287367616C614068697369746563682E636F6D290D0A417574686F723A20205261706861C3AF656C204C75746120287261706861656C406170616368652E6F7267290D0A2D2D3E0D0A0D0A3C78736C3A7374796C65736865657420786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D220D0A20202020202020202020202020202020786D6C6E733A7264663D22687474703A2F2F7777772E77332E6F72672F313939392F30322F32322D7264662D73796E7461782D6E7323220D0A20202020202020202020202020202020786D6C6E733A646F776E6C6576656C3D22687474703A2F2F6D792E6E657473636170652E636F6D2F7264662F73696D706C652F302E392F220D0A202020202020202020202020202020206578636C7564652D726573756C742D70726566697865733D22646F776E6C6576656C20726466220D0A2020202020202020202020202020202076657273696F6E3D22312E30223E0D0A0D0A202020203C78736C3A6F757470757420696E64656E743D22796573220D0A20202020202020202020202020202020206D6574686F643D2268746D6C220D0A20202020202020202020202020202020206F6D69742D786D6C2D6465636C61726174696F6E3D22796573222F3E0D0A0D0A202020203C78736C3A706172616D206E616D653D226974656D646973706C61796564222073656C6563743D226E756D62657228313529222F3E0D0A202020203C78736C3A706172616D206E616D653D2273686F776465736372697074696F6E222073656C6563743D22277472756527222F3E0D0A202020203C78736C3A706172616D206E616D653D2273686F777469746C65222073656C6563743D22277472756527222F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706F72746C6574223E0D0A093C64697620636C6173733D22706F72746C6574223E0D0A20202020202020203C78736C3A696620746573743D226E6F7428737472696E6728646973706C61792D706F72746C65742D7469746C65293D27312729223E0D0A0909093C683320636C6173733D22706F72746C65742D686561646572223E0D0A0909093C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D226E6F222073656C6563743D22706F72746C65742D6E616D6522202F3E0D0A0909093C2F68333E0D0A20202020202020203C2F78736C3A69663E0D0A0D0A09093C64697620636C6173733D22706F72746C65742D636F6E74656E74223E0D0A0909202020203C78736C3A63686F6F73653E0D0A0909202020203C78736C3A7768656E20746573743D226E6F742872737329223E0D0A090909202020203C78736C3A746578742064697361626C652D6F75747075742D6573636170696E673D22796573223E3C215B43444154415B266E6273703B5D5D3E3C2F78736C3A746578743E0D0A0909093C2F78736C3A7768656E3E0D0A0909093C78736C3A6F74686572776973653E0D0A090909202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2272737322202F3E0D0A0909093C2F78736C3A6F74686572776973653E0D0A0909093C2F78736C3A63686F6F73653E0D0A09093C2F6469763E0D0A093C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D22727373223E0D0A2020202020203C6469763E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226368616E6E656C222F3E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2274657874696E707574222F3E0D0A2020202020203C2F6469763E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D222F7264663A524446223E0D0A2020202020203C6469763E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D22646F776E6C6576656C3A6368616E6E656C222F3E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D22646F776E6C6576656C3A74657874696E707574222F3E0D0A2020202020203C2F6469763E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D226368616E6E656C223E0D0A2020202020203C78736C3A7661726961626C65206E616D653D226465736372697074696F6E222073656C6563743D226465736372697074696F6E222F3E0D0A2020202020203C78736C3A696620746573743D222473686F777469746C65203D2027747275652720616E6420246465736372697074696F6E223E0D0A20202020202020203C703E0D0A202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D22696D6167657C2E2E2F696D61676522206D6F64653D226368616E6E656C222F3E0D0A202020202020202020203C7374726F6E673E3C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22246465736372697074696F6E222F3E3C2F7374726F6E673E0D0A20202020202020203C2F703E0D0A2020202020203C2F78736C3A69663E0D0A2020202020203C756C3E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226974656D5B246974656D646973706C617965643E3D706F736974696F6E28295D222F3E0D0A2020202020203C2F756C3E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D22646F776E6C6576656C3A6368616E6E656C223E0D0A2020202020203C78736C3A7661726961626C65206E616D653D226465736372697074696F6E222073656C6563743D22646F776E6C6576656C3A6465736372697074696F6E222F3E0D0A2020202020203C78736C3A696620746573743D222473686F777469746C65203D2027747275652720616E6420246465736372697074696F6E223E0D0A20202020202020203C703E0D0A20202020202020203C78736C3A63686F6F73653E0D0A2020202020202020203C78736C3A7768656E20746573743D22636F756E74282E2E2F646F776E6C6576656C3A696D61676529223E0D0A202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D222E2E2F646F776E6C6576656C3A696D61676522206D6F64653D226368616E6E656C222F3E0D0A202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22246465736372697074696F6E222F3E0D0A2020202020202020203C2F78736C3A7768656E3E0D0A2020202020202020203C78736C3A6F74686572776973653E0D0A202020202020202020203C6120687265663D227B646F776E6C6576656C3A6C696E6B7D223E0D0A2020202020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22246465736372697074696F6E222F3E3C2F613E0D0A2020202020202020203C2F78736C3A6F74686572776973653E0D0A20202020202020203C2F78736C3A63686F6F73653E0D0A20202020202020203C2F703E0D0A2020202020203C2F78736C3A69663E0D0A2020202020203C756C3E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D222E2E2F646F776E6C6576656C3A6974656D5B246974656D646973706C617965643E3D706F736974696F6E28295D222F3E0D0A2020202020203C2F756C3E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D226974656D223E0D0A2020202020203C78736C3A7661726961626C65206E616D653D226465736372697074696F6E222073656C6563743D226465736372697074696F6E222F3E0D0A2020202020203C6C693E0D0A20202020202020203C626C6F636B71756F74653E0D0A20202020202020203C6120687265663D227B6C696E6B7D223E3C78736C3A76616C75652D6F662073656C6563743D227469746C65222F3E3C2F613E0D0A20202020202020203C78736C3A696620746573743D222473686F776465736372697074696F6E203D2027747275652720616E6420246465736372697074696F6E223E0D0A202020202020202020203C62722F3E3C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22737562737472696E6728246465736372697074696F6E2C302C34303029222F3E0D0A202020202020202020203C78736C3A746578743E2E2E2E3C2F78736C3A746578743E0D0A202020202020202020203C7020616C69676E3D227269676874223E3C6120687265663D227B6C696E6B7D22207461726765743D225F626C616E6B223E3C78736C3A746578743E4C697265206C61207375697465203E3C2F78736C3A746578743E3C2F613E3C2F703E0D0A20202020202020203C2F78736C3A69663E0D0A20202020202020203C2F626C6F636B71756F74653E0D0A2020202020203C2F6C693E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D22646F776E6C6576656C3A6974656D223E0D0A2020202020203C78736C3A7661726961626C65206E616D653D226465736372697074696F6E222073656C6563743D22646F776E6C6576656C3A6465736372697074696F6E222F3E0D0A2020202020203C6C693E0D0A20202020202020203C6120687265663D227B646F776E6C6576656C3A6C696E6B7D223E3C78736C3A76616C75652D6F662073656C6563743D22646F776E6C6576656C3A7469746C65222F3E3C2F613E0D0A20202020202020203C78736C3A696620746573743D222473686F776465736372697074696F6E203D2027747275652720616E6420246465736372697074696F6E223E0D0A202020202020202020203C62722F3E3C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22246465736372697074696F6E222F3E0D0A20202020202020203C2F78736C3A69663E0D0A2020202020203C2F6C693E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D2274657874696E707574223E0D0A2020202020203C666F726D20616374696F6E3D227B6C696E6B7D223E0D0A20202020202020203C78736C3A76616C75652D6F662073656C6563743D226465736372697074696F6E222F3E3C62722F3E0D0A20202020202020203C696E70757420747970653D227465787422206E616D653D227B6E616D657D222076616C75653D22222F3E0D0A20202020202020203C696E70757420747970653D227375626D697422206E616D653D227375626D6974222076616C75653D227B7469746C657D222F3E0D0A2020202020203C2F666F726D3E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D22646F776E6C6576656C3A74657874696E707574223E0D0A2020202020203C666F726D20616374696F6E3D227B646F776E6C6576656C3A6C696E6B7D223E0D0A20202020202020203C78736C3A76616C75652D6F662073656C6563743D22646F776E6C6576656C3A6465736372697074696F6E222F3E3C62722F3E0D0A20202020202020203C696E70757420747970653D227465787422206E616D653D227B646F776E6C6576656C3A6E616D657D222076616C75653D22222F3E0D0A20202020202020203C696E70757420747970653D227375626D697422206E616D653D227375626D6974222076616C75653D227B646F776E6C6576656C3A7469746C657D222F3E0D0A2020202020203C2F666F726D3E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D22696D61676522206D6F64653D226368616E6E656C223E0D0A2020202020203C6120687265663D227B6C696E6B7D223E0D0A20202020202020203C78736C3A656C656D656E74206E616D653D22696D67223E0D0A202020202020202020203C78736C3A617474726962757465206E616D653D22616C69676E223E72696768743C2F78736C3A6174747269627574653E0D0A202020202020202020203C78736C3A617474726962757465206E616D653D22626F72646572223E303C2F78736C3A6174747269627574653E0D0A202020202020202020203C78736C3A696620746573743D227469746C65223E0D0A2020202020202020202020203C78736C3A617474726962757465206E616D653D22616C74223E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D227469746C65222F3E0D0A2020202020202020202020203C2F78736C3A6174747269627574653E0D0A202020202020202020203C2F78736C3A69663E0D0A202020202020202020203C78736C3A696620746573743D2275726C223E0D0A2020202020202020202020203C78736C3A617474726962757465206E616D653D22737263223E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D2275726C222F3E0D0A2020202020202020202020203C2F78736C3A6174747269627574653E0D0A202020202020202020203C2F78736C3A69663E0D0A202020202020202020203C78736C3A696620746573743D227769647468223E0D0A2020202020202020202020203C78736C3A617474726962757465206E616D653D227769647468223E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D227769647468222F3E0D0A2020202020202020202020203C2F78736C3A6174747269627574653E0D0A202020202020202020203C2F78736C3A69663E0D0A202020202020202020203C78736C3A696620746573743D22686569676874223E0D0A2020202020202020202020203C78736C3A617474726962757465206E616D653D22686569676874223E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22686569676874222F3E0D0A2020202020202020202020203C2F78736C3A6174747269627574653E0D0A202020202020202020203C2F78736C3A69663E0D0A20202020202020203C2F78736C3A656C656D656E743E0D0A2020202020203C2F613E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C78736C3A74656D706C617465206D617463683D22646F776E6C6576656C3A696D61676522206D6F64653D226368616E6E656C223E0D0A2020202020203C6120687265663D227B646F776E6C6576656C3A6C696E6B7D223E0D0A20202020202020203C78736C3A656C656D656E74206E616D653D22696D67223E0D0A202020202020202020203C78736C3A617474726962757465206E616D653D22616C69676E223E72696768743C2F78736C3A6174747269627574653E0D0A202020202020202020203C78736C3A617474726962757465206E616D653D22626F72646572223E303C2F78736C3A6174747269627574653E0D0A202020202020202020203C78736C3A696620746573743D22646F776E6C6576656C3A7469746C65223E0D0A2020202020202020202020203C78736C3A617474726962757465206E616D653D22616C74223E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22646F776E6C6576656C3A7469746C65222F3E0D0A2020202020202020202020203C2F78736C3A6174747269627574653E0D0A202020202020202020203C2F78736C3A69663E0D0A202020202020202020203C78736C3A696620746573743D22646F776E6C6576656C3A75726C223E0D0A2020202020202020202020203C78736C3A617474726962757465206E616D653D22737263223E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22646F776E6C6576656C3A75726C222F3E0D0A2020202020202020202020203C2F78736C3A6174747269627574653E0D0A202020202020202020203C2F78736C3A69663E0D0A20202020202020203C2F78736C3A656C656D656E743E0D0A2020202020203C2F613E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A202020203C212D2D2057652069676E6F726520696D6167657320756E6C6573732077652061726520696E736964652061206368616E6E656C202D2D3E0D0A202020203C78736C3A74656D706C617465206D617463683D22696D616765223E0D0A202020203C2F78736C3A74656D706C6174653E0D0A202020203C78736C3A74656D706C617465206D617463683D22646F776E6C6576656C3A696D616765223E0D0A202020203C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A0D0A0D0A);
/*!40000 ALTER TABLE `core_stylesheet` ENABLE KEYS */;


--
-- Dumping data for table `core_user_right`
--

/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('RSS_FEEDS_MANAGEMENT',2),
 ('RSS_FEEDS_MANAGEMENT',6),
 ('RSS_MANAGEMENT',2),
 ('RSS_MANAGEMENT',6);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;
