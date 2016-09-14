/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.resmanagement.service.rest;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.resmanagement.common.util.request.RequestUtil;
import org.openo.nfvo.resmanagement.service.base.fs.impl.SitesImpl;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 2016年8月16日
 */
public class SitesRoaTest {

	private SitesRoa sitesRoa;

	@Before
	public void setUp() {
		sitesRoa = new SitesRoa();
		sitesRoa.setSites(new SitesImpl());
	}

	@Test
	public void testAddSites() throws ServiceException {
		new MockUp<RequestUtil>() {

			@Mock
			public JSONObject getAllJsonRequestBody(HttpServletRequest context) {
				return new JSONObject();
			}
		};
		new MockUp<SitesImpl>() {

			@Mock
			public int add(JSONObject jsonObject) throws ServiceException {
				return 1;
			}
		};
		JSONObject result = sitesRoa.addSites(null);
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("msg", "org.openo.nfvo.resmanage.common.add.success");
		assertEquals(expectedResult.toString(), result.toString());
	}

	@Test
	public void testAddSitesExceptions() throws ServiceException {
		new MockUp<RequestUtil>() {

			@Mock
			public JSONObject getAllJsonRequestBody(HttpServletRequest context) {
				return new JSONObject();
			}
		};
		new MockUp<SitesImpl>() {

			@Mock
			public int add(JSONObject jsonObject) throws ServiceException {
				throw new ServiceException();
			}
		};
		JSONObject result = sitesRoa.addSites(null);
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("msg", "");
		assertEquals(expectedResult.toString(), result.toString());
	}

	@Test
	public void testDeleteSites() throws ServiceException {
		new MockUp<RequestUtil>() {

			@Mock
			public JSONObject getAllJsonRequestBody(HttpServletRequest context) {
				return new JSONObject();
			}
		};
		new MockUp<SitesImpl>() {

			@Mock
			public int delete(String id) throws ServiceException {
				return 1;
			}
		};
		JSONObject result = sitesRoa.deleteSites(null, "123");
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("msg", "org.openo.nfvo.resmanage.common.del.success");
		assertEquals(expectedResult.toString(), result.toString());
	}

	@Test
	public void testDeleteSitesExceptions() throws ServiceException {
		new MockUp<RequestUtil>() {

			@Mock
			public JSONObject getAllJsonRequestBody(HttpServletRequest context) {
				return new JSONObject();
			}
		};
		new MockUp<SitesImpl>() {

			@Mock
			public int delete(String id) throws ServiceException {
				throw new ServiceException();
			}
		};
		JSONObject result = sitesRoa.deleteSites(null, "123");
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("msg", "");
		assertEquals(expectedResult.toString(), result.toString());
	}

	@Test
	public void testUpdateSites() throws ServiceException {
		new MockUp<RequestUtil>() {

			@Mock
			public JSONObject getAllJsonRequestBody(HttpServletRequest context) {
				return new JSONObject();
			}
		};
		new MockUp<SitesImpl>() {

			@Mock
			public int update(JSONObject jsonObject) throws ServiceException {
				return 1;
			}
		};
		JSONObject result = sitesRoa.updateSites(null);
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("msg", "org.openo.nfvo.resmanage.common.update.success");
		assertEquals(expectedResult.toString(), result.toString());
	}

	@Test
	public void testUpdateISitesExceptions() throws ServiceException {
		new MockUp<RequestUtil>() {

			@Mock
			public JSONObject getAllJsonRequestBody(HttpServletRequest context) {
				return new JSONObject();
			}
		};
		new MockUp<SitesImpl>() {

			@Mock
			public int update(JSONObject jsonObject) throws ServiceException {
				throw new ServiceException();
			}
		};
		JSONObject result = sitesRoa.updateSites(null);
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("msg", "");
		assertEquals(expectedResult.toString(), result.toString());
	}
}
