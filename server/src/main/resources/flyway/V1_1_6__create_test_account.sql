insert into users(id, username, email, password, salt, nickname)
values(10, 'test', 'test@ipetty.net', '9bd68c251be901430d6b3bd111ac8cba13b008ce', '7a2a9b7682d478ef', '测试帐号')
on duplicate key update username='test', email='test@ipetty.net',
password='9bd68c251be901430d6b3bd111ac8cba13b008ce', salt='7a2a9b7682d478ef', nickname='测试帐号';
