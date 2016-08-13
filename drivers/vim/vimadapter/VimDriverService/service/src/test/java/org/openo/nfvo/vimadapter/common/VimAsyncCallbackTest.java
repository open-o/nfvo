package org.openo.nfvo.vimadapter.common;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

public class VimAsyncCallbackTest {
	@Test
	public void testCallback(){
		RestfulResponse response = new RestfulResponse();
		VimAsyncCallback vimAsyncCallback = new VimAsyncCallback();
		vimAsyncCallback.callback(response);
	}
	
	@Test
	public void testHandleExcepion(){
		Throwable e = new Throwable();
		VimAsyncCallback vimAsyncCallback = new VimAsyncCallback();
		vimAsyncCallback.handleExcepion(e);
	}

}
