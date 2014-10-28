-- 确保通知消息的发送者不为空
update system_message s set s.from_user_id=(select seeker_id from seek where id=s.seek_id) where s.from_user_id is null;
