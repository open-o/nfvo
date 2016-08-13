package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.service.constant.Constant;

import net.sf.json.JSONObject;

public class AdapterUtilTest {

	private String basePath;

	private JSONObject networkJsonObj;

	@Before
	public void setUp() {
		basePath = System.getProperty("user.dir");
		networkJsonObj = JSONObject
				.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/network.json"));
	}

	@Test
	public void testCheckAddNetworkData() {
		JSONObject network = networkJsonObj.getJSONObject("NETWORK");
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertTrue(result);
	}

	@Test
	public void testCheckAddNetworkData1() {
		String data = "{\"id\": \"\",\"projectId\": \"\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"\",\"subMask\": \"\",\"enableDhcp\": \"\",\"rpId\": \"\",\"rpName\": \"\",\"connectType\": \"\",\"vimVendorId\": \"\",\"vimId\": \"\",\"type\": \"vlan\",\"name\": \"\",\"segmentation\": \"\",\"subnets\": \"\",\"physicalNet\": \"\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData2() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData3() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"aa\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData4() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"5000\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData5() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"-1\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData6() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"flat\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertTrue(result);
	}

	@Test
	public void testCheckAddNetworkData7() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData8() {
		String data = "{\"id\": \"123\",\"projectId\": \"\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData9() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData10() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData11() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData12() {
		String data = "{\"id\": \"123\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testCheckAddNetworkData13() {
		String data = "{\"id\": \"\",\"projectId\": \"12s3\",\"isPublic\": \"True\",\"cidr\": \"\",\"subGate\": \"255.255.255.0\",\"subMask\": \"255.255.255.0\",\"enableDhcp\": \"False\",\"rpId\": \"1234566\",\"rpName\": \"az1.dc1\",\"connectType\": \"outer\",\"vimVendorId\": \"c174842154151451\",\"vimId\": \"aHR0cHM6Ly9pZGVdGl0eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\",\"type\": \"vlan\",\"name\": \"project8581_EXT1\",\"segmentation\": \"\",\"subnets\": \"255.255.255.0-255.255.255.0\",\"physicalNet\": \"physnet1\"}";
		JSONObject network = JSONObject.fromObject(data);
		boolean result = AdapterUtil.checkAddNetworkData(network);
		assertFalse(result);
	}

	@Test
	public void testGetResponseFromResmgr() {
		boolean result = AdapterUtil.getResponseFromResmgr(null);
		assertFalse(result);
	}

	@Test
	public void testGetResponseFromResmgr1() {
		RestfulResponse dbResponse = new RestfulResponse();
		dbResponse.setStatus(Constant.HTTP_OK_STATUS_CODE);
		String responseContent = "{\"retCode\":1}";
		dbResponse.setResponseJson(responseContent);
		boolean result = AdapterUtil.getResponseFromResmgr(dbResponse);
		assertTrue(result);
	}

	@Test
	public void testGetResponseFromResmgr2() {
		RestfulResponse dbResponse = new RestfulResponse();
		dbResponse.setStatus(Constant.HTTP_OK_STATUS_CODE);
		String responseContent = "{\"retCode\":-1}";
		dbResponse.setResponseJson(responseContent);
		boolean result = AdapterUtil.getResponseFromResmgr(dbResponse);
		assertFalse(result);
	}

	@Test
	public void testGetResponseFromResmgr3() {
		RestfulResponse dbResponse = new RestfulResponse();
		dbResponse.setStatus(Constant.HTTP_BAD_REQUEST_STATUS_CODE);
		boolean result = AdapterUtil.getResponseFromResmgr(dbResponse);
		assertFalse(result);
	}

}
