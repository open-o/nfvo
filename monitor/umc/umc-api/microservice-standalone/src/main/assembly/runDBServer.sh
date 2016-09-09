#
# Copyright 2016, CMCC Technologies Co., Ltd.
 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
 
#      http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#JAVA_HOME="/home/conductortest/jdk1.7/jdk/linux"

DIRNAME=`dirname $0`
RUNHOME=`cd $DIRNAME/; pwd`
echo @RUNHOME@ $RUNHOME

JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")
echo @JAVA_HOME@ $JAVA_HOME
JAVA="$JAVA_HOME/bin/java"
echo @JAVA@ $JAVA

class_path="$RUNHOME/:$RUNHOME/umc.jar"
echo @class_path@ $class_path

"$JAVA" -classpath "$class_path" org.h2.tools.Console -tcp -tcpAllowOthers -tcpPort 8405 -web -webAllowOthers -webPort 8505
