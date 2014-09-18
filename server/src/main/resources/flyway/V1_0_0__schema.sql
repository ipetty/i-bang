-- users
create table users (
	id int primary key auto_increment,
	username varchar(50),
	email varchar(50),
	password varchar(50),
	salt varchar(20),
	nickname varchar(50),
	gender varchar(10),
	job varchar(50),
	avatar varchar(100),
	signature varchar(255),
	address varchar(255),
	created_on timestamp default current_timestamp
) engine=innodb default charset=utf8;
create index idx_username on users(username);
create index idx_email on users(email);
create index idx_address on users(address);

-- user_refresh_token
create table user_refresh_token (
	user_id int not null,
	device_screen_name varchar(50),
	device_id varchar(50), -- IMEI
	device_mac varchar(50),
	device_uuid varchar(50) not null, -- 客户端根据设备的多个唯一ID生成的UUID
	refresh_token varchar(50) not null,
	created_on timestamp default current_timestamp,
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_refresh_token on user_refresh_token(refresh_token);
create index idx_device_uuid on user_refresh_token(device_uuid);
