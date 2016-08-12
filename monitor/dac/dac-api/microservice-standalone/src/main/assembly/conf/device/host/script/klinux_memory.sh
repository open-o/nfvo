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

sml_mem=0
lg_mem=0
ovsz_alloc=0
sys_mem=0
phys_mem=0
sysmemratio=0
total=0
used=0

temp=`vmstat 1 5 | tail -1`
memqueue=`echo $temp | awk '{printf("%s\n",$2)}'`
mempageinratio=`echo $temp | awk '{printf("%s\n",$7)}'`
mempageoutratio=`echo $temp | awk '{printf("%s\n",$8)}'`

temp=`free | head -2 | tail -1`
phys_mem=`echo $temp | awk '{printf("%s\n",$2)}'`
free_mem=`echo $temp | awk '{printf("%s\n",$4)}'`
used_mem=`echo $temp | awk '{printf("%s\n",$3)}'`

temp=`free | head -4 | tail -1`
usedswap=`echo $temp | awk '{printf("%s\n",$3)}'`
totalswap=`echo $temp | awk '{printf("%s\n",$2)}'`
freeswap=`echo $temp | awk '{printf("%s\n",$4)}'`

temp=`free | head -3 | tail -1`
freebufferscache=`echo $temp | awk '{printf("%s\n",$4)}'`

if [ "$totalswap" = "0" ]
then
    usedswapratio=0.00
else
	usedswapratio=$(echo "scale=4; $usedswap / $totalswap * 100" | bc)
	usedswapratio=`echo $usedswapratio  | awk '{printf("%f", $1)}'`
fi

total=`expr $phys_mem + $totalswap`
used=`expr $total - $freebufferscache - $freeswap`

if [ "$total" = "0" ]
then
    usedmemratio=0.00
else
	usedmemratio=$(echo "scale=4; $used / $total * 100" | bc)
	usedmemratio=`echo $usedmemratio | awk '{printf("%f", $1)}'`
fi

  temp=`cat /proc/vmstat | grep pgpgin`
  swapin=`echo $temp | awk '{printf("%s\n",$2)}'`
  temp=`cat /proc/vmstat | grep pgpgout`
  swapout=`echo $temp | awk '{printf("%s\n",$2)}'`
  swaprequesttotal=`expr $swapin + $swapout`

echo "used_mem        $used_mem"
echo "phys_mem        $phys_mem"
echo "usedmemratio    $usedmemratio"
echo "sysmemratio     0"
echo "usrmemratio     0"
echo "mempagerequest  $swaprequesttotal"
echo "memqueue        $memqueue"
echo "mempageinratio  $mempageinratio"
echo "mempageoutratio $mempageoutratio"
echo "usedswap        $usedswap"
echo "totalswap       $totalswap"
echo "swapusedratio   $usedswapratio"
