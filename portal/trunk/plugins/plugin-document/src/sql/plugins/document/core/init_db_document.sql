--
-- Dumping data for table `document_attribute_type`
--

INSERT INTO `document_attribute_type` (`code_attribute_type`,`name_key`,`description_key`,`manager_class`) VALUES 
 ('file','document.attributeType.file.name','document.attributeType.file.description','fr.paris.lutece.plugins.document.service.attributes.FileManager'),
 ('listbox','document.attributeType.listbox.name','document.attributeType.listbox.description','fr.paris.lutece.plugins.document.service.attributes.ListBoxManager'),
 ('multiline','document.attributeType.multiline.name','document.attributeType.multiline.description','fr.paris.lutece.plugins.document.service.attributes.MultilineManager'),
 ('richtext','document.attributeType.richtext.name','document.attributeType.richtext.description','fr.paris.lutece.plugins.document.service.attributes.RichTextManager'),
 ('text','document.attributeType.text.name','document.attributeType.text.description','fr.paris.lutece.plugins.document.service.attributes.TextManager'),
 ('url','document.attributeType.url.name','document.attributeType.url.description','fr.paris.lutece.plugins.document.service.attributes.UrlManager'),
 ('date','document.attributeType.date.name','document.attributeType.date.description','fr.paris.lutece.plugins.document.service.attributes.DateManager');
INSERT INTO `document_attribute_type` (`code_attribute_type`,`name_key`,`description_key`,`manager_class`) VALUES 
 ('numerictext','document.attributeType.numerictext.name','document.attributeType.numerictext.description','fr.paris.lutece.plugins.document.service.attributes.NumerictextManager');


--
-- Dumping data for table `document_attribute_type_parameter`
--

INSERT INTO `document_attribute_type_parameter` (`code_attribute_type`,`parameter_name`,`parameter_label_key`,`parameter_index`,`parameter_description_key`,`parameter_default_value`) VALUES 
 ('text','size','document.attributeType.text.parameter.size.label',1,'document.attributeType.text.parameter.size.description','50'),
 ('text','maxlength','document.attributeType.text.parameter.maxlength.label',2,'document.attributeType.text.parameter.maxlength.description','60'),
 ('listbox','items','document.attributeType.listbox.parameter.items.label',2,'document.attributeType.listbox.parameter.items.description',''),
 ('listbox','value','document.attributeType.listbox.parameter.defaultvalue.label',1,'document.attributeType.listbox.parameter.defaultvalue.description',''),
 ('numerictext','size','document.attributeType.numerictext.parameter.size.label',1,'document.attributeType.numerictext.parameter.size.description','5'),
 ('numerictext','maxlength','document.attributeType.numerictext.parameter.maxlength.label',2,'document.attributeType.numerictext.parameter.maxlength.description','5');
INSERT INTO `document_attribute_type_parameter` (`code_attribute_type`,`parameter_name`,`parameter_label_key`,`parameter_index`,`parameter_description_key`,`parameter_default_value`) VALUES 
 ('date','defaultvalue','document.attributeType.date.parameter.defaultvalue.label',1,'document.attributeType.date.parameter.defaultvalue.description',''),
 ('date','currentdate','document.attributeType.date.parameter.currentdate.label',2,'document.attributeType.date.parameter.currentdate.description','');

--
-- Dumping data for table `document_page_template`
--

INSERT INTO `document_page_template` (`id_page_template_document`,`page_template_path` ,`picture_path`, `description`) VALUES 
 (0,'/skin/plugins/document/document_content_service.html', 'images/admin/skin/plugins/document/page_templates/page_template_document0.png', 'Défaut'),
 (1,'/skin/plugins/document/page_templates/page_template_document1.html', 'images/admin/skin/plugins/document/page_templates/page_template_document1.png', 'Pleine page'),
 (2,'/skin/plugins/document/page_templates/page_template_document2.html', 'images/admin/skin/plugins/document/page_templates/page_template_document2.png', 'Rubrique en-dessous');
 
 
 
--
-- Dumping data for table `document_rule`
--
INSERT INTO `document_rule` (`id_rule`,`rule_type`) VALUES 
 (3,'moveSpace'),
 (2,'moveSpace'),
 (1,'moveSpace'),
 (4,'moveSpace'),
 (6,'moveSpace');


--
-- Dumping data for table `document_rule_attributes`
--

INSERT INTO `document_rule_attributes` (`id_rule`,`attribute_name`,`attribute_value`) VALUES 
 (3,'id_state','4'),
 (2,'id_state','3'),
 (3,'id_space_source','3'),
 (3,'id_space_destination','2'),
 (1,'id_space_destination','3'),
 (1,'id_state','2'),
 (2,'id_space_source','3'),
 (2,'id_space_destination','4'),
 (1,'id_space_source','2'),
 (4,'id_space_source','4'),
 (4,'id_space_destination','15'),
 (4,'id_state','5'),
 (6,'id_space_source','15'),
 (6,'id_space_destination','4'),
 (6,'id_state','3');


--
-- Dumping data for table `document_space`
--

INSERT INTO `document_space` (`id_space`,`id_parent`,`name`,`description`,`view`,`id_space_icon`,`space_order`,`document_creation_allowed`) VALUES 
 (0,-1,'Racine des contenus','Racine des contenus','detail',1,0,NULL),
 (2,1,'Espace de travail','Espace de travail','detail',10,0,1),
 (3,1,'Espace de validation','Espace de validation','detail',11,1,0),
 (4,1,'Espace de publication','Espace de publication','detail',12,2,0),
 (6,5,'Images','Images','thumbnail',2,0,1),
 (5,0,'Bibliotheque multimedia','Bibliotheque multimedia','detail',1,1,NULL),
 (1,0,'Contenu Editorial','Contenu Editorial','detail',1,0,NULL),
 (7,0,'Fichiers en telechargement','Fichiers en telechargement','detail',1,2,NULL),
 (8,7,'Fichiers PDF','Fichiers PDF','detail',4,0,1),
 (15,1,'Archives','Archives','detail',9,4,0),
 (14,0,'Acteurs locaux','Contient les fiches acteurs locaux','detail',5,3,1),
 (16,5,'Fichiers son','Fichiers son','detail',8,1,1),
 (17,5,'Video','Video','detail',7,2,1),
 (18,5,'Icones et pictos','Icones et pictos','detail',2,NULL,1);


--
-- Dumping data for table `document_space_action`
--

INSERT INTO `document_space_action` (`id_action`,`name_key`,`description_key`,`action_url`,`icon_url`,`action_permission`) VALUES 
 (1,'document.spaces.action.createSpace.name','document.spaces.action.createSpace.description','jsp/admin/plugins/document/CreateSpace.jsp','images/admin/skin/plugins/document/actions/new_space.png','CREATE'),
 (2,'document.spaces.action.deleteSpace.name','document.spaces.action.deleteSpace.description','jsp/admin/plugins/document/DeleteSpace.jsp','images/admin/skin/plugins/document/actions/delete.png','DELETE'),
 (3,'document.spaces.action.modifySpace.name','document.spaces.action.modifySpace.description','jsp/admin/plugins/document/ModifySpace.jsp','images/admin/skin/plugins/document/actions/modify_space.png','MODIFY'),
 (4,'document.spaces.action.manageUsers.name','document.spaces.action.manageUsers.description','jsp/admin/plugins/document/ManageSpaceUsers.jsp','images/admin/skin/plugins/document/actions/users.png','USERS'),
 (5,'document.spaces.action.moveSpace.name','document.spaces.action.moveSpace.description','jsp/admin/plugins/document/MoveSpace.jsp','images/admin/skin/plugins/document/actions/move_space.png','MOVE');


--
-- Dumping data for table `document_space_document_type`
--

INSERT INTO `document_space_document_type` (`id_space`,`code_document_type`) VALUES 
 (2,'article'),
 (2,'dvd'),
 (2,'shortarticle'),
 (3,'article'),
 (3,'dvd'),
 (3,'shortarticle'),
 (4,'article'),
 (4,'dvd'),
 (4,'shortarticle'),
 (6,'image'),
 (8,'pdf'),
 (14,'actor'),
 (15,'article'),
 (15,'dvd'),
 (15,'shortarticle'),
 (16,'sound'),
 (17,'video'),
 (18,'image');

--
-- Dumping data for table `document_space_icon`
--

INSERT INTO `document_space_icon` (`id_space_icon`,`icon_url`) VALUES 
 (1,'images/admin/skin/plugins/document/spaces/space.png'),
 (2,'images/admin/skin/plugins/document/spaces/space_images.png'),
 (3,'images/admin/skin/plugins/document/spaces/space_download.png'),
 (4,'images/admin/skin/plugins/document/spaces/space_pdf.png'),
 (5,'images/admin/skin/plugins/document/spaces/space_users.png'),
 (6,'images/admin/skin/plugins/document/spaces/space_multimedia.png'),
 (7,'images/admin/skin/plugins/document/spaces/space_video.png'),
 (8,'images/admin/skin/plugins/document/spaces/space_sound.png'),
 (9,'images/admin/skin/plugins/document/spaces/space_archive.png'),
 (10,'images/admin/skin/plugins/document/spaces/space_working.png'),
 (11,'images/admin/skin/plugins/document/spaces/space_validation.png'),
 (12,'images/admin/skin/plugins/document/spaces/space_published.png');


--
-- Dumping data for table `document_type`
--

INSERT INTO `document_type` (`code_document_type`,`name`,`description`,`thumbnail_attribute_id`,`default_thumbnail_url`,`admin_xsl`,`content_service_xsl`,`metadata_handler`) VALUES 
 ('actor','Fiche acteur','Fiche acteur local',41,'',NULL,0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A200D0A202020203C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A20202020203C78736C3A74656D706C617465206D617463683D226163746F72223E0D0A20202020202020203C703E0D0A2020202020202020202020203C78736C3A63686F6F73653E0D0A202020202020202020202020202020203C78736C3A7768656E20746573743D226163746F722D70686F746F2F66696C652D7265736F75726365213D2727223E0D0A202020202020202020202020202020202020202020203C696D67207372633D22646F63756D656E743F69643D7B6163746F722D70686F746F2F66696C652D7265736F757263652F7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B6163746F722D70686F746F2F66696C652D7265736F757263652F7265736F757263652D6174747269627574652D69647D2220616C69676E3D226C656674222076616C69676E3D226D6964646C6522206873706163653D22323022202F3E0D0A202020202020202020202020202020203C2F78736C3A7768656E3E0D0A202020202020202020202020202020203C78736C3A6F74686572776973653E2020202020202020202020202020200D0A2020202020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A2020202020202020202020203C2F78736C3A63686F6F73653E0D0A202020202020202020202020203C7374726F6E673E203C78736C3A76616C75652D6F662073656C6563743D226163746F722D66697273746E616D6522202F3E2026233136303B203C78736C3A76616C75652D6F662073656C6563743D226163746F722D6C6173746E616D6522202F3E3C2F7374726F6E673E0D0A20202020202020203C2F703E0D0A20202020202020203C703E0D0A2020202020202020202020202020202020466F6E6374696F6E203A203C78736C3A76616C75652D6F662073656C6563743D226163746F722D66756E6374696F6E22202F3E0D0A20202020202020203C2F703E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020203C2F78736C3A74656D706C6174653E0D0A0D0A200D0A2020203C78736C3A74656D706C617465206D617463683D2266696C652D7265736F75726365223E0D0A20202020202020203C78736C3A63686F6F73653E0D0A2020202020202020202020203C78736C3A7768656E20746573743D22287265736F757263652D636F6E74656E742D747970653D27696D6167652F6A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F6A706727206F7220207265736F757263652D636F6E74656E742D747970653D27696D6167652F706A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F67696627206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F706E67272922203E0D0A202020202020202020202020202020203C696D67207372633D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D2220616C69676E3D22726967687422202F3E0D0A2020202020202020202020203C2F78736C3A7768656E3E0D0A2020202020202020202020203C78736C3A6F74686572776973653E0D0A202020202020202020202020202020203C6120687265663D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D223E200D0A20202020202020202020202020202020202020203C696D67207372633D22696D616765732F61646D696E2F736B696E2F706C7567696E732F646F63756D656E742F66696C6574797065732F66696C652E706E672220626F726465723D223022202F3E0D0A202020202020202020202020202020203C2F613E0D0A2020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A20202020202020203C2F78736C3A63686F6F73653E0D0A202020203C2F78736C3A74656D706C6174653E0D0A3C2F78736C3A7374796C6573686565743E0D0A0D0A200D0A0D0A,'dublincore'),
 ('article','Article','Article',0,'',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A200D0A0D0A202020203C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A20202020203C78736C3A74656D706C617465206D617463683D2261727469636C65223E0D0A20202020202020203C703E0D0A2020202020202020202020203C7374726F6E673E236931386E7B646F63756D656E742E646F63756D656E745F61646D696E5F64656661756C745F78736C2E6C6162656C5469746C657D203A20203C2F7374726F6E673E0D0A2020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22646F63756D656E742D7469746C6522202F3E0D0A20202020202020203C2F703E0D0A20202020202020203C703E0D0A2020202020202020202020203C7374726F6E673E236931386E7B646F63756D656E742E646F63756D656E745F61646D696E5F64656661756C745F78736C2E6C6162656C53756D6D6172797D203A20203C2F7374726F6E673E0D0A2020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22646F63756D656E742D73756D6D61727922202F3E0D0A20202020202020203C2F703E0D0A20202020202020203C703E0D0A2020202020202020202020203C7374726F6E673E46696368696572203A203C2F7374726F6E673E200D0A2020202020202020203C2F703E0D0A2020202020202020203C703E2020200D0A2020202020202020202020203C78736C3A63686F6F73653E0D0A202020202020202020202020202020203C78736C3A7768656E20746573743D2261727469636C652D6174746163686D656E742F66696C652D7265736F75726365213D2727223E0D0A20202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2261727469636C652D6174746163686D656E742F66696C652D7265736F7572636522202F3E0D0A202020202020202020202020202020203C2F78736C3A7768656E3E0D0A202020202020202020202020202020203C78736C3A6F74686572776973653E2020202020202020202020202020200D0A2020202020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A2020202020202020202020203C2F78736C3A63686F6F73653E0D0A20202020202020203C2F703E20202020202020200D0A092020202020203C703E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D2261727469636C652D626F647922202F3E200D0A0920202020203C2F703E090D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020203C2F78736C3A74656D706C6174653E0D0A0D0A200D0A2020203C78736C3A74656D706C617465206D617463683D2266696C652D7265736F75726365223E0D0A20202020202020203C78736C3A63686F6F73653E0D0A2020202020202020202020203C78736C3A7768656E20746573743D22287265736F757263652D636F6E74656E742D747970653D27696D6167652F6A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F6A706727206F7220207265736F757263652D636F6E74656E742D747970653D27696D6167652F706A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F67696627206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F706E67272922203E0D0A202020202020202020202020202020203C696D67207372633D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D2220616C69676E3D22726967687422202F3E0D0A2020202020202020202020203C2F78736C3A7768656E3E2020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020200D0A2020202020202020202020203C78736C3A6F74686572776973653E0D0A202020202020202020202020202020203C6120687265663D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D223E200D0A20202020202020202020202020202020202020203C696D67207372633D22696D616765732F61646D696E2F736B696E2F706C7567696E732F646F63756D656E742F66696C6574797065732F66696C652E706E672220626F726465723D223022202F3E0D0A202020202020202020202020202020203C2F613E0D0A2020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A20202020202020203C2F78736C3A63686F6F73653E0D0A202020203C2F78736C3A74656D706C6174653E0D0A3C2F78736C3A7374796C6573686565743E0D0A,0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A200D0A0D0A202020203C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A20202020203C78736C3A74656D706C617465206D617463683D2261727469636C65223E0D0A20202020202020203C703E0D0A2020202020202020202020203C7374726F6E673E3C78736C3A76616C75652D6F662073656C6563743D22646F63756D656E742D7469746C6522202F3E3C2F7374726F6E673E0D0A20202020202020203C2F703E0D0A20202020202020203C703E0D0A2020202020202020202020203C78736C3A63686F6F73653E0D0A202020202020202020202020202020203C78736C3A7768656E20746573743D2261727469636C652D6174746163686D656E742F66696C652D7265736F75726365213D2727223E0D0A20202020202020202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D2261727469636C652D6174746163686D656E742F66696C652D7265736F7572636522202F3E0D0A202020202020202020202020202020203C2F78736C3A7768656E3E0D0A202020202020202020202020202020203C78736C3A6F74686572776973653E2020202020202020202020202020200D0A2020202020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A2020202020202020202020203C2F78736C3A63686F6F73653E0D0A20202020202020203C2F703E202020200D0A0920203C703E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D2261727469636C652D626F647922202F3E200D0A0920203C2F703E090D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020203C2F78736C3A74656D706C6174653E0D0A0D0A200D0A2020203C78736C3A74656D706C617465206D617463683D2266696C652D7265736F75726365223E0D0A20202020202020203C78736C3A63686F6F73653E0D0A2020202020202020202020203C78736C3A7768656E20746573743D22287265736F757263652D636F6E74656E742D747970653D27696D6167652F6A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F6A706727206F7220207265736F757263652D636F6E74656E742D747970653D27696D6167652F706A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F67696627206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F706E67272922203E0D0A202020202020202020202020202020203C696D67207372633D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D2220616C69676E3D22726967687422202F3E0D0A2020202020202020202020203C2F78736C3A7768656E3E0D0A2020202020202020202020203C78736C3A6F74686572776973653E0D0A202020202020202020202020202020203C6120687265663D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D223E200D0A20202020202020202020202020202020202020203C696D67207372633D22696D616765732F61646D696E2F736B696E2F706C7567696E732F646F63756D656E742F66696C6574797065732F66696C652E706E672220626F726465723D223022202F3E0D0A202020202020202020202020202020203C2F613E0D0A2020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A20202020202020203C2F78736C3A63686F6F73653E0D0A202020203C2F78736C3A74656D706C6174653E0D0A3C2F78736C3A7374796C6573686565743E,'dublincore');
INSERT INTO `document_type` (`code_document_type`,`name`,`description`,`thumbnail_attribute_id`,`default_thumbnail_url`,`admin_xsl`,`content_service_xsl`,`metadata_handler`) VALUES 
 ('shortarticle','Brèves','Brèves',0,'',NULL,NULL,'none'),
 ('image','Image','Image',43,'',NULL,0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A200D0A0D0A202020203C78736C3A6F7574707574206D6574686F643D2268746D6C2220696E64656E743D22796573222F3E0D0A0D0A20202020203C78736C3A74656D706C617465206D617463683D22696D616765223E0D0A093C703E0D0A09202020203C7374726F6E673E203C78736C3A76616C75652D6F662073656C6563743D22646F63756D656E742D7469746C6522202F3E3C2F7374726F6E673E0D0A093C2F703E0D0A0D0A2020202020202020202020203C78736C3A63686F6F73653E0D0A202020202020202020202020202020203C78736C3A7768656E20746573743D22696D6167652D66696C652F66696C652D7265736F75726365213D2727223E0D0A20202020202020202020202020202020202020202020202020202020203C696D67207372633D22646F63756D656E743F69643D7B696D6167652D66696C652F66696C652D7265736F757263652F7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B696D6167652D66696C652F66696C652D7265736F757263652F7265736F757263652D6174747269627574652D69647D2220202F3E0D0A202020202020202020202020202020203C2F78736C3A7768656E3E0D0A202020202020202020202020202020203C78736C3A6F74686572776973653E2020202020202020202020202020200D0A2020202020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A2020202020202020202020203C2F78736C3A63686F6F73653E0D0A093C703E0D0A202020202020093C78736C3A76616C75652D6F662073656C6563743D22646F63756D656E742D73756D6D61727922202F3E0D0A093C2F703E0D0A0920203C703E0D0A20202020202020202020202020203C78736C3A76616C75652D6F662064697361626C652D6F75747075742D6573636170696E673D22796573222073656C6563743D22696D6167652D6372656469747322202F3E200D0A093C2F703E090D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A20202020202020203C6272202F3E0D0A0D0A20202020203C2F78736C3A74656D706C6174653E0D0A0D0A200D0A2020203C78736C3A74656D706C617465206D617463683D2266696C652D7265736F75726365223E0D0A20202020202020203C78736C3A63686F6F73653E0D0A2020202020202020202020203C78736C3A7768656E20746573743D22287265736F757263652D636F6E74656E742D747970653D27696D6167652F6A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F6A706727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F706A70656727206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F67696627206F72207265736F757263652D636F6E74656E742D747970653D27696D6167652F706E67272922203E0D0A202020202020202020202020202020203C696D67207372633D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D2220616C69676E3D22726967687422202F3E0D0A2020202020202020202020203C2F78736C3A7768656E3E0D0A2020202020202020202020203C78736C3A6F74686572776973653E0D0A202020202020202020202020202020203C6120687265663D22646F63756D656E743F69643D7B7265736F757263652D646F63756D656E742D69647D26616D703B69645F6174747269627574653D7B7265736F757263652D6174747269627574652D69647D223E200D0A20202020202020202020202020202020202020203C696D67207372633D22696D616765732F61646D696E2F736B696E2F706C7567696E732F646F63756D656E742F66696C6574797065732F66696C652E706E672220626F726465723D223022202F3E0D0A202020202020202020202020202020203C2F613E0D0A2020202020202020202020203C2F78736C3A6F74686572776973653E20202020202020200D0A20202020202020203C2F78736C3A63686F6F73653E0D0A202020203C2F78736C3A74656D706C6174653E0D0A3C2F78736C3A7374796C6573686565743E,'dublincore'),
 ('video','Video','Video',0,'',NULL,NULL,'dublincore');
INSERT INTO `document_type` (`code_document_type`,`name`,`description`,`thumbnail_attribute_id`,`default_thumbnail_url`,`admin_xsl`,`content_service_xsl`,`metadata_handler`) VALUES 
 ('pdf','PDF','Fichier PDF',0,'images/admin/skin/plugins/document/filetypes/pdf.png',NULL,NULL,'dublincore'),
 ('sound','Enregistrement sonore','Enregistrement sonore',0,'',NULL,NULL,'dublincore');


--
-- Dumping data for table `document_type_attributes`
--

INSERT INTO `document_type_attributes` (`id_document_attribute`,`code_document_type`,`code_attribute_type`,`code`,`name`,`description`,`attribute_order`,`required`,`searchable`) VALUES 
 (12,'article','richtext','body','Corps','Corps de l\'article',5,1,1),
 (8,'dossier','multiline','body','corps','Corps du dossier',0,1,1),
 (11,'article','file','attachment','Piece jointe','Piece jointe',4,0,0),
 (10,'article','url','url','Url','Url liée à l\'article',2,0,0),
 (18,'dvd','url','url','URL','URL du site du Film',3,0,0),
 (20,'dvd','file','cover','Pochette','Image de la jaquette',4,0,0),
 (30,'actor','text','firstname','Prenom','Prenom',1,1,1),
 (31,'actor','text','lastname','Nom','Nom',2,1,1),
 (32,'actor','multiline','function','Fontion','Fonction',3,0,0),
 (39,'video','multiline','comments','commentaires','Commentaires sur la video',2,1,1),
 (40,'video','file','file','Fichier video','Fichier video',3,1,0),
 (41,'actor','file','photo','Photo','Photo',4,0,0),
 (43,'image','file','file','Fichier','Fichier image',2,1,0),
 (44,'image','text','credits','Credits','Credits',3,1,0);
INSERT INTO `document_type_attributes` (`id_document_attribute`,`code_document_type`,`code_attribute_type`,`code`,`name`,`description`,`attribute_order`,`required`,`searchable`) VALUES 
 (48,'pdf','file','file','Fichier','Fichier',2,1,1),
 (50,'sound','file','file','Fichier','Fichier son',2,1,0),
 (51,'sound','text','author','Auteur','Auteur',3,0,1);


--
-- Dumping data for table `document_type_attributes_parameters`
--

INSERT INTO `document_type_attributes_parameters` (`id_document_attribute`,`parameter_name`,`id_list_parameter`,`parameter_value`) VALUES 
 (30,'maxlength',1,'50'),
 (30,'size',1,'50'),
 (31,'maxlength',1,'50'),
 (31,'size',1,'50'),
 (44,'size',1,'60'),
 (44,'maxlength',1,'100'),
 (51,'size',1,'60'),
 (51,'maxlength',1,'60');


--
-- Dumping data for table `document_view`
--

INSERT INTO `document_view` (`code_view`,`name_key`) VALUES 
 ('detail','document.view.detail'),
 ('thumbnail','document.view.thumbnail');


--
-- Dumping data for table `document_workflow_action`
--

INSERT INTO `document_workflow_action` (`id_action`,`name_key`,`description_key`,`action_url`,`icon_url`,`action_permission`) VALUES 
 (1,'document.workflow.action.deleteDocument.name','document.workflow.action.deleteDocument.description','jsp/admin/plugins/document/DeleteDocument.jsp?','images/admin/skin/plugins/document/actions/delete.png','DELETE'),
 (2,'document.workflow.action.modifyDocument.name','document.workflow.action.modifyDocument.description','jsp/admin/plugins/document/ModifyDocument.jsp?','images/admin/skin/plugins/document/actions/modify.png','MODIFY'),
 (3,'document.workflow.action.submitForApproval.name','document.workflow.action.submitForApproval.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=2&amp;','images/admin/skin/plugins/document/actions/submit.png','SUBMIT'),
 (4,'document.workflow.action.approveDocument.name','document.workflow.action.approveDocument.description','jsp/admin/plugins/document/DoValidateDocument.jsp?id_state=3&amp;','images/admin/skin/plugins/document/actions/validate.png','VALIDATE'),
 (5,'document.workflow.action.publishDocument.name','document.workflow.action.publishDocument.description','jsp/admin/plugins/document/ManageDocumentPublishing.jsp?','images/admin/skin/plugins/document/actions/publish.png','PUBLISH');
INSERT INTO `document_workflow_action` (`id_action`,`name_key`,`description_key`,`action_url`,`icon_url`,`action_permission`) VALUES 
 (6,'document.workflow.action.rejectDocument.name','document.workflow.action.rejectDocument.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=4&amp;','images/admin/skin/plugins/document/actions/reject.png','VALIDATE'),
 (7,'document.workflow.action.submitAgain.name','document.workflow.action.submitAgain.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=2&amp;','images/admin/skin/plugins/document/actions/submit_again.png','SUBMIT'),
 (8,'document.workflow.action.archiveDocument.name','document.workflow.action.archiveDocument.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=5&amp;','images/admin/skin/plugins/document/actions/archive.png','ARCHIVE'),
 (9,'document.workflow.action.changeDocument.name','document.workflow.action.changeDocument.description','jsp/admin/plugins/document/ModifyDocument.jsp?id_state=6&amp;','images/admin/skin/plugins/document/actions/change.png','CHANGE'),
 (10,'document.workflow.action.submitChanges.name','document.workflow.action.submitChanges.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=7&amp;','images/admin/skin/plugins/document/actions/submit_changes.png','SUBMIT');
INSERT INTO `document_workflow_action` (`id_action`,`name_key`,`description_key`,`action_url`,`icon_url`,`action_permission`) VALUES 
 (11,'document.workflow.action.rejectChanges.name','document.workflow.action.rejectChanges.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=6&amp;','images/admin/skin/plugins/document/actions/reject_changes.png','VALIDATE'),
 (13,'document.workflow.action.unarchiveDocument.name','document.workflow.action.unarchiveDocument.description','jsp/admin/plugins/document/DoChangeState.jsp?id_state=3&amp;','images/admin/skin/plugins/document/actions/unarchive.png','ARCHIVE'),
 (12,'document.workflow.action.approveChanges.name','document.workflow.action.approveChanges.description','jsp/admin/plugins/document/DoValidateDocument.jsp?id_state=3&amp;','images/admin/skin/plugins/document/actions/validate.png','VALIDATE'),
 (14,'document.workflow.action.history.name','document.workflow.action.history.description','jsp/admin/plugins/document/DocumentHistory.jsp?','images/admin/skin/plugins/document/actions/history.png','VIEW_HISTORY'),
 (15,'document.workflow.action.previewDocument.name','document.workflow.action.previewDocument.description','jsp/admin/plugins/document/PreviewDocument.jsp?','images/admin/skin/plugins/document/actions/view.png','VIEW');
INSERT INTO `document_workflow_action` (`id_action`,`name_key`,`description_key`,`action_url`,`icon_url`,`action_permission`) VALUES 
 (16,'document.workflow.action.moveDocument.name','document.workflow.action.moveDocument.description','jsp/admin/plugins/document/MoveDocument.jsp?','images/admin/skin/plugins/document/actions/move.png','MOVE'),
 (17,'document.workflow.action.commentDocument.name','document.workflow.action.commentDocument.description','jsp/admin/plugins/document/ManageDocumentComments.jsp?','images/admin/skin/plugins/document/actions/comment.png','COMMENT');

--
-- Dumping data for table `document_workflow_state`
--

INSERT INTO `document_workflow_state` (`id_state`,`name_key`,`description_key`,`state_order`) VALUES 
 (1,'document.workflow.state.writing.name','document.workflow.state.writing.description',1),
 (2,'document.workflow.state.waitingForApproval.name','document.workflow.state.waitingForApproval.description',2),
 (3,'document.workflow.state.approved.name','document.workflow.state.approved.description',3),
 (4,'document.workflow.state.rejected.name','document.workflow.state.rejected.description',4),
 (5,'document.workflow.state.archived.name','document.workflow.state.archived.description',5),
 (6,'document.workflow.state.inChange.name','document.workflow.state.inChange.description',6),
 (7,'document.workflow.state.waitingForChangesApproval.name','document.workflow.state.waitingForChangesApproval.name',7);


--
-- Dumping data for table `document_workflow_transition`
--

INSERT INTO `document_workflow_transition` (`id_state`,`id_action`) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,14),
 (1,15),
 (1,16),
 (2,2),
 (2,4),
 (2,6),
 (2,14),
 (2,15),
 (2,16),
 (3,0),
 (3,5),
 (3,8),
 (3,9),
 (3,14),
 (3,15),
 (4,1),
 (4,2),
 (4,7),
 (4,14),
 (4,15),
 (4,16),
 (5,1),
 (5,13),
 (5,14),
 (5,15),
 (6,9),
 (6,10),
 (6,14),
 (6,15),
 (6,16),
 (7,11),
 (7,12),
 (7,14),
 (7,15),
 (7,16),
 (3,17);
