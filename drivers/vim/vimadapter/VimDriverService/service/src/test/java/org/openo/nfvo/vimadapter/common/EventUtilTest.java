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
