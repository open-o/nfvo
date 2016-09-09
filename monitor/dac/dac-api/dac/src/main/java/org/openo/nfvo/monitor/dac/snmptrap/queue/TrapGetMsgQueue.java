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
package org.openo.nfvo.monitor.dac.snmptrap.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.openo.nfvo.monitor.dac.snmptrap.TrapFilter;
import org.openo.nfvo.monitor.dac.snmptrap.util.TrapConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponderEvent;


public class TrapGetMsgQueue
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapGetMsgQueue.class);
    private int maxQueueSize;
    private static TrapGetMsgQueue instance = null;
    private ConcurrentLinkedQueue<CommandResponderEvent> getMsgQueue = new ConcurrentLinkedQueue<CommandResponderEvent>();
    private static boolean threadflag = false;


    public TrapGetMsgQueue()
    {
    	maxQueueSize = TrapConfig.getTrapReceiveQueueSize();
    }


    public synchronized static TrapGetMsgQueue getInstance()
    {
        if (instance == null)
        {
            instance = new TrapGetMsgQueue();
        }
        return instance;
    }


    public void put(CommandResponderEvent msg)
    {

		if (getMsgQueue.size() < maxQueueSize) {
			getMsgQueue.add(msg);
		} else {
			getMsgQueue.remove();
			getMsgQueue.add(msg);
			LOGGER.info("getMsgQueue is full,Throw old TrapEvent,add new TrapEvent");
		}

	}


    private class getTrapEventThread implements Runnable
    {
		public void run() {

			while (threadflag) {
				try {
					if (getMsgQueue != null && getMsgQueue.size() != 0) {
						while (getMsgQueue.size() > 0) {
							try {
								CommandResponderEvent trapEvent = getMsgQueue
										.remove();
								TrapFilter.applyTrapFilter(trapEvent);
							} catch (NullPointerException e2) {
							}
						}
					} else {
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					LOGGER.warn("Send TrapEvent to  TrapFilter failed. " + e);
				}
			}
			LOGGER.info("End getTrapEventThread  ");
		}
	}


    public void startGetTrapEvent()
    {
        threadflag = true;
        getTrapEventThread getTrapThread = new getTrapEventThread();
        Thread t = new Thread(getTrapThread);
        t.start();
    }


    public void endGetTrapEvent()
    {
        threadflag = false;
    }


	public void clearGetMsgQueue() {

		while (!getMsgQueue.isEmpty()) {
			getMsgQueue.remove();
		}

	}
}
