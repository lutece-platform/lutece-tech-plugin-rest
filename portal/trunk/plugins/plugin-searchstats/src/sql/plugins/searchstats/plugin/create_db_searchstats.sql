CREATE TABLE `searchstats_queries` (
  `yyyy` int(11) default NULL,
  `mm` int(11) default NULL,
  `dd` int(11) default NULL,
  `hh` int(11) default NULL,
  `query` varchar(255) character set utf8 collate utf8_unicode_ci default NULL,
  `results_count` int(11) default NULL
)  ENGINE=MyISAM DEFAULT CHARSET=utf8;
