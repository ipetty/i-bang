-- 更新seeker_info数据
replace into seeker_info(user_id, title) select id, '大好人' from users;
update seeker_info si, (select seeker_id, count(id) as seek_count from seek group by seeker_id) as s set si.seek_count=s.seek_count where s.seeker_id=si.user_id;
update seeker_info si, (select evaluate_target_id, sum(point) as total_point from evaluation where type='offerer_to_seeker' group by evaluate_target_id) as e set si.total_point=e.total_point where e.evaluate_target_id=si.user_id;

-- 更新offerer_info数据
replace into offerer_info(user_id, title) select id, '大好人' from users;
update offerer_info oi, (select offerer_id, count(id) as offer_count from offer group by offerer_id) as o set oi.offer_count=o.offer_count where o.offerer_id=oi.user_id;
update offerer_info oi, (select evaluate_target_id, sum(point) as total_point from evaluation where type='seeker_to_offerer' group by evaluate_target_id) as e set oi.total_point=e.total_point where e.evaluate_target_id=oi.user_id;

-- select * from seeker_info;
-- select * from offerer_info;
