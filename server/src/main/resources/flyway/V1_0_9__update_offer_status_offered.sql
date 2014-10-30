-- 将offer的 应征中 状态变更为 等待接受帮助
update offer set status='等待接受帮助' where status='应征中';
