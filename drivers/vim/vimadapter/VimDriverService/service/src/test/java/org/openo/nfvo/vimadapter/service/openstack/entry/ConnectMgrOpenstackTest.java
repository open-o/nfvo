package org.openo.nfvo.vimadapter.service.openstack.entry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;

import mockit.Mock;
import mockit.MockUp;

public class ConnectMgrOpenstackTest {	
	@Test
    public void testConnect1() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		new MockUp<ConnectionMgr>(){
			@Mock
		    public synchronized int addConnection(Vim vim) throws LoginException {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		};
		int result = connectMgrOpenstack.connect(new Vim(), "POST");
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	@Test
    public void testConnect2() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		new MockUp<ConnectionMgr>(){
			@Mock
		    public synchronized int updateConnection(Vim vim) throws LoginException {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		};
		int result = connectMgrOpenstack.connect(new Vim(), "PUT");
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	@Test
    public void testConnect3() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		new MockUp<ConnectionMgr>(){
			@Mock
		    public synchronized void disConnect(Vim vim) {
			}
		};
		int result = connectMgrOpenstack.connect(new Vim(), "DEL");
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	@Test
    public void testConnect4() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		new MockUp<ConnectionMgr>(){
			@Mock
		    public int isConnected(Vim vim) throws LoginException{
				return Constant.HTTP_OK_STATUS_CODE;
			}
		};
		int result = connectMgrOpenstack.connect(new Vim(), "handShake");
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	@Test
    public void testConnect5() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		new MockUp<ConnectionMgr>(){
			@Mock
		    public synchronized int addConnection(Vim vim) throws LoginException {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		};
		int result = connectMgrOpenstack.connect(new Vim(), "first_handShake");
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	@Test
    public void testConnect6() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		int result = connectMgrOpenstack.connect(new Vim(), "");
		int expectedResult = Constant.TYPE_PARA_ERROR_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	@Test
    public void testConnectByLoginException() throws Exception {
		ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
		new MockUp<ConnectionMgr>(){
			@Mock
		    public synchronized int addConnection(Vim vim) throws LoginException {
				throw new LoginException();
			}
		};
		int result = connectMgrOpenstack.connect(new Vim(), "first_handShake");
		int expectedResult = Constant.INTERNAL_EXCEPTION_STATUS_CODE;
		assertEquals(expectedResult, result);
	}
	
	
}
