/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.snmptrap.common;

/**
 * @author wangjiangping
 * @date 2016/3/2 9:15:59
 *
 */
public class SnmpTrapQueue {
    private static TrapMsgQueue trapMsgQueue = null;

    public static void setTrapMsgQueue(TrapMsgQueue trapMsgQueue) {
        SnmpTrapQueue.trapMsgQueue = trapMsgQueue;
    }

    public static void putTrapMsgData(TrapMsgData msg)
    {
        if (trapMsgQueue == null)
        {
            TrapMsgQueue trapMsgQueue = new TrapMsgQueue();
            setTrapMsgQueue(trapMsgQueue);
            trapMsgQueue.start();
        }
        trapMsgQueue.put(msg);
    }
}
