-----------------使用方法----------------------
初次安装	./install.sh all
发布服务器	./install.sh iserver
发布客户端	./install.sh iclient
服务器回滚	./install.sh irevert
其它命令	./install.sh help
-----------------脚本规范----------------------
规范名		描述
全局变量	全部大写
函数内变量	以"_"开头
其它		遵循驼峰
-----------------常用目录----------------------
Tomcat配置目录:/etc/tomcat
Tomcat安装目录:/usr/share/tomcat/
Nginx配置目录:/etc/nginx
Nginx日志文件:/var/log/nginx
Nginx程序文件:/usr/sbin/nginx
Nginx数据目录:/usr/share/nginx
Nginx缓存目录:/var/cache/nginx
Mysql配置文件:/etc/my.cnf
Mysql错误日志:/var/log/mysqld.log
Mysql数据目录:/var/lib/mysql
Mysql程序文件:/usr/sbin/mysqld
Mysql程序目录:/usr/share/mysql
IServer上传目录:/home/files
