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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.nfvo.vimadapter.service.entity.Vim;

import net.sf.json.JSONObject;

public class ValidateVimTest {
	@Test
	public void testValidLen() {
		String url = "http";
		assertTrue(ValidateVim.validLen(url));
	}

	@Test
	public void testValidLen1() {
		String url = "http://127.0.0.1";
		assertFalse(ValidateVim.validLen(url));
	}

	@Test
	public void testValidLen2() {
		String url = "{\"id\": \"123456\",\"name\": \"GWT\",\"type\": \"openstack\",\"version\": \"1.0\",\"userName\": \"admin\",\"pwd\": \"huawei\",\"url\": \"http://192.168.3.35:5000\",\"extraInfo\": {\"domain\": \"admin\",\"authenticMode\": \"Anonymous\"}}\"id\": \"123456\",\"name\": \"GWT\",\"type\": \"openstack\",\"version\": \"1.0\",\"userName\": \"admin\",\"pwd\": \"huawei\",\"url\": \"http://192.168.3.35:5000\",\"extraInfo\": {\"domain\": \"admin\",\"authenticMode\": \"Anonymous\"}}";
		assertTrue(ValidateVim.validLen(url));
	}

	@Test
	public void testValidSuccessStatus() {
		assertTrue(ValidateVim.validSuccessStatus(200));
		assertTrue(ValidateVim.validSuccessStatus(201));
		assertFalse(ValidateVim.validSuccessStatus(400));
	}

	@Test
	public void testCheckUrl() {
		String data = "{\"id\": \"123456\",\"name\": \"GWT\",\"type\": \"openstack\",\"version\": \"1.0\",\"userName\": \"admin\",\"pwd\": \"huawei\",\"url\": \"http://192.168.3.35:5000\",\"extraInfo\": {\"domain\": \"admin\",\"authenticMode\": \"Anonymous\"}}";
		JSONObject vimJson = JSONObject.fromObject(data);
		Vim vim = new Vim("111", vimJson, "ACTIVE");
	}

}
