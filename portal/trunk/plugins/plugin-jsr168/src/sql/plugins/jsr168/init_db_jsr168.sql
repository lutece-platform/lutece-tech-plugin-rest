#
# Table structure for table 'portlet_jsr168'
#

CREATE TABLE portlet_jsr168 (
  id_portlet int(11) NOT NULL default '0',
  jsr168Name varchar(100) NOT NULL,
  PRIMARY KEY  (id_portlet),
  KEY index_portlet_jsr168 (id_portlet)
) TYPE=MyISAM;
