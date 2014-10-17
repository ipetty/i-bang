#!/bin/sh
#
set	-e
source ./config.conf
source ./ilib
cur_dir=$(cd "$(dirname	"$0")";	pwd)
function usage(){
   echo "usage: $0 {all|init|tomcat|ant|maven|mysql|nginx|nmon|goAccess|iserver|irevert|adt|iclient|setSwap|dataSync|webmin|aliyunMon}"
   RETVAL="2"
}

RETVAL="0"
case "$1" in
	all)
		linuxIntiPrompt
		tomcatConfigPrompt
		iclinetConfigPrompt
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
		installAndroidSDK
		installIclient
		echo "all OK"
		;;
	init)
		linuxIntiPrompt
		linuxInit
		service	ntpd restart
		echo "init OK"
		;;
	tomcat)
		tomcatConfigPrompt
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
	iserver)
		installIserver
		echo "iserver OK"
		;;
	irevert)
		rollbackIServer
		echo "revertIserver OK"
		;;
	adt)
		installAndroidSDK
		echo "adt OK"
		;;
	iclient)
		iclinetConfigPrompt
		installIclient
		echo "iclinet OK"
		;;
	setSwap)
		setSwap
		echo "setSwap OK"
		;;
	dataSync)
		dataSync
		echo "dataSync OK"
		;;
	webmin)
		webmin
		echo "dataSync OK"
		;;
		
	aliyunMon)
		aliyunMon
		echo "aliyunMon OK"
		;;
	*)
	  usage
	  ;;
esac

cd $cur_dir
exit $RETVAL