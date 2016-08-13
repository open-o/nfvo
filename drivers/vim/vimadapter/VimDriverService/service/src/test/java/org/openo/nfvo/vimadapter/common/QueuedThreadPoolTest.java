package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueuedThreadPoolTest {
	@Test
	public void testGetThreadPool(){
		assertNotNull(QueuedThreadPool.getThreadPool());
	}	
}
