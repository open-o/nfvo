package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeUtilTest {
	@Test
	public void testGetHisIsoTime() {
		String result = TimeUtil.getHisIsoTime(100L, 10L);
		assertEquals("1970-01-01T08:00:00", result);
	}

	@Test
	public void testGetIsoTime() {
		String result = TimeUtil.getIsoTime(1000L);
		assertEquals("1970-01-01T08:00:01", result);
	}

	@Test
	public void testGetTime() {
		long result = TimeUtil.getTime();
		assertNotNull(result);
	}

	@Test
	public void testGetIntTime() {
		long result = TimeUtil.getIntTime("1970-01-01T08:00:00");
		assertEquals(0L, result);
	}

	@Test
	public void testGetIntTimeByParseException() {
		long result = TimeUtil.getIntTime("1970-01-01TA08:00:00");
		assertEquals(0L, result);
	}

}
