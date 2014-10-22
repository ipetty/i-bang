-- location
create table location (
	id bigint primary key auto_increment,
	longitude double,
	latitude double,
	geo_hash varchar(50),
	coor_type varchar(20),
	radius float,
	province varchar(50),
	city varchar(50),
	district varchar(50),
	street varchar(50),
	street_number varchar(50),
	address varchar(255),
	silent boolean default true
) engine=innodb default charset=utf8;
create index idx_coor_type on location(coor_type);
create index idx_province on location(province);
create index idx_city on location(city);
create index idx_district on location(district);
create index idx_address on location(address);
create index idx_silent on location(silent);

-- location of seek
alter table seek add column location_id bigint;
create index idx_location_id on seek(location_id);

-- location of user
alter table users add column province varchar(50);
alter table users add column city varchar(50);
alter table users add column district varchar(50);
create index idx_province on users(province);
create index idx_city on users(city);
create index idx_district on users(district);
