-- 为评价增加图片
alter table image add column evaluation_id bigint after seek_id,
	add constraint fk_seek_id foreign key(seek_id) references seek(id),
	add constraint fk_evaluation_id foreign key(evaluation_id) references evaluation(id);
