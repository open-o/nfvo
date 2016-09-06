/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.dac.snmptrap.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.openo.orchestrator.nfv.dac.cometd.CometdService;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.openo.orchestrator.nfv.dac.snmptrap.util.TrapConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrapSendMsgQueue
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapSendMsgQueue.class);
    private int maxQueueSize;
    private static TrapSendMsgQueue instance = null;
    private ConcurrentLinkedQueue<TrapData> sendMsgQueue = new ConcurrentLinkedQueue<TrapData>();
    private static boolean threadflag = false;


    public TrapSendMsgQueue()
    {
    	maxQueueSize = TrapConfig.getTrapSendQueueSize();
    }


    public synchronized static TrapSendMsgQueue getInstance()
    {
        if (instance == null)
        {
            instance = new TrapSendMsgQueue();
        }
        return instance;
    }


	public void put(TrapData msg) {

		if (sendMsgQueue.size() < maxQueueSize) {
			sendMsgQueue.add(msg);
		}

		else {
			sendMsgQueue.remove();
			sendMsgQueue.add(msg);
			LOGGER.info("sendMsgQueue is full,Throw old TrapAlarmInfo,Put new TrapAlarmInfo");
		}

	}

    private class sendTrapAlarmThread implements Runnable
    {
		public void run() {

			while (threadflag) {
				try {
					if (sendMsgQueue != null && sendMsgQueue.size() != 0) {
						while (sendMsgQueue.size() > 0) {
							TrapData trapData = sendMsgQueue.remove();
//							LOGGER.info("send trapData--trapOid:" + trapData.getTrapOid()
//									+ " entityID:" + trapData.getEntity()
//									+ " alarmCode:" + trapData.getAlarmCode()
//									+ " eventType:" + trapData.getEventType()
//									+ " devIp:" + trapData.getIpAddress());

							//send trap data to umc using cometd
							CometdService.getInstance().publish(CometdService.SNMPTRAP_CHANNEL, trapData);
						}
					} else {
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					LOGGER.warn("Send TrapAlarmInfo to Server failed. " + e);
				}
			}
		}
	}

    public void startSendTrapAlarm()
    {
        threadflag = true;
        sendTrapAlarmThread sendTrapThread = new sendTrapAlarmThread();
        Thread t = new Thread(sendTrapThread);
        t.start();
    }

    public void endSendTrapAlarm()
    {
        threadflag = false;
    }

	public void clearSendMsgQueue() {

		while (!sendMsgQueue.isEmpty()) {
			sendMsgQueue.remove();
		}

	}
}
