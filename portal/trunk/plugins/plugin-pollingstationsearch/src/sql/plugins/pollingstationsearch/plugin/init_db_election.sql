SHOW WARNINGS\G
load data local infile './election/num_ext.csv' into table number_suffix fields terminated by ';';
SHOW WARNINGS\G
load data local infile './election/type_voie.csv' into table street_type fields terminated by ';';
 SHOW WARNINGS\G
load data local infile './election/bv_parisfr.csv' into table polling_station fields terminated by ';';
SHOW WARNINGS\G
load data local infile './election/Rues_parisfr.csv' into table street fields terminated by ';';
SHOW WARNINGS\G
load data local infile './election/pa_parisfr.csv' into table address fields terminated by ';';