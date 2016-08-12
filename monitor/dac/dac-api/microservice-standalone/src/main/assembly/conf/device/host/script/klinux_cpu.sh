#
# Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
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

#!/bin/sh

count=1
idle=0
system=0
user=0
nice=0
wait=0
processcnt=0
userno=3
sysno=5
waitno=6
idle=8

  temp=`sar 1 1 | tail -3 | head -1`
  for list in $temp
  do
      if [ $list = "%user" ]
      then
      		userno=$count
      fi
      if [ $list = "%system" ]
      then
      		sysno=$count
      fi
      if [ $list = "%iowait" ]
      then
      		waitno=$count
      fi
      if [ $list = "%idle" ]
      then
      		idleno=$count
      fi
      if [ $list = "AM" ] || [ $list = "PM" ]
      then
      		continue
      fi
      count=`expr $count + 1`
  done
  temp=`sar 1 3 | tail -1`
  user=`echo $temp |awk '{printf("%s\n",$'$userno')}'`
  system=`echo $temp |awk '{printf("%s\n",$'$sysno')}'`
  wait=`echo $temp |awk '{printf("%s\n",$'$waitno')}'`
  idle=`echo $temp |awk '{printf("%s\n",$'$idleno')}'`

  temp=`ps -ef | wc -l`
  processcnt=`echo $temp`

  echo "TotalCPUUtilization    100"
  echo "CPUUtilization(user)   $user"
  echo "CPUUtilization(system) $system"
  echo "CPUUtilization(nice)   $nice"
  echo "CPUUtilization(idle)   $idle"
  echo "CPUUtilization(wait)   $wait"
  echo "ProcessCnt             $processcnt"
