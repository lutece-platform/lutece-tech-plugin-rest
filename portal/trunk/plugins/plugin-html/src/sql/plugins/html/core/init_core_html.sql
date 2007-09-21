--
-- Dumping data for table `core_portlet_type`
--

/*!40000 ALTER TABLE `core_portlet_type` DISABLE KEYS */;
INSERT INTO `core_portlet_type` (`id_portlet_type`,`name`,`url_creation`,`url_update`,`home_class`,`plugin_name`,`url_docreate`,`create_script`,`create_specific`,`create_specific_form`,`url_domodify`,`modify_script`,`modify_specific`,`modify_specific_form`) VALUES 
 ('HTML_PORTLET','html.portlet.name','plugins/html/CreatePortletHtml.jsp','plugins/html/ModifyPortletHtml.jsp','fr.paris.lutece.plugins.html.business.portlet.HtmlPortletHome','html','plugins/html/DoCreatePortletHtml.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/html/portlet_html.html','','plugins/html/DoModifyPortletHtml.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/html/portlet_html.html','');
/*!40000 ALTER TABLE `core_portlet_type` ENABLE KEYS */;

--
-- Dumping data for table `core_style`
--

/*!40000 ALTER TABLE `core_style` DISABLE KEYS */;
INSERT INTO `core_style` (`id_style`,`description_style`,`id_portlet_type`,`id_portal_component`) VALUES 
 (100,'Défaut','HTML_PORTLET',0),
 (104,'Fond coloré','HTML_PORTLET',0),
 (103,'Titre en image','HTML_PORTLET',0),
 (102,'Encadré','HTML_PORTLET',0);
/*!40000 ALTER TABLE `core_style` ENABLE KEYS */;


--
-- Dumping data for table `core_style_mode_stylesheet`
--

/*!40000 ALTER TABLE `core_style_mode_stylesheet` DISABLE KEYS */;
INSERT INTO `core_style_mode_stylesheet` (`id_style`,`id_mode`,`id_stylesheet`) VALUES 
 (100,0,10),
 (102,0,291),
 (103,0,282),
 (104,0,285);
/*!40000 ALTER TABLE `core_style_mode_stylesheet` ENABLE KEYS */;


INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES 
 (10,'Rubrique HTML','portlet_html.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706F72746C6574223E0D0A093C64697620636C6173733D22706F72746C6574223E0D0A09202020203C78736C3A696620746573743D226E6F7428737472696E6728646973706C61792D706F72746C65742D7469746C65293D27312729223E0D0A0909093C683320636C6173733D22706F72746C65742D686561646572223E0D0A090909093C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22706F72746C65742D6E616D6522202F3E0D0A0909093C2F68333E0D0A20202020202020203C2F78736C3A69663E0D0A0D0A09093C64697620636C6173733D22706F72746C65742D636F6E74656E74223E0D0A0920202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C657422202F3E0D0A09093C2F6469763E0D0A093C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C6574223E0D0A093C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C65742D636F6E74656E7422202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C65742D636F6E74656E74223E0D0A093C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D222E22202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A),
 (282,'Rubrique HTML - titre en image','portlet_html_title_image.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D226D6F646522202F3E0D0A0D0A3C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706F72746C6574223E0D0A20203C64697620636C6173733D22706F72746C65742D6261636B67726F756E642D696D6167652D6F6E2D7469746C65223E0D0A20202020202020202020202020202020202020202020202020203C683320636C6173733D22706F72746C65742D6261636B67726F756E642D696D6167652D6F6E2D7469746C652D686561646572223E0D0A20202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22706F72746C65742D6E616D6522202F3E0D0A2020202020202020202020202020202020202020202020202020202020202020203C2F68333E0D0A0D0A20202020202020202020202020202020202020202020202020202020203C64697620636C6173733D22706F72746C65742D6261636B67726F756E642D696D6167652D6F6E2D7469746C652D636F6E74656E74223E0D0A0D0A20202020202020202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C657422202F3E0D0A20202020202020202020202020202020202020202020202020202020200D0A2020202020202020202020202020202020202020202020203C2F6469763E0D0A2020202020202020202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C6574223E0D0A2020202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C65742D636F6E74656E7422202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C65742D636F6E74656E74223E0D0A2020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D222E22202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A),
 (285,'Rubrique HTML - fond coloré','portlet_html_background_colored.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D226D6F646522202F3E0D0A0D0A3C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706F72746C6574223E0D0A20203C64697620636C6173733D22706F72746C65742D6261636B67726F756E642D636F6C6F726564223E0D0A20202020202020202020202020202020203C78736C3A696620746573743D226E6F7428737472696E6728646973706C61792D706F72746C65742D7469746C65293D27312729223E0D0A0909093C683320636C6173733D22706F72746C65742D6261636B67726F756E642D636F6C6F7265642D686561646572223E0D0A090909093C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22706F72746C65742D6E616D6522202F3E0D0A0909093C2F68333E0D0A20202020202020203C2F78736C3A69663E0D0A0D0A0D0A20202020202020202020202020202020202020202020202020202020203C64697620636C6173733D22706F72746C65742D6261636B67726F756E642D636F6C6F7265642D636F6E74656E74223E0D0A0D0A20202020202020202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C657422202F3E0D0A20202020202020202020202020202020202020202020202020202020200D0A202020202020202020202020202020202020202020202020202020203C2F6469763E0D0A2020202020202020202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C6574223E0D0A2020202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C65742D636F6E74656E7422202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C65742D636F6E74656E74223E0D0A2020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D222E22202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A),
 (291,'Rubrique HTML - Encadré','portlet_html_bordered.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706F72746C6574223E0D0A203C78736C3A696620746573743D226E6F7428737472696E6728646973706C61792D706F72746C65742D7469746C65293D27312729223E0D0A0909093C683320636C6173733D22706F72746C65742D686561646572223E0D0A090909093C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22706F72746C65742D6E616D6522202F3E0D0A0909093C2F68333E0D0A20202020202020203C2F78736C3A69663E0D0A0D0A20202020202020202020202020202020202020203C64697620636C6173733D22706F72746C6574223E0D0A202020202020202020202020202020202020202020202020202020202020202020203C64697620636C6173733D22706F72746C65742D636F6E74656E74223E0D0A20202020202020202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C657422202F3E0D0A202020202020202020202020202020202020202020202020202020203C2F6469763E0D0A20202020202020202020202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C6574223E0D0A2020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2268746D6C2D706F72746C65742D636F6E74656E7422202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2268746D6C2D706F72746C65742D636F6E74656E74223E0D0A2020202020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D222E22202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A);
/*!40000 ALTER TABLE `core_stylesheet` ENABLE KEYS */;
