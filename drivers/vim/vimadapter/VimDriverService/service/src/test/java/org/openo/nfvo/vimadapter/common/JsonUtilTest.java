package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtilTest {
	@Test
	public void testGetMapper() {
		assertNotNull(JsonUtil.getMapper());
	}

	@Test
	public void testGetJsonFieldStr() {
		assertEquals("", JsonUtil.getJsonFieldStr(null, null));
	}

	@Test
	public void testGetJsonFieldStr1() {
		String data = "{\"fieldName\":\"\"}";
		JSONObject jsonObj = JSONObject.fromObject(data);
		assertEquals("", JsonUtil.getJsonFieldStr(jsonObj, "abc"));
	}

	@Test
	public void testGetJsonFieldStr2() {
		String data = "{\"fieldName\":null}";
		JSONObject jsonObj = JSONObject.fromObject(data);
		assertEquals("", JsonUtil.getJsonFieldStr(jsonObj, "fieldName"));
	}

	@Test
	public void testGetJsonFieldInt() {
		assertEquals((Integer) 0, JsonUtil.getJsonFieldInt(null, ""));
	}

	@Test
	public void testGetJsonFieldInt1() {
		String data = "{\"fieldName\":\"\"}";
		JSONObject jsonObj = JSONObject.fromObject(data);
		assertEquals((Integer) 0, JsonUtil.getJsonFieldInt(jsonObj, "abc"));
	}

	@Test
	public void testGetJsonFieldLong() {
		assertEquals((Long) 0L, JsonUtil.getJsonFieldLong(null, ""));
	}

	@Test
	public void testGetJsonFieldLong1() {
		String data = "{\"fieldName\":\"\"}";
		JSONObject jsonObj = JSONObject.fromObject(data);
		assertEquals((Long) 0L, JsonUtil.getJsonFieldLong(jsonObj, "abc"));
	}

	@Test
	public void testObjectToJson() {
		String data = "{\"test\":\"123\"}";
		JSONObject obj = JSONObject.fromObject(data);
		assertEquals(obj, JsonUtil.objectToJson(data));
	}

	@Test
	public void testToBean() {
		String data = "{\"VIM\": {\"id\": \"123456\",\"name\": \"GWT\",\"type\": \"openstack\",\"version\": \"1.0\",\"userName\": \"admin\",\"pwd\": \"huawei\",\"url\": \"http://192.168.3.35:5000\",\"extraInfo\": {\"domain\": \"admin\",\"authenticMode\": \"Anonymous\"}}}";
		JSONObject vimJson = JSONObject.fromObject(data).getJSONObject("VIM");
		Vim result = (Vim) JSONObject.toBean(vimJson, Vim.class);
		assertEquals(result, JsonUtil.toBean(vimJson, Vim.class));
	}

	@Test
	public void testJsonToList() {
		String data = "{\"id\": \"123456\",\"name\": \"GWT\",\"type\": \"openstack\",\"version\": \"1.0\",\"userName\": \"admin\",\"pwd\": \"huawei\",\"url\": \"http://192.168.3.35:5000\",\"extraInfo\": {\"domain\": \"admin\",\"authenticMode\": \"Anonymous\"}}";
		JSONObject vimJson = JSONObject.fromObject(data);
		JSONArray vimJsonArr = new JSONArray();
		vimJsonArr.add(vimJson);
		List<Vim> list = new ArrayList<Vim>(Constant.DEFAULT_COLLECTION_SIZE);
		list.add((Vim) JSONObject.toBean(vimJson, Vim.class));
		String vimJsonArrStr = vimJsonArr.toString();
		List<Vim> resultList = JsonUtil.jsonToList(vimJsonArrStr, Vim.class);
		assertEquals(list, resultList);
	}

	@Test
	public void testMarshal() throws IOException {
		String data = "{\"test\":\"123\"}";
		JSONObject obj = JSONObject.fromObject(data);
		assertEquals(data, JsonUtil.marshal(obj));
		assertEquals("\"{\\\"test\\\":\\\"123\\\"}\"", JsonUtil.marshal(data));
	}

	@Test
	public void testUnMarshal() throws IOException {
		String data = "{\"id\": \"111\",\"name\": \"testName\",\"type\": \"openstack\",\"version\": \"1.0\",\"userName\": \"cloud_admin\",\"pwd\": \"OpenStack123\",\"url\": \"https://identity.az1.dc1.huawei.com/identity-admin\"}";
		JSONObject vimJson = JSONObject.fromObject(data);
		Vim result = (Vim) JSONObject.toBean(vimJson, Vim.class);
		assertEquals(result, JsonUtil.unMarshal(data, Vim.class));
	}

}
