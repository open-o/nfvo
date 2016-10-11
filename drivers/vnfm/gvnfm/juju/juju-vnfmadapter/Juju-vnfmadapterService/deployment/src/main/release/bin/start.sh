#*
# Copyright 2016 Huawei Technologies Co., Ltd.
#  
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#  
#     http://www.apache.org/licenses/LICENSE-2.0
#  
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#*
CUR_PATH=$(cd `dirname $0`;pwd)
SCRIPT_PATH=$0
IPMC_USER="`stat -c '%U' ${SCRIPT_PATH}`"
export IPMC_USER
CURRENT_USER="`/usr/bin/id -u -n`"

if [ "${IPMC_USER}" != "${CURRENT_USER}" ]
then
    echo "only ${IPMC_USER} can execute this script."
    exit 1
fi

umask 027

if [ -z "$JAVA_HOME" ]
then
    echo "There is no JAVA_HOME"
    exit 1
fi

if [ -z "$CATALINA_HOME" ]
then
    echo "There is no CATALINA_HOME"
    exit 1
fi

if [ -z "$APP_ROOT" ]
then
    echo "There is no APP_ROOT"
    exit 1
fi

JAVA_OPTS="$JAVA_OPTS -Dbsp.app.datasource=vnfmdb"

export CATALINA_BASE=$APP_ROOT
export COMPLETE_PROCESS_NAME=$PROCESS_NAME-$NODE_ID-$PROCESS_SLOT
LOG_PATH=$_APP_LOG_DIR/$COMPLETE_PROCESS_NAME

JAVA_OPTS="-Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -Dlog.dir=$LOG_PATH"
JAVA_OPTS="$JAVA_OPTS -DAPP_ROOT=$APP_ROOT"
export TOMCAT_LOG_DIR=$_APP_LOG_DIR/$COMPLETE_PROCESS_NAME/tomcatlog
mkdir -p $TOMCAT_LOG_DIR
export TOMCAT_WORK_DIR=$_APP_SHARE_DIR/$COMPLETE_PROCESS_NAME/tomcatwork
export CATALINA_OUT=$TOMCAT_LOG_DIR/catalina.out
JAVA_OPTS="$JAVA_OPTS -DNFW=$COMPLETE_PROCESS_NAME  -DTOMCAT_LOG_DIR=$TOMCAT_LOG_DIR  -DTOMCAT_WORK_DIR=$TOMCAT_WORK_DIR -DNFW=$COMPLETE_PROCESS_NAME -Dprocname=$COMPLETE_PROCESS_NAME "
export JAVA_OPTS="$JAVA_OPTS -server -Xms32m -Xmx256m -XX:InitialCodeCacheSize=32m -XX:ReservedCodeCacheSize=64m -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=128m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=62 -XX:-UseLargePages -XX:+UseFastAccessorMethods -XX:+CMSClassUnloadingEnabled"

$CATALINA_HOME/bin/catalina.sh start

result=0;$CUR_PATH/../../../../manager/agent/tools/shscript/syslogutils.sh "$(basename $0)" "$result" "Execute($#):$CUR_PATH/$0 $@";exit $result
