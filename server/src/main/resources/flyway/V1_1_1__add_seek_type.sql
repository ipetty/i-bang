-- 为seek新增类型字段
alter table seek add column type varchar(50),
	add index idx_type(type);
