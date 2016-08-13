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
