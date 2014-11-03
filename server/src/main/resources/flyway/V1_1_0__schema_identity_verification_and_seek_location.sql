-- identity_verification
create table identity_verification (
	user_id int primary key,
	real_name varchar(50),
	id_number varchar(20),
	id_card_front varchar(255),
	id_card_reverse_side varchar(255),
	id_card_in_hand varchar(255),
	submitted_on timestamp default current_timestamp,
	verifier_id int,
	verified_on datetime,
	status varchar(20),
	foreign key(user_id) references users(id),
	foreign key(verifier_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_status on identity_verification(status);

-- 为seek新增位置信息
alter table seek add column province varchar(50),
	add column city varchar(50),
	add column district varchar(50),
	add column address varchar(255),
	add index idx_province(province),
	add index idx_city(city),
	add index idx_district(district);
