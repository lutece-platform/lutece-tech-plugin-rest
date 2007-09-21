CREATE TABLE link (
  id_link int(11) NOT NULL default '0',
  name varchar(200) default NULL,
  description tinytext,
  date date default NULL,
  url varchar(100) default NULL,
  image_content longblob default NULL,
  workgroup_key varchar(250) default NULL,
  mime_type varchar(255),
  PRIMARY KEY  (id_link)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Table structure for table 'link_virtual_host'
#

CREATE TABLE link_virtual_host (
  id_link int(11) NOT NULL default '0',
  virtual_host_key VARCHAR(45) NOT NULL DEFAULT '',
  url VARCHAR(100) NOT NULL DEFAULT '',
  PRIMARY KEY(id_link, virtual_host_key)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
