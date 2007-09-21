/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`) VALUES 
 ('JSR170_MANAGEMENT','jsr170.adminFeature.jsr170_management.name',1,'jsp/admin/plugins/jsr170/ManageWorkspaces.jsp','jsr170.adminFeature.jsr170_management.description',0,'jsr170',NULL,NULL),
 ('JSR170_VIEW_MANAGEMENT','jsr170.adminFeature.jsr170_view_management.name',1,'jsp/admin/plugins/jsr170/ManageViews.jsp','jsr170.adminFeature.jsr170_view_management.description',0,'jsr170',NULL,NULL);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;


/*!40000 ALTER TABLE `core_portlet_type` DISABLE KEYS */;
INSERT INTO `core_portlet_type` (`id_portlet_type`,`name`,`url_creation`,`url_update`,`home_class`,`plugin_name`,`url_docreate`,`create_script`,`create_specific`,`create_specific_form`,`url_domodify`,`modify_script`,`modify_specific`,`modify_specific_form`) VALUES 
 ('JSR170_PORTLET','jsr170.portlet.name','plugins/jsr170/CreatePortletJsr170.jsp','plugins/jsr170/ModifyPortletJsr170.jsp','fr.paris.lutece.plugins.jcr.business.portlet.Jsr170PortletHome','jsr170','plugins/jsr170/DoCreatePortletJsr170.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/jsr170/portlet/create_jsr170.html','','plugins/jsr170/DoModifyPortletJsr170.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/jsr170/portlet/modify_jsr170.html','');
/*!40000 ALTER TABLE `core_portlet_type` ENABLE KEYS */;


/*!40000 ALTER TABLE `core_style` DISABLE KEYS */;
INSERT INTO `core_style` (`id_style`,`description_style`,`id_portlet_type`,`id_portal_component`) VALUES 
 (16,'jcr','JSR170_PORTLET',0);
/*!40000 ALTER TABLE `core_style` ENABLE KEYS */;


/*!40000 ALTER TABLE `core_style_mode_stylesheet` DISABLE KEYS */;
INSERT INTO `core_style_mode_stylesheet` (`id_style`,`id_mode`,`id_stylesheet`) VALUES 
 (16,0,281);
/*!40000 ALTER TABLE `core_style_mode_stylesheet` ENABLE KEYS */;


/*!40000 ALTER TABLE `core_stylesheet` DISABLE KEYS */;
INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES 
(281,'JCR','portlet_jsr170.xsl',0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D2249534F2D383835392D3135223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D22736974652D70617468222073656C6563743D22736974652D7061746822202F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706F72746C6574223E0D0A202020200D0A093C64697620636C6173733D22706F72746C6574222069643D22706F72746C65745F69645F7B706F72746C65742D69647D22206E616D653D22706F72746C65745F69645F7B706F72746C65742D69647D22203E0D0A20202020202020203C78736C3A696620746573743D226E6F7428737472696E6728646973706C61792D706F72746C65742D7469746C65293D27312729223E0D0A0909093C683320636C6173733D22706F72746C65742D686561646572223E0D0A090909093C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22706F72746C65742D6E616D6522202F3E0D0A0909093C2F68333E0D0A20202020202020203C2F78736C3A69663E0D0A0D0A09093C64697620636C6173733D22706F72746C65742D636F6E74656E74223E0D0A0909202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226A73723137302D706F72746C65742D6572726F7222202F3E0D0A0909202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226A73723137302D706F72746C657422202F3E0D0A0909202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226A73723137302D706F72746C65742D6D6F6469667922202F3E0D0A0909202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226A73723137302D706F72746C65742D686973746F727922202F3E0D0A09093C2F6469763E0D0A093C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D226A73723137302D706F72746C6574223E0D0A093C78736C3A6170706C792D74656D706C617465732073656C6563743D2261646D696E2D766965772D636F6D626F22202F3E0D0A093C78736C3A6170706C792D74656D706C617465732073656C6563743D226572726F722D75706C6F616422202F3E0D0A202020203C68333E0D0A202020200952E9706572746F697265203A200D0A20202020093C693E0D0A0920202020093C78736C3A6170706C792D74656D706C617465732073656C6563743D2262726561646372756D627322202F3E0D0A09093C2F693E090D0A202020203C2F68333E0D0A202020200D0A202020200D0A093C7461626C652063656C6C70616464696E673D2231222063656C6C73706163696E673D2231222077696474683D2231303025223E0D0A09202020203C74723E0D0A2020202009202020203C74683E3C2F74683E0D0A2020202020202020202020203C74682077696474683D22363025223E4E6F6D3C2F74683E0D0A2020202020202020202020203C74683E5461696C6C653C2F74683E0D0A2020202020202020202020203C74683E446174653C2F74683E0D0A2020202020202020202020203C74683E416374696F6E733C2F74683E0D0A20202020202020203C2F74723E0D0A20202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226469726563746F727922202F3E0D0A09202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2266696C6522202F3E0D0A20202020203C2F7461626C653E0D0A0D0A093C78736C3A696620746573743D2263616E57726974653D277472756527223E0D0A202020203C666F726D206E616D653D22666F726D5F75706C6F61645F7B2E2E2F706F72746C65742D69647D2220616374696F6E3D226A73702F736974652F706C7567696E732F6A73723137302F55706C6F616446696C652E6A73702220656E63747970653D226D756C7469706172742F666F726D2D6461746122206D6574686F643D22706F7374223E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22706F72746C65745F6964222076616C75653D227B2E2E2F706F72746C65742D69647D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D226469726563746F72795F70617468222076616C75653D227B6469726563746F72792D706174687D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22766965775F69645F7B2E2E2F706F72746C65742D69647D222076616C75653D227B63757272656E742D766965772D69647D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22706167655F6964222076616C75653D227B2E2E2F706167652D69647D22202F3E090D0A093C6C6162656C20666F723D2275706C6F61645F66696C65223E416A6F7574657220756E20666963686965723C2F6C6162656C3E0D0A093C696E70757420747970653D2266696C6522206E616D653D2275706C6F61645F66696C6522202F3E0D0A093C696E70757420747970653D227375626D69742220636C6173733D22627574746F6E222076616C75653D22416A6F75746572222F3E0D0A202020203C2F666F726D3E0D0A202020203C666F726D206E616D653D22666F726D5F6D6B6469725F7B2E2E2F706F72746C65742D69647D2220616374696F6E3D226A73702F736974652F706C7567696E732F6A73723137302F4372656174654469726563746F72792E6A737022206D6574686F643D22706F7374223E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22706F72746C65745F6964222076616C75653D227B2E2E2F706F72746C65742D69647D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D226469726563746F72795F70617468222076616C75653D227B6469726563746F72792D706174687D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22766965775F69645F7B2E2E2F706F72746C65742D69647D222076616C75653D227B63757272656E742D766965772D69647D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22706167655F6964222076616C75653D227B2E2E2F706167652D69647D22202F3E090D0A093C6C6162656C20666F723D226E65775F6469726563746F7279223E4372E9657220756E2072E9706572746F6972653C2F6C6162656C3E0D0A093C696E70757420747970653D227465787422206E616D653D226E65775F6469726563746F727922202F3E0D0A093C696E70757420747970653D227375626D69742220636C6173733D22627574746F6E222076616C75653D224372E965722072E9706572746F697265222F3E0D0A202020203C2F666F726D3E0D0A202020203C2F78736C3A69663E0D0A0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226A73723137302D706F72746C65742D6D6F64696679223E0D0A202020203C666F726D206E616D653D22666F726D5F75706C6F61645F7B2E2E2F706F72746C65742D69647D2220616374696F6E3D226A73702F736974652F706C7567696E732F6A73723137302F4D6F6469667946696C652E6A73702220656E63747970653D226D756C7469706172742F666F726D2D6461746122206D6574686F643D22706F7374223E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22706F72746C65745F6964222076616C75653D227B2E2E2F706F72746C65742D69647D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22766965775F69645F7B2E2E2F706F72746C65742D69647D222076616C75653D227B63757272656E742D766965772D69647D22202F3E0D0A093C696E70757420747970653D2268696464656E22206E616D653D22706167655F6964222076616C75653D227B2E2E2F706167652D69647D22202F3E090D0A093C696E70757420747970653D2268696464656E22206E616D653D2266696C655F6964222076616C75653D227B66696C652F66696C652D69647D22202F3E0D0A093C6C6162656C20666F723D2275706C6F61645F66696C65223E4D6F646966696572206C652066696368696572203C78736C3A76616C75652D6F662073656C6563743D2266696C652F66696C652D6E616D6522202F3E3C2F6C6162656C3E0D0A093C696E70757420747970653D2266696C6522206E616D653D2275706C6F61645F66696C6522202F3E0D0A093C696E70757420747970653D227375626D69742220636C6173733D22627574746F6E222076616C75653D224D6F646966696572222F3E0D0A202020203C2F666F726D3E0D0A093C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2F706F72746C65742F706F72746C65742D69647D262333383B706167655F69643D7B2F706F72746C65742F706167652D69647D262333383B766965775F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B63757272656E742D766965772D69647D262333383B66696C655F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B706172656E742D69647D23706F72746C65745F69645F7B2F706F72746C65742F706F72746C65742D69647D22203E0D0A09093C78736C3A746578743E416E6E756C65723C2F78736C3A746578743E0D0A093C2F613E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226A73723137302D706F72746C65742D686973746F7279223E0D0A093C7461626C652063656C6C70616464696E673D2231222063656C6C73706163696E673D2231222077696474683D2231303025223E0D0A09202020203C74723E0D0A2020202009202020203C74683E3C2F74683E0D0A2020202020202020202020203C74682077696474683D22363025223E56657273696F6E3C2F74683E0D0A2020202020202020202020203C74683E3C2F74683E0D0A2020202020202020202020203C74683E446174653C2F74683E0D0A2020202020202020202020203C74683E3C2F74683E0D0A20202020202020203C2F74723E0D0A09092020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2276657273696F6E22202F3E0D0A20202020203C2F7461626C653E0D0A20202020203C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2F706F72746C65742F706F72746C65742D69647D262333383B706167655F69643D7B2F706F72746C65742F706167652D69647D262333383B766965775F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B63757272656E742D766965772D69647D262333383B66696C655F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B706172656E742D69647D23706F72746C65745F69645F7B2F706F72746C65742F706F72746C65742D69647D22203E0D0A09093C78736C3A746578743E416E6E756C65723C2F78736C3A746578743E0D0A09203C2F613E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226572726F722D75706C6F6164223E0D0A093C64697620636C6173733D22616C657274223E200D0A20202020093C78736C3A76616C75652D6F662073656C6563743D222E22202F3E0D0A202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D2266696C65223E0D0A093C74723E0D0A09093C74643E0D0A0909093C78736C3A63686F6F73653E0D0A0909092020093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276A70672729206F72202866696C652D657874656E73696F6E203D20274A50472729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F696D6167652E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A09090920093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276769662729206F72202866696C652D657874656E73696F6E203D20274749462729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F696D6167652E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D2027706E672729206F72202866696C652D657874656E73696F6E203D2027504E472729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F696D6167652E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D2027626D702729206F72202866696C652D657874656E73696F6E203D2027424D502729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F696D6167652E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A090909093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D2027646F632729206F72202866696C652D657874656E73696F6E203D20277274662729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F776F72642E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D2027786C732729206F72202866696C652D657874656E73696F6E203D20276373762729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F657863656C2E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277070742729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7070742E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277064662729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7064662E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A090909093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277478742729206F72202866696C652D657874656E73696F6E203D20276373732729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7478742E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A090909093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277A69702729206F72202866696C652D657874656E73696F6E203D20277261722729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7A69702E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276A732729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F6A732E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276A6176612729206F72202866696C652D657874656E73696F6E203D20276A73702729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F6A6176612E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D202768746D6C2729206F72202866696C652D657874656E73696F6E203D202768746D2729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F68746D6C2E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D2027786D6C2729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F786D6C2E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D202778736C2729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F78736C2E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276F64732729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F6F64732E6A706722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276F64742729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F6F64742E6A706722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20276F64702729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F6F64702E6A706722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277378632729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7378632E6A706722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277378692729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7378692E6A706722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090909093C78736C3A7768656E20746573743D222866696C652D657874656E73696F6E203D20277378772729223E0D0A0909090920202020202020203C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F7378772E6A706722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A0909092020093C2F78736C3A7768656E3E0D0A090920202020202020203C78736C3A6F74686572776973653E0D0A20202020202020200909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F756E6B6E6F776E2E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E0D0A090920202020202020203C2F78736C3A6F74686572776973653E0D0A0909093C2F78736C3A63686F6F73653E0D0A09093C2F74643E0D0A202009093C74643E0D0A09090909093C6120687265663D226A73702F736974652F706C7567696E732F6A73723137302F446973706C617946696C652E6A73703F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69643D7B66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D223E0D0A0909090909202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2266696C652D6E616D6522202F3E0D0A09090909093C2F613E0D0A09093C2F74643E0D0A09093C74643E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D2266696C652D73697A65222F3E204B6F0D0A09093C2F74643E0D0A09093C74643E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D2266696C652D64617465222F3E0D0A09093C2F74643E0D0A09093C74643E0D0A0909093C78736C3A696620746573743D222E2E2F63616E52656D6F76653D277472756527223E0D0A0909093C6120687265663D226A73702F736974652F706C7567696E732F6A73723137302F446F44656C65746546696C652E6A73703F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69643D7B66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D223E0D0A090909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F616374696F6E732F616374696F6E5F64656C6574652E706E672220616C743D227375707072696D657222207469746C653D225375707072696D657222202F3E0D0A0909093C2F613E0D0A0909093C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69643D7B66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D262333383B616374696F6E5F7B2E2E2F2E2E2F706F72746C65742D69647D3D6D6F64696679223E0D0A090909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F616374696F6E732F616374696F6E5F656469742E706E672220616C743D226D6F64696669657222207469746C653D224D6F64696669657222202F3E0D0A0909093C2F613E0D0A0D0A0909093C78736C3A63686F6F73653E0D0A090909093C78736C3A7768656E20746573743D2266696C652D76657273696F6E61626C653D277472756527223E0D0A09090909093C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69643D7B66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D262333383B616374696F6E5F7B2E2E2F2E2E2F706F72746C65742D69647D3D686973746F7279223E0D0A0909090909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F616374696F6E732F616374696F6E5F686973746F72792E706E672220616C743D22436F6E73756C746572206C27686973746F726971756522207469746C653D22436F6E73756C746572206C27686973746F726971756522202F3E0D0A09090909093C2F613E0D0A090909093C2F78736C3A7768656E3E0D0A090909093C78736C3A6F74686572776973653E0D0A09090909093C6120687265663D226A73702F736974652F706C7567696E732F6A73723137302F446F41646446696C6556657273696F6E696E672E6A73703F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69643D7B66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D223E0D0A0909090909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F616374696F6E732F616374696F6E5F636865636B6F75742E706E672220616C743D22416A6F757465722061752067657374696F6E6E616972652064652076657273696F6E7322207469746C653D22416A6F757465722061752067657374696F6E6E616972652064652076657273696F6E7322202F3E0D0A09090909093C2F613E0D0A090909093C2F78736C3A6F74686572776973653E0D0A0909093C2F78736C3A63686F6F73653E0D0A0D0A0909093C2F78736C3A69663E0D0A09093C2F74643E0D0A093C2F74723E0D0A093C78736C3A6170706C792D74656D706C617465732073656C6563743D2266696C652D636F6E74656E7422202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D226469726563746F7279223E0D0A093C74723E0D0A09093C74643E0D0A0909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F69636F6E732F69636F6E5F6469726563746F72792E706E6722206865696768743D223136222077696474683D2231362220626F726465723D2230222F3E20200D0A09093C2F74643E0D0A202009093C74643E0D0A0920202020202020203C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B66696C652D69647D23706F72746C65745F69645F7B2E2E2F2E2E2F706F72746C65742D69647D22207461726765743D225F746F7022203E0D0A090920202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226469726563746F72792D6E616D6522202F3E0D0A0909093C2F613E0D0A09093C2F74643E0D0A09093C74643E3C2F74643E0D0A09093C74643E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D226469726563746F72792D64617465222F3E0D0A09093C2F74643E0D0A09093C74643E0D0A0909093C78736C3A696620746573743D222E2E2F63616E52656D6F76653D277472756527223E0D0A0909093C6120687265663D226A73702F736974652F706C7567696E732F6A73723137302F446F44656C65746546696C652E6A73703F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B66696C655F69643D7B66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D223E0D0A090909093C696D67207372633D22696D616765732F6C6F63616C2F736B696E2F706C7567696E732F6A73723137302F616374696F6E732F616374696F6E5F64656C6574652E706E672220616C743D227375707072696D657222202F3E0D0A0909093C2F613E0D0A0909093C2F78736C3A69663E0D0A09093C2F74643E0D0A093C2F74723E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D2276657273696F6E223E0D0A093C74723E0D0A09093C74643E0D0A09093C2F74643E0D0A202009093C74643E0D0A0909093C6120687265663D226A73702F736974652F706C7567696E732F6A73723137302F446973706C617956657273696F6E6E656446696C652E6A73703F706F72746C65745F69643D7B2E2E2F2E2E2F706F72746C65742D69647D262333383B766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D3D7B2E2E2F63757272656E742D766965772D69647D262333383B76657273696F6E5F6E616D653D7B76657273696F6E2D6E616D657D262333383B66696C655F69643D7B2E2E2F66696C652D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D223E0D0A090920202020202020203C78736C3A76616C75652D6F662073656C6563743D2276657273696F6E2D6E616D6522202F3E0D0A0920202020202020203C2F613E0D0A09093C2F74643E0D0A09093C74643E3C2F74643E0D0A09093C74643E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D2276657273696F6E2D64617465222F3E0D0A09093C2F74643E0D0A09093C74643E0D0A09093C2F74643E0D0A093C2F74723E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2262726561646372756D6273223E0D0A3C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2F706F72746C65742F706F72746C65742D69647D262333383B706167655F69643D7B2F706F72746C65742F706167652D69647D262333383B766965775F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B2F706F72746C65742F6A73723137302D706F72746C65742F63757272656E742D766965772D69647D262333383B66696C655F69645F7B2F706F72746C65742F706F72746C65742D69647D3D23706F72746C65745F69645F7B2F706F72746C65742F706F72746C65742D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D22203E0D0A093C78736C3A746578743E726163696E652F3C2F78736C3A746578743E0D0A3C2F613E0D0A3C78736C3A6170706C792D74656D706C617465732073656C6563743D2262726561646372756D62222F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2262726561646372756D62223E0D0A3C6120687265663D227B24736974652D706174687D3F706F72746C65745F69643D7B2F706F72746C65742F706F72746C65742D69647D262333383B706167655F69643D7B2F706F72746C65742F706167652D69647D262333383B766965775F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B2F706F72746C65742F6A73723137302D706F72746C65742F63757272656E742D766965772D69647D262333383B66696C655F69645F7B2F706F72746C65742F706F72746C65742D69647D3D7B62726561646372756D622D706174682D69647D23706F72746C65745F69645F7B2F706F72746C65742F706F72746C65742D69647D262333383B706167655F69643D7B2E2E2F2E2E2F706167652D69647D22203E0D0A093C78736C3A76616C75652D6F662073656C6563743D2262726561646372756D622D6E616D6522202F3E3C78736C3A746578743E2F3C2F78736C3A746578743E0D0A3C2F613E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2261646D696E2D766965772D636F6D626F223E0D0A093C68333E0D0A0909457370616365206465207472617661696C203A0D0A09202020203C666F726D206E616D653D226368616E67655F776F726B73706163652220616374696F6E3D226A73702F736974652F706C7567696E732F6A73723137302F4368616E6765566965772E6A737022206D6574686F643D22706F7374223E0D0A0909093C696E70757420747970653D2268696464656E22206E616D653D22706F72746C65745F6964222076616C75653D227B2E2E2F2E2E2F706F72746C65742D69647D22202F3E0D0A0909093C696E70757420747970653D2268696464656E222076616C75653D227B2E2E2F63757272656E742D766965772D69647D22206E616D653D226F6C645F766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D22202F3E0D0A0909093C696E70757420747970653D2268696464656E22206E616D653D22706167655F6964222076616C75653D227B2E2E2F2E2E2F706167652D69647D22202F3E0D0A0D0A0909093C73656C656374206E616D653D22766965775F69645F7B2E2E2F2E2E2F706F72746C65742D69647D223E0D0A090920202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2261646D696E2D7669657722202F3E0D0A0920202020202020203C2F73656C6563743E0D0A0920202020202020203C696E70757420747970653D227375626D6974222076616C75653D224D6F64696669657222202F3E0D0A20202020202020203C2F666F726D3E0D0A202020203C2F68333E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2261646D696E2D76696577223E0D0A09093C78736C3A63686F6F73653E0D0A0909093C78736C3A7768656E20746573743D2261646D696E2D766965772D69643D2E2E2F2E2E2F63757272656E742D766965772D6964223E0D0A090909093C6F7074696F6E2076616C75653D227B61646D696E2D766965772D69647D222073656C65637465643D2273656C6563746564223E3C78736C3A76616C75652D6F662073656C6563743D2261646D696E2D766965772D6E616D65222F3E3C2F6F7074696F6E3E0D0A0909093C2F78736C3A7768656E3E0D0A0909093C78736C3A6F74686572776973653E0D0A090909093C6F7074696F6E2076616C75653D227B61646D696E2D766965772D69647D223E3C78736C3A76616C75652D6F662073656C6563743D2261646D696E2D766965772D6E616D65222F3E3C2F6F7074696F6E3E0D0A0909093C2F78736C3A6F74686572776973653E0D0A09093C2F78736C3A63686F6F73653E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D2266696C652D636F6E74656E74223E0D0A093C74723E0D0A09093C746420636F6C7370616E3D2235223E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D222E22202F3E0D0A09093C2F74643E0D0A093C2F74723E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226A73723137302D706F72746C65742D6572726F72223E0D0A093C78736C3A76616C75652D6F662073656C6563743D222E22202F3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A);
/*!40000 ALTER TABLE `core_stylesheet` ENABLE KEYS */;

