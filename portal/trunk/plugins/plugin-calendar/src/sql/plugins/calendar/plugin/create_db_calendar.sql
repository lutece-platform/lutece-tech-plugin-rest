--
-- Table structure for table `calendar_agendas`
--

DROP TABLE IF EXISTS `calendar_agendas`;
CREATE TABLE `calendar_agendas` (
  `id_agenda` int(11) NOT NULL default '0',
  `agenda_name` varchar(130) default NULL,
  `agenda_image` varchar(130) default NULL,
  `agenda_prefix` varchar(130) default NULL,
  `role` varchar(130) NOT NULL,
  `workgroup_key` varchar(50) default NULL,
  `role_manage` varchar(130) default NULL,
  PRIMARY KEY  (`id_agenda`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


--
-- Table structure for table `calendar_events`
--

DROP TABLE IF EXISTS `calendar_events`;
CREATE TABLE `calendar_events` (
  `id_event` int(11) NOT NULL,
  `id_agenda` int(11) NOT NULL default '0',
  `event_date` varchar(8) NOT NULL,
  `event_time_start` varchar(5) default NULL,
  `event_time_end` varchar(5) default NULL,
  `event_title` varchar(60) NOT NULL,
  PRIMARY KEY  (`id_event`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;