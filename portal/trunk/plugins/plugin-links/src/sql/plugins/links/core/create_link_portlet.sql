
#
# Table structure for table 'portlet_link'
#
CREATE TABLE link_portlet (
  id_portlet int(11) NOT NULL default '0',
  portlet_link_order int(11) default '0',
  PRIMARY KEY  (id_portlet)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Table structure for table 'portlet_link_list'
#
CREATE TABLE link_list_portlet (
  id_portlet int(11) NOT NULL default '0',
  id_link int(11) NOT NULL default '0',
  link_order int(11) default NULL,
  PRIMARY KEY  (id_portlet,id_link)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;