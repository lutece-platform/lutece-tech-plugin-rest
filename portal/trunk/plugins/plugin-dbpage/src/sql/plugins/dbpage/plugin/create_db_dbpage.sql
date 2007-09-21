/*
 *  The database structure of dbpage which is used in dbpage plugin
 */


--
-- Table structure for table dbpage_page
--

CREATE TABLE dbpage_page (
  id_page int(4) NOT NULL default '0',
  param_name varchar(255) collate utf8_unicode_ci default NULL,
  title varchar(255) collate utf8_unicode_ci default NULL,
  workgroup_key varchar(50) NOT NULL default 'all',
  PRIMARY KEY  (id_page)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table dbpage_section
--

CREATE TABLE dbpage_section (
  id_section int(11) NOT NULL default '0',
  id_page int(11) default NULL,
  id_type int(255) default NULL,
  template varchar(255) collate utf8_unicode_ci default NULL,
  desc_column varchar(255) collate utf8_unicode_ci default NULL,
  desc_sql longtext collate utf8_unicode_ci NOT NULL,
  pool varchar(255) collate utf8_unicode_ci default NULL,
  title varchar(255) collate utf8_unicode_ci default NULL,
  id_order int(11) NOT NULL default '0',
  role varchar(255) NOT NULL default 'none',
  PRIMARY KEY  (id_section)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;


--
-- Table structure for table dbpage_section_type
--

CREATE TABLE dbpage_section_type (
  id_type int(4) NOT NULL default '0',
  class_desc varchar(255) collate utf8_unicode_ci default NULL,
  description varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (id_type)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
