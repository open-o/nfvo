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

rowcount=0
rowno=0
count=0

rowcount=`cat /proc/net/dev | wc -l`
rowcount=`expr $rowcount - 2`
while [ $rowno -lt $rowcount ] ;
do
        count=`expr $rowcount - $rowno`
        tmp1=`cat /proc/net/dev | tail -$count | head -1`
        nic=${tmp1%:*}
        line=${tmp1#*:}
        if [ $nic = "lo" ]
        then
            rowno=`expr $rowno + 1`
            continue
        else
            tmp2=`sudo /usr/sbin/ethtool $nic | grep Speed`
            speed=`echo $tmp2 | awk '{printf("%s\n",$2+0)}'`
            echo "$nic $line $speed"
        fi
        rowno=`expr $rowno + 1`
done