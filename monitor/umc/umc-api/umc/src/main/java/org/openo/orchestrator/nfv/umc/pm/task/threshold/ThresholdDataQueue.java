package org.openo.orchestrator.nfv.umc.pm.task.threshold;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmOsfUtil;


/**
 * Performance Threshold Data Queue.
 */
public class ThresholdDataQueue extends Thread
{
	private static DebugPrn dMsg = new DebugPrn(ThresholdDataQueue.class.getName());

	private int maxQueueSize = 500;
	private int maxThreadNum = 10;
	private int runCollectPeriod = 1;
	private transient boolean isWorking = true;

	private ConcurrentLinkedQueue<Map> queue = new ConcurrentLinkedQueue<Map>();
	private ThreadPoolExecutor poolThreads = null;

	/**
	 * 
	 */
	public ThresholdDataQueue()
	{
		init();
		poolThreads = new ThreadPoolExecutor(10, maxThreadNum, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(
				maxQueueSize), new DefaultRejectedExecutionHandler());

	}

	private void init()
	{
		Hashtable<String, String> htQueue = PmOsfUtil.getHmQueueInfo("ThresholdDataQueue");
		maxQueueSize = Integer.parseInt(htQueue.get("maxQueueSize"));
		int tmp_maxThreadNum = Integer.parseInt((String) htQueue.get("maxThreadNum"));
		maxThreadNum = tmp_maxThreadNum < 10 ? 10 : tmp_maxThreadNum;
	}

	public void run()
	{
		while (isWorking)
		{
			if (queue.size() == 0)
			{
				try
				{
					Thread.sleep(runCollectPeriod * 100);
				}
				catch (InterruptedException e)
				{
					dMsg.warn("Thread Interrupted!", e);
				}
			} else
			{
				dispatchDataRptMsg();
			}
		}
	}

	/**
	 * Processing queue data.
	 */
	private void dispatchDataRptMsg()
	{
		while (queue.size() > 0)
		{
			Map attrList = (Map) queue.remove();
			dMsg.debug("PM DATA queue size:" + poolThreads.getQueue().size());
			poolThreads.execute(new ThresholdHandleThread(attrList));
		}
	}

	/**
	 * put data to the queue.
	 */
	public void put(Map attrList)
	{
		queue.add(attrList);
	}

	public void close()
	{
		isWorking = false;
		poolThreads.shutdownNow();
	}

	public boolean isRunning()
	{
		return isWorking;
	}
	private class DefaultRejectedExecutionHandler implements
	RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			dMsg.info("PM DATA queue full!" + executor.getQueue().size());
            if (!executor.isShutdown()) {
            	executor.getQueue().poll();
            	executor.execute(r);
            }
		}
	}
}
