package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberUtilTest {
	@Test
	public void testIsNumeric(){
		boolean result = NumberUtil.isNumeric("123");
		assertTrue(result);
	}
	
	@Test
	public void testIsNumeric1(){
		boolean result = NumberUtil.isNumeric("abc");
		assertFalse(result);
	}
	
	@Test
	public void testIsNumeric2(){
		boolean result = NumberUtil.isNumeric("123abc");
		assertFalse(result);
	}

}
