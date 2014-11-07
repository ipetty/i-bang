-- 站内信
create table letter (
	id bigint primary key auto_increment,
	owner_id int,
	cooperator_id int not null,
	content text,
	is_inbox boolean,
	is_read boolean default false,
	created_on timestamp default current_timestamp,
	read_on datetime,
	foreign key(owner_id) references users(id),
	foreign key(cooperator_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_read on letter(is_read);
create index idx_created_on on letter(created_on);
