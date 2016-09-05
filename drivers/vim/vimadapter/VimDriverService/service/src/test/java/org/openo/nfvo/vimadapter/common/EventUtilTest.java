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

import org.junit.Test;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.constant.UrlConstant;

import net.sf.json.JSONObject;

public class EventUtilTest {
	@Test
	public void testSentEvtByRest() {
		EventUtil.sentEvtByRest(String.format(UrlConstant.REST_EVENT_ADD, "id", "name"), Constant.ASYNCPOST,
				new JSONObject());
	}

	@Test
	public void testSentEvtByRest1() {
		EventUtil.sentEvtByRest(String.format(UrlConstant.REST_EVENT_ADD, "id", "name"), "abc", new JSONObject());
	}

	@Test
	public void testSentEvtByRest2() {
		EventUtil.sentEvtByRest(String.format(UrlConstant.REST_EVENT_ADD, "id", "name"), Constant.ASYNCPOST,
				new JSONObject());
	}
}
