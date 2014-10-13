insert into users(id, username, email, password, salt, nickname)
values(1, 'ibang', 'ibang@ipetty.net', '9bd68c251be901430d6b3bd111ac8cba13b008ce', '7a2a9b7682d478ef', '爱帮')
on duplicate key update username='ibang', email='ibang@ipetty.net',
password='9bd68c251be901430d6b3bd111ac8cba13b008ce', salt='7a2a9b7682d478ef', nickname='爱帮';
