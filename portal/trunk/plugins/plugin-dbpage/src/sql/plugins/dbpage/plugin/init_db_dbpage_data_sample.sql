--
-- Dumping data for table dbpage_page
--

INSERT INTO dbpage_page VALUES (1,'Portlet Stats','Quelques exemples de section', 'all');


--
-- Dumping data for table dbpage_section
--

INSERT INTO  `dbpage_section` VALUES 
(1,1,1,'Portlet_Stats/form_header.html','Name','SELECT \'Ceci est un exemple\'','portal','Exemple de formulaire',1,'none'),
(3,1,2,'Portlet_Stats/table_select.html','core,id_portlet_type','select distinct c.id_portlet_type,t.name from core_portlet as c,core_portlet_type as t where c.id_portlet_type=t.id_portlet_type','portal','Liste deroulante',2,'none'),
(4,1,2,'Portlet_Stats/table_test.html','name,date_creation,date_update','select  name,date_creation ,date_update from core_portlet where  id_portlet_type=\"@value1@\" ORDER BY \"@value2@\"','portal','Table',3,'none');
