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
	phone varchar(20),
	telephone varchar(20),
	avatar varchar(100),
	signature varchar(255),
	address varchar(255),
	created_on timestamp default current_timestamp,
	version int not null default 1
) engine=innodb default charset=utf8;
create index idx_username on users(username);
create index idx_email on users(email);
create index idx_address on users(address);
create index idx_created_on on users(created_on);

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

-- seeker_info
create table seeker_info (
	user_id int primary key,
	seek_count int default 0,
	total_point int default 0,
	title varchar(50),
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;

-- offerer_info
create table offerer_info (
	user_id int primary key,
	offer_count int default 0,
	total_point int default 0,
	title varchar(50),
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;

-- offerer_range
create table offerer_range (
	user_id int not null,
	category_l1 varchar(50),
	category_l2 varchar(50),
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_category_l1 on offerer_range(category_l1);
create index idx_category_l2 on offerer_range(category_l2);

-- seek
create table seek (
	id bigint primary key auto_increment,
	sn varchar(50),
	seeker_id int not null,
	contact_info_visible boolean default false,
	category_l1 varchar(50),
	category_l2 varchar(50),
	content text,
	requirement text,
	delegate_number int default 1,
	reward varchar(255),
	additional_reward varchar(255),
	created_on timestamp default current_timestamp,
	expire_date date,
	closed_on datetime,
	status varchar(20),
	foreign key(seeker_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_sn on seek(sn);
create index idx_category_l1 on seek(category_l1);
create index idx_category_l2 on seek(category_l2);
create index idx_created_on on seek(created_on);
create index idx_expire_date on seek(expire_date);
create index idx_status on seek(status);

-- image
create table image (
	id bigint primary key auto_increment,
	sn varchar(50),
	seek_id bigint,
	idx int,
	small_url varchar(255),
	original_url varchar(255)
) engine=innodb default charset=utf8;

-- offer
create table offer (
	id bigint primary key auto_increment,
	sn varchar(50),
	offerer_id int not null,
	seek_id bigint not null,
	content text,
	description text,
	deadline date,
	created_on timestamp default current_timestamp,
	closed_on datetime,
	status varchar(20),
	foreign key(offerer_id) references users(id),
	foreign key(seek_id) references seek(id)
) engine=innodb default charset=utf8;
create index idx_sn on offer(sn);
create index idx_deadline on offer(deadline);
create index idx_created_on on offer(created_on);
create index idx_status on offer(status);

-- delegation
create table delegation (
	id bigint primary key auto_increment,
	sn varchar(50),
	seek_id bigint not null,
	seeker_id int not null,
	offer_id bigint not null,
	offerer_id int not null,
	deadline date,
	created_on timestamp default current_timestamp,
	closed_on datetime,
	status varchar(20),
	foreign key(seek_id) references seek(id),
	foreign key(seeker_id) references users(id),
	foreign key(offer_id) references offer(id),
	foreign key(offerer_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_sn on delegation(sn);
create index idx_deadline on delegation(deadline);
create index idx_created_on on delegation(created_on);
create index idx_status on delegation(status);

-- evaluation
create table evaluation (
	id bigint primary key auto_increment,
	delegation_id bigint not null,
	type varchar(50),
	evaluator_id int not null,
	evaluate_target_id int not null,
	point int,
	content text,
	created_on timestamp default current_timestamp,
	foreign key(delegation_id) references delegation(id),
	foreign key(evaluator_id) references users(id),
	foreign key(evaluate_target_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_type on evaluation(type);
create index idx_created_on on evaluation(created_on);

-- system_message
create table system_message (
	id bigint primary key auto_increment,
	from_user_id int,
	receiver_id int not null,
	type varchar(50),
	content text,
	created_on timestamp default current_timestamp,
	foreign key(from_user_id) references users(id),
	foreign key(receiver_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_type on system_message(type);
create index idx_created_on on system_message(created_on);
