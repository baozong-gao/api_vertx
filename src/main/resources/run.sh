#!/bin/sh 
# 
#该脚本为Linux下启动java程序的通用脚本。即可以作为开机自启动service脚本被调用， 
#也可以作为启动java程序的独立脚本来使用。 
# 
#Author: gbz, Date: 2017/1/11
# 
#警告!!!：该脚本stop部分使用系统kill命令来强制终止指定的java程序进程。 
#在杀死进程前，未作任何条件检查。在某些情况下，如程序正在进行文件或数据库写操作， 
#可能会造成数据丢失或数据不完整。如果必须要考虑到这类情况，则需要改写此脚本， 
#增加在执行kill命令前的一系列检查。 
#   
################################### 
# 以下这些注释设置可以被chkconfig命令读取   
# chkconfig: - 99 50   
# description: Java程序启动脚本   
# processname: test   
# config: 如果需要的话，可以配置   
###################################   
# 
################################### 
#环境变量及程序执行参数 
#需要根据实际环境以及Java程序名称来修改这些参数 
################################### 
#JDK所在路径 
JAVA_HOME="/usr/local/java" 

#执行程序启动所使用的系统用户，考虑到安全，推荐不使用root帐号 
RUNNING_USER=sxapi

#Java程序所在的目录
APP_HOME=/home/sxapi/save

#需要启动的Java主程序
APP_MAINCLASS=sxapi.jar


#java虚拟机启动参数 
JAVA_OPTS="-server -ms2048m -mx2048m -Xmn512m -Djava.awt.headless=true -Djava.rmi.server.hostname=172.16.1.35 -Dcom.sun.management.jmxremote.port=18999  -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

###################################
#(函数)判断程序是否已启动
#
#说明：
#使用JDK自带的JPS命令及grep命令组合，准确查找pid
#jps 加 l 参数，表示显示java的完整包路径
#使用awk，分割出pid ($1部分)，及Java程序名称($2部分)
###################################
#初始化psid变量（全局）
psid=0

checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}

###################################
#(函数)检查程序进程个数
#
#说明：
#使用JDK自带的JPS命令及grep命令组合，准确查找pid
#jps 加 l 参数，表示显示java的完整包路径
#使用awk，分割出pid ($1部分)，及Java程序名称($2部分)
###################################

checkpidsize() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS | wc -l`

   if [ -n "$javaps" ]; then
      echo $javaps
   else
      echo 0
   fi
}
returnpidsize() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS | wc -l`

   if [ -n "$javaps" ]; then
      return $javaps
   else
      return 0
   fi
}
###################################
#(函数)启动程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示程序已启动
#3. 如果程序没有被启动，则执行启动命令行
#4. 启动命令执行后，再次调用checkpid函数
#5. 如果步骤4的结果能够确认程序的pid,则打印[OK]，否则打印[Failed]
#注意：echo -n 表示打印字符后，不换行
#注意: "nohup 某命令 >/dev/null 2>&1 &" 的用法
###################################
start() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo "================================"
      echo "warn: $APP_MAINCLASS already started! (pid=$psid)"
      echo "================================"
      return 0
   else
      echo -n "Starting $APP_MAINCLASS ..."
      JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_HOME/$APP_MAINCLASS >/dev/null 2>&1 &"
      sudo su - $RUNNING_USER -c "$JAVA_CMD"
      checkpid
      if [ $psid -ne 0 ]; then
         echo "(pid=$psid) [OK]"
         return 0
      else
         echo "[Failed]"
         return 1
      fi
   fi
}

###################################
#(函数)停止程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则开始执行停止，否则，提示程序未运行
#3. 使用kill -9 pid命令进行强制杀死进程
#4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
#5. 如果步骤4的结果$?等于0,则打印[OK]，否则打印[Failed]
#6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死的处理（递归调用stop）。
#注意：echo -n 表示打印字符后，不换行
#注意: 在shell编程中，"$?" 表示上一句命令或者一个函数的返回值
###################################
stop() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_MAINCLASS ...(pid=$psid) "
      sudo su - $RUNNING_USER -c "kill -9 $psid"
      if [ $? -eq 0 ]; then
         echo "[OK]"
         return 0
      else
         echo "[Failed]"
         return 1
      fi

      checkpid
      if [ $psid -ne 0 ]; then
         stop
      fi
   else
      echo "================================"
      echo "warn: $APP_MAINCLASS is not running"
      echo "================================"
      return 0
   fi
}

###################################
#(函数)检查程序运行状态
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示正在运行并表示出pid
#3. 否则，提示程序未运行
###################################
status() {
   checkpid

   if [ $psid -ne 0 ];  then
      echo "$APP_MAINCLASS is running! (pid=$psid)"
   else
      echo "$APP_MAINCLASS is not running"
   fi
   return 0
}

###################################
#(函数)打印系统环境参数
###################################
info() {
   echo "System Information:"
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_MAINCLASS=$APP_MAINCLASS"
   echo "****************************"
}

###################################
#读取脚本的第一个参数($1)，进行判断
#参数取值范围：{start|stop|restart|status|info}
#如参数不在指定范围之内，则打印帮助信息
###################################
case "$1" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'checkprocsize')
     checkpidsize
     ;;
   'returnpidsize')
     returnpidsize
     ;;
   'info')
     info
     ;;
  *)
     echo "Usage: $0 {start|stop|restart|status|info|checkprocsize|returnpidsize}"
     exit 1 
esac