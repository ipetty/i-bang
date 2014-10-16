#!/bin/sh
#
set	-e
source ./ilib
source ./config.conf
cur_dir=$(cd "$(dirname	"$0")";	pwd)



function usage()
{
   echo	"Usage:	$0 {init|all|tomcat|ant|maven|mysql|nginx|nmon|goAccess|androidSdk|iserver|iclient|revertIserver}"
   RETVAL="2"
}

RETVAL="0"
case "$1" in
	all)
		prompt tomcatAdminUser 'Tomcat Admin User' 'admin'
		prompt tomcatAdminPass 'Tomcat Admin Password' 'admin'
		linuxInit
		installTomcat
		installProbe
		installAnt
		installMaven
		installMysql
		installGoAccess
		installNginx
		installNmon
		#installGvm
		configNginx
		configTomcat $tomcatAdminUser $tomcatAdminPass
		configMysql
		service	tomcat restart
		service	nginx restart
		service	ntpd restart
		installIserver
		installIclient
		echo "all OK"
		;;
	init)
		linuxInit
		service	ntpd restart
		echo "init OK"
		;;
	tomcat)
		prompt tomcatAdminUser 'Tomcat Admin User' 'admin'
		prompt tomcatAdminPass 'Tomcat Admin Password' 'admin'
		installTomcat
		installProbe
		configTomcat $tomcatAdminUser $tomcatAdminPass
		service	tomcat restart
		echo "tomcat OK"
		;;
	ant)
		installAnt
		echo "ant OK"
		;;
	maven)
		installMaven
		echo "maven	OK"
		;;
	mysql)
		installMysql
		configMysql
		echo "mysql	OK"
		;;
	nginx)
		installNginx
		configNginx
		service	nginx restart
		echo "nginx	OK"
		;;
	nmon)
		installNmon
		echo "nmon OK"
		;;
	goAccess)
		installGoAccess
		echo "goAccess OK"
		;;
	androidSdk)
		installAndroidSDK
		echo "revert OK"
		;;
	iserver)
		installIserver
		echo "iserver OK"
		;;
	revertIserver)
		rollbackIServer
		echo "revertIserver	OK"
		;;
	iclient)
		installIclient
		echo "iclinet OK"
		;;
	*)
	  usage
	  ;;
esac

cd $cur_dir
exit $RETVAL