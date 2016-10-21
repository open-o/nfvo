/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.nfvo.monitor.umc.res;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.openo.nfvo.monitor.umc.UMCApp;
import org.openo.nfvo.monitor.umc.cache.CacheService;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.RocResourceConfig;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheUtil;
import org.openo.nfvo.monitor.umc.fm.db.process.FmDBProcess;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.AlarmIds;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.UpdateAckStateParam;
import org.openo.nfvo.monitor.umc.fm.wrapper.ProbableCauseServiceWrapper;
import org.openo.nfvo.monitor.umc.i18n.I18n;
import org.openo.nfvo.monitor.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.db.process.PmCommonProcess;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.res.bean.NotificationResult;
import org.openo.nfvo.monitor.umc.res.resource.ChangeResource;
import org.openo.nfvo.monitor.umc.util.ExtensionAccess;
import org.openo.nfvo.monitor.umc.util.ExtensionUtil;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChangeResourcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProbableCauseServiceWrapper.class);
    private static MonitorInfo monitorInfo = null;
    private static final FmDBProcess dbProcess = mock(FmDBProcess.class);
    static {
        RocResourceConfig.setRocResourceAddr("127.0.0.1:8204");
    }
    @Captor
    private ArgumentCaptor<FmAlarmData> itAlarmDataCaptor;
    private FmAlarmData itAlarmData;
    private UpdateAckStateParam updatePara = new UpdateAckStateParam();
    private AlarmIds delPara = new AlarmIds();

    @Before
    public void setUp() {
    	String[] packageUrls =new String[] {UMCApp.class.getPackage().getName()};
    	ExtensionUtil.init(packageUrls);	
    	String filePath = "E:\\monitor-dev-code\\monitor\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf";
    	initFastFileSystem(filePath);
    	FmCacheUtil.init();
    	
    	I18n.init();
        UmcDbUtil.setSessionFactory(HibernateSession.init());
        CacheService.init();

		monitorInfo = new MonitorInfo();
		String oid = "nfv.host.linux=010074149067";
		String customPara = "{\"PORT\":\"22\",\"USERNAME\":\"cmcc\",\"PASSWORD\":\"123456\",\"PROTOCOL\":\"SSH\",\"PROXYIP\":\"192.168.113.99\"}";
		String oids = "[{\"oid\":\"nfv.host.linux=010074149067\"}]";
		monitorInfo.setOid(oid);
		monitorInfo.setIpAddress("192.168.113.107");
		monitorInfo.setNeTypeId("nfv.host.linux");
		monitorInfo.setCustomPara(customPara);
		monitorInfo.setLabel("computer-node02");
		monitorInfo.setExtendPara("");
		monitorInfo.setOrigin("roc");
		JSONObject object = JSONObject.fromObject(monitorInfo);
		System.out.println(object);
        
    }

    @After
    public void tearDown() {
    	try {
    		PmCommonProcess.delete(PmConst.MONITOR_INFO, monitorInfo);
    		PmCommonProcess.clear(PmConst.PM_TASK);
    		PmCommonProcess.clear(PmConst.PM_TASK_THRESHOLD);
    	} catch (PmException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        try {
            HibernateSession.destory();
            //H2DbServer.shutDown();
        } catch (Exception e) {
            Assert.fail("Exception" + e.getMessage());
        }
        
    }
    
    @Test
    public void putNotificatioinData() throws JsonProcessingException {
		
		String requestObj = "{'operationType':'create','resourceType':'HOST','label':" +
				"'computer-node02','ipAddress':'192.168.113.107','customPara':{'PORT':'22','USERNAME':'cmcc','PASSWORD':'123456'," +
				"'PROTOCOL':'SSH','PROXYIP':'192.168.113.99'},'data':[{'oid':'nfv.host.linux=010074149067','moc':'nfv.host.linux'}]}";
		JSONObject jsonObject = JSONObject.fromObject(requestObj);
		
		ChangeResource changeResource = new ChangeResource();
        NotificationResult result = new NotificationResult();
        result = changeResource.receiveNotification(objectToString(jsonObject));
        String resultStr = objectToString(result);
        assertThat(result.getResult()).isEqualTo("SUCCESS");
        assertThat(result.getOid()[0]).isEqualTo("nfv.host.linux=010074149067");
/*        try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
      
    }

 
    
    public static String objectToString(Object o) {
        if (o == null) return "";
        Gson gson = new Gson();
        String str = gson.toJson(o);
        return str;
    } 
    
	public static void initFastFileSystem(String configPath)
	{
		FastFileSystem.setInitDir(configPath);
		FastFileSystem.getInstance();
		File descFiles[] = FastFileSystem.getFiles("*-extendsdesc.xml");
		File implFiles[] = FastFileSystem.getFiles("*-extendsimpl.xml");
		ExtensionAccess.tryToInjectExtensionBindings(descFiles, implFiles);
	}
    


}
