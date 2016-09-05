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
