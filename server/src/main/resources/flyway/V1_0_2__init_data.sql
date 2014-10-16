insert into users(id, username, email, password, salt, nickname)
values(2, 'kc', 'kc@ipetty.net', '9bd68c251be901430d6b3bd111ac8cba13b008ce', '7a2a9b7682d478ef', 'kc')
on duplicate key update username='kc', email='kc@ipetty.net',
password='9bd68c251be901430d6b3bd111ac8cba13b008ce', salt='7a2a9b7682d478ef', nickname='kc';

insert into users(id, username, email, password, salt, nickname)
values(3, 'lcf', 'lcf@ipetty.net', '9bd68c251be901430d6b3bd111ac8cba13b008ce', '7a2a9b7682d478ef', 'lcf')
on duplicate key update username='lcf', email='lcf@ipetty.net',
password='9bd68c251be901430d6b3bd111ac8cba13b008ce', salt='7a2a9b7682d478ef', nickname='lcf';

insert into users(id, username, email, password, salt, nickname)
values(4, 'zzl', 'zzl@ipetty.net', '9bd68c251be901430d6b3bd111ac8cba13b008ce', '7a2a9b7682d478ef', 'zzl')
on duplicate key update username='zzl', email='zzl@ipetty.net',
password='9bd68c251be901430d6b3bd111ac8cba13b008ce', salt='7a2a9b7682d478ef', nickname='zzl';

insert into users(id, username, email, password, salt, nickname)
values(5, 'xjh', 'xjh@ipetty.net', '9bd68c251be901430d6b3bd111ac8cba13b008ce', '7a2a9b7682d478ef', 'xjh')
on duplicate key update username='xjh', email='xjh@ipetty.net',
password='9bd68c251be901430d6b3bd111ac8cba13b008ce', salt='7a2a9b7682d478ef', nickname='xjh';

update users set phone='13800138000';
