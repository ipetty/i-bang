-- 更新身份验证表结构
alter table identity_verification add column description text after id_card_in_hand,
	add column verify_info text after verifier_id;

-- 用户表增加是否通过身份验证字段
alter table users add column identity_verified boolean default false after salt;
