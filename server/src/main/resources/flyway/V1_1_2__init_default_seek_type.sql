-- 为seek的类型字段设置默认值
update seek set type='求助' where type is null;
