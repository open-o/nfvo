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
package org.openo.nfvo.monitor.umc.adpt.roc;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.cometd.bayeux.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @date 2016/3/2 9:19:16
 * 
 */
public class RocMsgQueue extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocMsgQueue.class);

    private int maxQueueSize = 500;
    private int maxThreadNum = 1;
    private int processPeriod = 1;

    private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
    private ThreadPoolExecutor poolThreads = null;

    public RocMsgQueue() {
        poolThreads = new ThreadPoolExecutor(1, maxThreadNum, 2, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxQueueSize),
                new MyThreadFactory("RocMsgQueue"), new DefaultRejectedExecutionHandler());
    }


    public void run() {
        while (true) {
            if (queue.size() == 0) {
                try {
                    Thread.sleep(processPeriod * 100);
                } catch (InterruptedException e) {
                    LOGGER.warn("Thread Interrupted!", e);
                }
            } else {
                dispatchDataRptMsg();
            }
        }
    }

    private void dispatchDataRptMsg() {
        while (queue.size() > 0) {
        	Message msgData = queue.remove();
            LOGGER.debug("Roc msg queue size:" + poolThreads.getQueue().size());
            poolThreads.execute(new RocMsgProcessThread(msgData));
        }
    }

    public void put(Message msg) {
        queue.add(msg);
    }
    
    private class MyThreadFactory implements ThreadFactory {
        private int counter;
        private String name;

        public MyThreadFactory(String name) {
            counter = 0;
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, name + "-Thread-" + counter);
            counter++;
            LOGGER.info(String.format("Created thread %d with name %s on %s\n", t.getId(),
                    t.getName(), new Date()));
            return t;
        }
    }

    private class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            LOGGER.info("RocMsgQueue is full!" + executor.getQueue().size());
            if (!executor.isShutdown()) {
                executor.getQueue().poll();
                executor.execute(r);
            }
        }
    }
}
