-- 举报
create table report (
	id bigint primary key auto_increment,
	sn varchar(50),
	type varchar(50),
	seek_id bigint,
	seek_type varchar(50),
	offer_id bigint,
	user_id int default null,
	behave varchar(50),
	content text,
	reporter_id int not null,
	created_on timestamp default current_timestamp,
	result varchar(50),
	dealed_on datetime,
	foreign key(seek_id) references seek(id),
	foreign key(offer_id) references offer(id),
	foreign key(user_id) references users(id),
	foreign key(reporter_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_type on report(type);
create index idx_behave on report(behave);

-- 为seek增加enable字段
alter table seek add column enable boolean default true,
	add column disabled_on datetime,
	add index idx_enable(enable);

-- 为offer增加enable字段
alter table offer add column enable boolean default true,
	add column disabled_on datetime,
	add index idx_enable(enable);

-- 为users增加enable字段
alter table users add column enable boolean default true,
	add column disabled_on datetime,
	add index idx_enable(enable);
