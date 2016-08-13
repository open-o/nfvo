package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResourceUtilTest {
	
	@Test
	public void getMessage(){
		String result = ResourceUtil.getMessage("123");
		String expectedResult = "123";
		assertEquals(expectedResult,result);
	}

}
