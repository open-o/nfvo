package org.openo.nfvo.vimadapter.service.openstack.connectmgr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.vimadapter.common.TestUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class ConnectionMgrTest {
	private ConnectionMgr connectionMgr; 
	
    private String basePath;
	
	private JSONObject vimJsonObj;
	
	
	@Before
    public void setUp() {
		connectionMgr = ConnectionMgr.getConnectionMgr();
		basePath = System.getProperty("user.dir");
		vimJsonObj = JSONObject.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/vim.json"));
	}

	@Test
	public void testAddConnection() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
		new MockUp<OpenstackConnection>(){
			@Mock
		    public int connect() {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		};
		int result = connectionMgr.addConnection(vim);
        int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult,result);
	}
	
	@Test
	public void testUpdateConnection() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
        connectionMgr.disConnect(vim);
		int result = connectionMgr.updateConnection(vim);
        int expectedResult = Constant.CONNECT_NOT_FOUND_STATUS_CODE;
        assertEquals(expectedResult,result);
	}
	
	@Test
	public void testUpdateConnection1() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
		new MockUp<OpenstackConnection>(){
			@Mock
		    public int connect() {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		}; 
		connectionMgr.addConnection(vim);
		int result = connectionMgr.updateConnection(vim);
        int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult,result);
	}
	
	@Test
	public void testGetConnection() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
        ConnectInfo info = new ConnectInfo(vim);
		new MockUp<OpenstackConnection>(){
			@Mock
		    public int connect() {
				return Constant.HTTP_OK_STATUS_CODE;
			}
			@Mock
		    public int isConnected(){
				return Constant.HTTP_UNAUTHORIZED_STATUS_CODE;
			}
		}; 
		connectionMgr.addConnection(vim);
		OpenstackConnection result = connectionMgr.getConnection(info);
		OpenstackConnection expectedResult = new OpenstackConnection();
        assertEquals(expectedResult.getDomainTokens(),result.getDomainTokens());
	}
	
	@Test
	public void testGetConnection1() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
        ConnectInfo info = new ConnectInfo(vim);
		new MockUp<OpenstackConnection>(){
			@Mock
		    public int connect() {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		}; 
		connectionMgr.disConnect(vim);
		OpenstackConnection result = connectionMgr.getConnection(info);
		OpenstackConnection expectedResult = new OpenstackConnection();
        assertEquals(expectedResult.getDomainTokens(),result.getDomainTokens());
	}
	
	@Test
	public void testDisConnect() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
		new MockUp<OpenstackConnection>(){
			@Mock
		    public int connect() {
				return Constant.HTTP_OK_STATUS_CODE;
			}
		}; 
		connectionMgr.addConnection(vim);
		connectionMgr.disConnect(vim);
	}
	
	@Test
	public void testIsConnected() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
		new MockUp<OpenstackConnection>(){
			@Mock
		    public int connect() {
				return Constant.HTTP_OK_STATUS_CODE;
			}
			@Mock
		    public int isConnected(){
				return Constant.HTTP_OK_STATUS_CODE;
			}
		}; 
		connectionMgr.addConnection(vim);
		int result = connectionMgr.isConnected(vim);
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult,result);
	}
	
	@Test
	public void testIsConnected1() throws Exception{
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
		int result = connectionMgr.isConnected(vim);
		int expectedResult = Constant.CONNECT_FAIL_STATUS_CODE;
        assertEquals(expectedResult,result);
	}
	
}
