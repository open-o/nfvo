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
package org.openo.nfvo.monitor.umc.pm.datacollect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.common.PmOsfUtil;
import org.openo.nfvo.monitor.umc.pm.datacollect.bean.PmData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * History performance data processor pool.
 */
public class PmDataProcessorPool {

	private int maxThreadNum = 80;

	private int maxQueueSize = 300;

	private ThreadPoolExecutor executor;

	private BlockingQueue<Runnable> taskQueue;

	private final static String POOL_NAME = "PmDataProcessorPool-thread-";

	private static PmDataProcessorPool instance = new PmDataProcessorPool();

	private static final Logger LOGGER = LoggerFactory.getLogger(PmDataProcessorPool.class);

	public static PmDataProcessorPool getInstance() {
		return instance;
	}

	private PmDataProcessorPool() {
		init();
		taskQueue = new ArrayBlockingQueue<Runnable>(maxQueueSize, true);
		executor = new ThreadPoolExecutor(10, maxThreadNum, 1, TimeUnit.HOURS,
				taskQueue, new PmThreadFactory(),
				new PmRejectedExecutionHandler());
	}

	/**
	 * processor pool initialization. Read parameters from the configuration file.
	 */
	private void init()
	{
		Hashtable<String, String> htQueue = PmOsfUtil.getHmQueueInfo("PmDataTaskQueue");
		maxQueueSize = Integer.parseInt(htQueue.get("maxQueueSize"));
		int tmp_maxThreadNum = Integer.parseInt(htQueue.get("maxThreadNum"));
		maxThreadNum = tmp_maxThreadNum < 10 ? 10 : tmp_maxThreadNum;
	}
	
	public void addPmDataProcessorTask(String proxyIp, List<PmData> pmDataList) {
		PmDataSaver pmDataSaver = new PmDataSaver(proxyIp, pmDataList, System
				.currentTimeMillis());
		executor.execute(pmDataSaver);
	}

	private class PmDataSaver implements Runnable {
		private long enqueueTime;
		private long granularity;
		private List<PmData> pmDataList;
		private String proxyIp;

		public PmDataSaver(String proxyIp, List<PmData> pmDataList,
				long enqueueTime) {
			this.granularity = pmDataList.get(0).getGranularity() * 1000;
			this.enqueueTime = enqueueTime;
			this.pmDataList = pmDataList;
			this.proxyIp = proxyIp;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("TaskInfo:");
			sb.append("proxyIp=").append(proxyIp).append(";");
			sb.append("pmDataList=").append(pmDataList).append(";");
			return sb.toString();
		}

		@Override
		public void run() {
			long elapsed = System.currentTimeMillis() - enqueueTime;
			if (elapsed > granularity) {
			    LOGGER.warn("Task process timeout." + toString());
			}		
			process();
		}

		private void process() {
			String poid = null;

			try {
				poid = PmDataManager.getInstance().getPoidFromDataaqTask(proxyIp, pmDataList);
				if (poid == null) {
					return;
				}

			} catch (PmException e) {
			    LOGGER.warn("", e);
				return;
			} 
			// save data to database
			PmDataManager.getInstance().saveToDB(proxyIp, poid, pmDataList);
		}

	}

	private class PmRejectedExecutionHandler implements
			RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			BlockingQueue<Runnable> queue = executor.getQueue();
			PmDataSaver task = (PmDataSaver) queue.poll();
			PmData pmData = task.pmDataList.get(0);
			long cTime = pmData.getCollectTime();
			String collectTime = formatTime(cTime);
			LOGGER.warn("Task discard.CollectTime=" + collectTime
					+ ";ElapesdTime="
					+ (System.currentTimeMillis() - task.enqueueTime) + ";"
					+ task.toString());
			executor.execute(r);
		}

		private String formatTime(long time) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(new Date(time));
		}

		private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	}

	private class PmThreadFactory implements ThreadFactory {
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);

		public PmThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
					.getThreadGroup();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, POOL_NAME
					+ threadNumber.getAndIncrement());
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}

	}

}
