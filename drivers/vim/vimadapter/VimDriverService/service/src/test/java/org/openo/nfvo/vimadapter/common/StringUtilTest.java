/*
 * Copyright 2016, Huawei Technologies Co., Ltd.
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
