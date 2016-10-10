#!/bin/bash
#
# Copyright 2016 ZTE Corporation.
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
#
#     Author: Zhang Zhou
#     email: zhang.zhou1@zte.com.cn
#

DIRNAME=`dirname $0`
HOME=`cd $DIRNAME/; pwd`
SFC_Main_Class="sfc-service.jar"

echo ================== sfc      info  =============================================
echo HOME=$HOME
echo SFC_Main_Class=$SFC_Main_Class
echo ===============================================================================
cd $HOME; pwd

echo @WORK_DIR@ $HOME

function save_sfc_pid(){
	sfc_id=`ps -ef | grep $SFC_Main_Class | grep $HOME | grep -v grep | awk '{print $2}'`
	echo $sfc_id
}

function kill_apiroute_process(){
	ps -p $sfc_id
	if [ $? == 0 ]; then
		kill -9 $sfc_id
	fi
}

save_sfc_pid;
echo @C_CMD@ kill -9 $sfc_id
kill_apiroute_process;