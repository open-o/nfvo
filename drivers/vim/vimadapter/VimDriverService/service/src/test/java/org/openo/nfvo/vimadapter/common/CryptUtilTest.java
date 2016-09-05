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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openo.baseservice.encrypt.cbb.CipherCreator;
import org.openo.baseservice.encrypt.cbb.inf.AbstractCipher;
import org.openo.nfvo.vimadapter.common.CryptUtil;

public class CryptUtilTest {
	
	@Test
    public void testEnCrypt() {
		AbstractCipher abstractCipher = CipherCreator.instance().create();
		String result = CryptUtil.enCrypt(null);
		String expectedResult = null;
		assertEquals(expectedResult,result);
	}

	@Test
    public void testDeCrypt() {
		AbstractCipher abstractCipher = CipherCreator.instance().create();
		String result = CryptUtil.deCrypt(null);
		String expectedResult = null;
		assertEquals(expectedResult,result);
	}
}
