#
#      Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#JAVA_HOME="/home/conductortest/jdk1.7/jdk/linux"
echo @JAVA_HOME@ $JAVA_HOME
port=8789
JAVA="$JAVA_HOME/bin/java"
echo @JAVA@ $JAVA
JAVA_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$port,server=y,suspend=n"
echo @JAVA_OPTS@ $JAVA_OPTS
"$JAVA" $JAVA_OPTS  -jar "lib/console-service.jar" server "conf/console.yml"
