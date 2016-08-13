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
