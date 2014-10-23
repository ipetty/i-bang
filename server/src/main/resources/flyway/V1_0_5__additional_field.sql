-- 添加服务时间字段
alter table seek add column service_date varchar(255);
alter table seek add column title varchar(255);
create index idx_title on seek(title);
