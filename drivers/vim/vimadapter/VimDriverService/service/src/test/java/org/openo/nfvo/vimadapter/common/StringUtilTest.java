package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Test;

public class StringUtilTest {
	@Test
	public void testIsValidString(){
		boolean result = StringUtil.isValidString("abc");
		assertTrue(result);
	}
	
	@Test
	public void testIsValidString1(){
		boolean result = StringUtil.isValidString(" abc ");
		assertTrue(result);
	}
	
	@Test
	public void testIsValidString2(){
		boolean result = StringUtil.isValidString("  ");
		assertFalse(result);
	}
	
	@Test
	public void testIsValidString3(){
		boolean result = StringUtil.isValidString("");
		assertFalse(result);
	}
	
	@Test
	public void testIsValidString4(){
		boolean result = StringUtil.isValidString(null);
		assertFalse(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort() throws MalformedURLException{
		String source = "https://127.0.0.1:31943";
		String target = "https://127.0.0.1:31943";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertTrue(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort1() throws MalformedURLException{
		String source = "https://127.0.0.1";
		String target = "https://127.0.0.1:443";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertTrue(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort2() throws MalformedURLException{
		String source = "https://127.0.0.1:443";
		String target = "https://127.0.0.1";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertTrue(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort3() throws MalformedURLException{
		String source = "https://127.0.0.1:443";
		String target = "https://127.0.0.1:442";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertFalse(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort4() throws MalformedURLException{
		String source = "https://127.0.0.1";
		String target = "https://127.0.0.2";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertFalse(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort5() throws MalformedURLException{
		String source = "https://127.0.0.1";
		String target = "https://127.0.0.1:31943";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertFalse(result);
	}
	
	@Test
	public void testIsSameUrlIgnorePort6() throws MalformedURLException{
		String source = "https://127.0.0.1:31943";
		String target = "https://127.0.0.1";
		String sourceType = "url";
		String targetType = "url";
		boolean result = StringUtil.isSameUrlIgnorePort(source, target, sourceType, targetType);
		assertFalse(result);
	}

}
