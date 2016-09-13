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
package org.openo.nfvo.monitor.umc.drill.wrapper.resources;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.nfvo.monitor.umc.drill.resources.bean.response.HostInformation;
import org.openo.nfvo.monitor.umc.drill.resources.bean.response.NSInformation;
import org.openo.nfvo.monitor.umc.drill.resources.bean.response.NodeInformation;
import org.openo.nfvo.monitor.umc.drill.resources.bean.response.RootNode;
import org.openo.nfvo.monitor.umc.drill.resources.bean.response.VDUInformation;
import org.openo.nfvo.monitor.umc.drill.resources.bean.response.VNFCInformation;
import org.openo.nfvo.monitor.umc.drill.resources.bean.response.VNFInformation;
import org.openo.nfvo.monitor.umc.drill.wrapper.common.TopologyConsts;
import org.openo.nfvo.monitor.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.nfvo.monitor.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.HostData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.NfvoData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.NsData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.VduData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.VimData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.VnfData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.VnfcData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.VnfmData;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.response.RestReturnMsg;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.config.ServiceContext;
import org.openo.nfvo.monitor.umc.fm.wrapper.CurrentAlarmServiceWrapper;
import org.openo.nfvo.monitor.umc.util.APIHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *       the client stub of the resource service,holding the proxy instance used to send request
 */
public class ResourceServicesStub {
	 private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServicesStub.class);
    // singleton instance
    private static ResourceServicesStub resourceServicesStub = new ResourceServicesStub();
    private static String serverURI = null;
    private ResourceServicesStub() {
        // initialize the service context of resource service
        ServiceContext serviceContext = new ServiceContext();
        // create the proxy instance
        this.serverURI = serviceContext.getServerURI();
    }
    
    public static String queryVim(String vimId){
    	String vimName = vimId;
    	RestQueryListReturnMsg<VimData> restResult = null;
    	String url = serverURI+"/resource/vims/"+vimId;
    	try{
    		String response = APIHttpClient.doGet(url, "", "utf-8", "");
    		JSONObject jsonObject = JSONObject.fromObject(response);
    		Map<String, Class> map = new HashMap<String, Class>();  
    		map.put("data", VimData.class);
    		restResult = (RestQueryListReturnMsg<VimData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
	    
    	} catch (Exception e) {
    		LOGGER.error("query vnfm nameError!throw exception!", e);
    		return vimName;
    	}
	    if (restResult == null|| restResult.getOperationResult().equals(TopologyConsts.OPERATIONS_RESULT_FAIL)) {
            return vimName;
        }
        VimData[] vims = restResult.getData();
        if (ArrayUtils.isArrayNotEmpty(vims)) {
            vimName = vims[0].getName();
        }
        return vimName;
    }
    
    public static String queryVnfm(String vnfmId){
    	 String vnfmName = vnfmId;
         RestQueryListReturnMsg<VnfmData> restResult = null;
         String url = serverURI+"/resource/vnfms/"+vnfmId;
         try {
        	 String response = APIHttpClient.doGet(url, "", "utf-8", "");
     		 JSONObject jsonObject = JSONObject.fromObject(response);
     		 Map<String, Class> map = new HashMap<String, Class>();  
     		 map.put("data", NfvoData.class);
     		 restResult = (RestQueryListReturnMsg<VnfmData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
         } catch (Exception e) {
             LOGGER.error("query vnfm nameError!throw exception!", e);
             return vnfmName;
         }
         if (restResult == null|| restResult.getOperationResult().equals(TopologyConsts.OPERATIONS_RESULT_FAIL)) {
             return vnfmName;
         }
         VnfmData[] vnfms = restResult.getData();
         if (ArrayUtils.isArrayNotEmpty(vnfms)) {
             vnfmName = vnfms[0].getName();
         }
         return vnfmName;
    }
    
    public static HostInformation qureyHost(String hostId) throws TopologyException{
    	RestQueryListReturnMsg<HostData> restResult = null;
    	HostInformation hostinfo = null;
    	String url = serverURI+"/resource/hosts/"+hostId;
    	 try {
        	 String response = APIHttpClient.doGet(url, "", "utf-8", "");
     		 JSONObject jsonObject = JSONObject.fromObject(response);
     		 Map<String, Class> map = new HashMap<String, Class>();  
     		 map.put("data", HostData.class);
     		 restResult = (RestQueryListReturnMsg<HostData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
         } catch (Exception e) {
        	 handleRestserviceException(e);
         }
         checkRestserviceResult(restResult);
         HostData[] hosts = restResult.getData();
         // whether the current node is null
         if (ArrayUtils.isArrayNotEmpty(hosts)) {
             hostinfo = new HostInformation(hosts[0]);
             hostinfo.setAlarmCount(queryAlarmCount(hostinfo.getId()));
         } else {
             LOGGER.warn("the node dose not exist!Hostid:" + hostId);
             // if the id dose not match any node, throw exception
             throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                     "the node dose not exist!");
         }
         return hostinfo;
    }
    
    public static NodeInformation queryVnf(String resourceid) throws TopologyException {
        VNFInformation vnfInfo = null;
        RestQueryListReturnMsg<VnfData> restResult = null;
        String url = serverURI+"/resource/vnfs/"+resourceid;
        try {
       	     String response = APIHttpClient.doGet(url, "", "utf-8", "");
    		 JSONObject jsonObject = JSONObject.fromObject(response);
    		 Map<String, Class> map = new HashMap<String, Class>();  
    		 map.put("data", VnfData.class);
    		 restResult = (RestQueryListReturnMsg<VnfData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
            
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        VnfData[] vnfs = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(vnfs)) {
            vnfInfo = new VNFInformation(vnfs[0]);
            vnfInfo.setAlarmCount(queryAlarmCount(vnfInfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!VNFid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return vnfInfo;
    }
    public static NodeInformation[] queryHost(NodeInformation currentNodeDetail)throws TopologyException {
        RestQueryListReturnMsg<HostData> restResult = null;
       
        try {
            VDUInformation vduInformation = (VDUInformation) currentNodeDetail;
            String hostId = vduInformation.getHost_id();
            String url = serverURI+"/resource/hosts/"+hostId;
       	    String response = APIHttpClient.doGet(url, "", "utf-8", "");
    		JSONObject jsonObject = JSONObject.fromObject(response);
    		Map<String, Class> map = new HashMap<String, Class>();  
    		map.put("data", HostData.class);
    		restResult = (RestQueryListReturnMsg<HostData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble HostInformation[] according to the HostData[]
        HostInformation[] HostInformations = assembleData(restResult, HostData.class, HostInformation.class);

        for(int i=0;i<HostInformations.length;i++){
            HostInformations[i].setAlarmCount(queryAlarmCount(HostInformations[i].getId()));
        }
        return HostInformations;
    	
    	
    }
    
    public static NodeInformation queryVnfc(String resourceid)throws TopologyException {
        VNFCInformation vnfcInfo = null;
        RestQueryListReturnMsg<VnfcData> restResult = null;
        try {
            String url = serverURI+"/resource/vnfcs/"+resourceid;
       	    String response = APIHttpClient.doGet(url, "", "utf-8", "");
    		JSONObject jsonObject = JSONObject.fromObject(response);
    		Map<String, Class> map = new HashMap<String, Class>();  
    		map.put("data", VnfcData.class);
    		restResult = (RestQueryListReturnMsg<VnfcData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        VnfcData[] vnfcs = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(vnfcs)) {
            vnfcInfo = new VNFCInformation(vnfcs[0]);
            vnfcInfo.setAlarmCount(queryAlarmCount(vnfcInfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!VNFCid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return vnfcInfo;
    	
    }
    
    
    public static NodeInformation queryNs(String nsId) throws TopologyException {
        NSInformation nsInfo = null;
        RestQueryListReturnMsg<NsData> restResult = null;
        String url = serverURI+"/resource/nsrs/"+nsId;
        try {
        	String response = APIHttpClient.doGet(url, "", "utf-8", "");
 		    JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", NsData.class);
 		    restResult = (RestQueryListReturnMsg<NsData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
        	handleRestserviceException(e);
        }        
        checkRestserviceResult(restResult);
        NsData[] nss = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(nss)) {
            nsInfo = new NSInformation(nss[0]);
        } else {
            LOGGER.warn("the node dose not exist!nsid:" + nsId);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return nsInfo;
    }
    
    public static NodeInformation queryVdu(String vduId)  throws TopologyException {
        VDUInformation vduinfo = null;
        RestQueryListReturnMsg<VduData> restResult = null;
        String url = serverURI+"/resource/vdus/"+vduId;
        try {
        	String response = APIHttpClient.doGet(url, "", "utf-8", "");
 		    JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", NsData.class);
 		    restResult = (RestQueryListReturnMsg<VduData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
        	 handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        VduData[] vdus = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(vdus)) {
            vduinfo = new VDUInformation(vdus[0]);
            vduinfo.setAlarmCount(queryAlarmCount(vduinfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!VDUid:" + vduId);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return vduinfo;
    }
    
    public static NodeInformation[] queryVnf(NodeInformation currentNodeDetail)throws TopologyException {
        RestQueryListReturnMsg<VnfData> restResult = null;
        try {
            // VNFC stores the id of VNF,use id to query
            VNFCInformation vnfcInformation = (VNFCInformation) currentNodeDetail;
            String vnfId = vnfcInformation.getVnf_id();
            String url = serverURI+"/resource/vnfs/"+vnfId;
            String response = APIHttpClient.doGet(url, "", "utf-8", "");
 		    JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", NsData.class);
 		    restResult = (RestQueryListReturnMsg<VnfData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFInformation[] according to the VnfData[]
        VNFInformation[] VNFInformations = assembleData(restResult, VnfData.class, VNFInformation.class);

        for(int i=0;i<VNFInformations.length;i++){
            VNFInformations[i].setAlarmCount(queryAlarmCount(VNFInformations[i].getId()));
        }
        return VNFInformations;
    }
    
    public static NodeInformation[] queryVdu(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VduData> restResult = null;
        try { 	
            // VNFC stores the id of VDU,use id to query
            VNFCInformation vnfcInformation = (VNFCInformation) currentNodeDetail;
            String vduId = vnfcInformation.getVdu_id();
        	String url = serverURI+"/resource/vdus/"+vduId;    
        	String response = APIHttpClient.doGet(url, "", "utf-8", "");
 		    JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", VduData.class);
 		    restResult = (RestQueryListReturnMsg<VduData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VDUInformation[] according to the VduData[]
        VDUInformation[] VDUInformations = assembleData(restResult, VduData.class, VDUInformation.class);

        for(int i=0;i<VDUInformations.length;i++){
            VDUInformations[i].setAlarmCount(queryAlarmCount(VDUInformations[i].getId()));
        }
        return VDUInformations;
    }
    
    public static NodeInformation[] queryAllHosts(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<HostData> restResult = null;
        try {
        	String url = serverURI+"/resource/hosts";    
        	String response = APIHttpClient.doGet(url, "", "utf-8", "");
 		    JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", HostData.class);
 		    restResult = (RestQueryListReturnMsg<HostData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble HostInformation[] according to the HostData[]
        return assembleData(restResult, HostData.class, HostInformation.class);
    }
    
    public static NodeInformation[] queryAllVdus(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VduData> restResult = null;
        try {
            // query all the vdu list
          	String url = serverURI+"/resource/vdus";    
    	    String response = APIHttpClient.doGet(url, "", "utf-8", "");
		    JSONObject jsonObject = JSONObject.fromObject(response);
		    Map<String, Class> map = new HashMap<String, Class>();  
		    map.put("data", VduData.class);
		    restResult = (RestQueryListReturnMsg<VduData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VDUInformation[] according to the VduData[]
        return assembleData(restResult, VduData.class, VDUInformation.class);
    }
    public static NodeInformation[] queryVdusOfHost(NodeInformation currentNodeDetail) throws TopologyException {
        RestQueryListReturnMsg<VduData> restResult = null;

        try {
            HostInformation hostInformation = (HostInformation) currentNodeDetail;
            String hostId = hostInformation.getId();
            // query all the VduData using the hostId
            String url = serverURI+"/resource/vdus/";
            String queryStr = "hostId="+hostId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", VduData.class);
            restResult = (RestQueryListReturnMsg<VduData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VDUInformation[] according to the VduData[]
        VDUInformation[] VDUInformations = assembleData(restResult, VduData.class, VDUInformation.class);

        for(int i=0;i<VDUInformations.length;i++){
            VDUInformations[i].setAlarmCount(queryAlarmCount(VDUInformations[i].getId()));
        }
        return VDUInformations;
    	
    }
    
    public static NodeInformation[] queryNssOfVNF(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<NsData> restResult = null;
        try {
            VNFInformation vnfInformation = (VNFInformation) currentNodeDetail;
            String vnfId = vnfInformation.getId();
            String url = serverURI+"/resource/nsrs";
            String queryStr = "vnfId="+vnfId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", NsData.class);
            restResult = (RestQueryListReturnMsg<NsData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);            
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble NSInformation[] according to the NsData[]
        NSInformation[] NSInformations = assembleData(restResult, NsData.class, NSInformation.class);

        for(int i=0;i<NSInformations.length;i++){
            NSInformations[i].setAlarmCount(queryAlarmCount(NSInformations[i].getId()));
        }
        return NSInformations;
    }
    
    public static NodeInformation[] queryVnfcsOfVNF(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VnfcData> restResult = null;
        try {
            VNFInformation vnfInformation = (VNFInformation) currentNodeDetail;
            String vnfId = vnfInformation.getId();
            String url = serverURI+"/resource/vnfcs";
            String queryStr = "vnfId="+vnfId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", VnfcData.class);
            restResult = (RestQueryListReturnMsg<VnfcData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map); 
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFCInformation[] according to the VnfcData[]
        VNFCInformation[] VNFCInformations =assembleData(restResult, VnfcData.class, VNFCInformation.class);

        for(int i=0;i<VNFCInformations.length;i++){
            VNFCInformations[i].setAlarmCount(queryAlarmCount(VNFCInformations[i].getId()));
        }
        return VNFCInformations;
    }
    
    public static NodeInformation[] queryVnfsOfNS(NodeInformation currentNodeDetail) throws TopologyException {
        RestQueryListReturnMsg<VnfData> restResult = null;
        try {
            NSInformation nsInformation = (NSInformation) currentNodeDetail;
            String nsId = nsInformation.getId();
            String url = serverURI+"/resource/vnfs";
            String queryStr = "nsId="+nsId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", VnfData.class);
            restResult = (RestQueryListReturnMsg<VnfData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFInformation[] according to the VnfData[]
        return assembleData(restResult, VnfData.class, VNFInformation.class);
    	
    }
    
    public static NodeInformation[] queryVnfcsOfVDU(NodeInformation currentNodeDetail)throws TopologyException {
        RestQueryListReturnMsg<VnfcData> restResult = null;
        try {
            VDUInformation vduInformation = (VDUInformation) currentNodeDetail;
            String vduId = vduInformation.getId();
            String url = serverURI+"/resource/vnfcs";
            String queryStr = "vduId="+vduId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", VnfcData.class);
            restResult = (RestQueryListReturnMsg<VnfcData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFCInformation[] according to the VnfcData[]
        VNFCInformation[] VNFCInformations = assembleData(restResult, VnfcData.class, VNFCInformation.class);

        for(int i=0;i<VNFCInformations.length;i++){
            VNFCInformations[i].setAlarmCount(queryAlarmCount(VNFCInformations[i].getId()));
        }
        return VNFCInformations;
    	
    }
    public static NodeInformation[] queryNss_vnfsOfConductor(NodeInformation currentNodeDetail)
            throws TopologyException {
        // the result array that save NSs and VNFs
        NodeInformation[] nodeInfos = null;
        // temp array that save the NS nodes
        NodeInformation[] nsInfos = null;
        // temp array that save the VNF nodes
        NodeInformation[] vnfInfos = null;

        // get the id or root
        RootNode rootNode = (RootNode) currentNodeDetail;
        String rootId = rootNode.getId();

        /** start process the NS nodes of the system **/
        RestQueryListReturnMsg<NsData> nsRestResult = null;
        try {
            // query the NS nodes of the system
            String url = serverURI+"/resource/nsrs";
            String queryStr = "conductorId="+rootId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", NsData.class);
 		    nsRestResult = (RestQueryListReturnMsg<NsData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
            
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(nsRestResult);
        NsData[] nss = nsRestResult.getData();
        if (ArrayUtils.isArrayNotEmpty(nss)) {
            nsInfos = new NodeInformation[nss.length];
            for (int i = 0; i < nss.length; i++) {
                nsInfos[i] = new NSInformation(nss[i]);
            }
        }
        /** end process the NS nodes of the system **/
        /** start process the VNFs that not belong to any NS node **/
        RestQueryListReturnMsg<VnfData> vnfRestResult = null;
        try {
            // query the VNFs that belong to the system
            String url = serverURI+"/resource/vnfs";
            String queryStr = "conductorId="+rootId;
            String response = APIHttpClient.doGet(url, queryStr, "utf-8", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
 		    Map<String, Class> map = new HashMap<String, Class>();  
 		    map.put("data", VnfData.class);
 		    vnfRestResult = (RestQueryListReturnMsg<VnfData>)jsonObject.toBean(jsonObject, RestQueryListReturnMsg.class, map);
        } catch (Exception e) {
            handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(vnfRestResult);
        VnfData[] vnfs = vnfRestResult.getData();
        if (ArrayUtils.isArrayNotEmpty(vnfs)) {
            vnfInfos = new NodeInformation[vnfs.length];
            for (int i = 0; i < vnfs.length; i++) {
                vnfInfos[i] = new VNFInformation(vnfs[i]);
            }
        }
        /** end process the VNFs that not belong to any NS node **/

        // combine the temp NS[] and VNF[]
        nodeInfos = ArrayUtils.addAll(nsInfos, vnfInfos);

        return nodeInfos;
    }
    protected static void checkRestserviceResult(RestReturnMsg restResult) throws TopologyException {
        if (null == restResult
                || !(TopologyConsts.OPERATIONS_RESULT_SUCCESS.equals(restResult
                        .getOperationResult()))) {
            LOGGER.warn("call resource service fail!Cause:" + restResult.getException());
            // conver to the business exception and throw
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_CALLSERVICEFAULT,
                    "call resource service fail!Cause:" + restResult.getException());
        }
    }
    
    protected static int queryAlarmCount(String resourceid) {
        return new CurrentAlarmServiceWrapper().queryCurAlarmsCount(resourceid);
    }
    
    protected static void handleRestserviceException(Exception e) throws TopologyException {
        LOGGER.error("call resource service Error!throw exception!", e);
        // conver to the business exception and throw
        throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_CALLSERVICEEXCEPTION,
                "call resource service throw exception!Cause:" + e.getMessage());
    }
    
    protected static <S extends NfvoData, T extends NodeInformation> T[] assembleData(
            RestQueryListReturnMsg<S> restResult, Class<S> sourceType, Class<T> targetType)
            throws TopologyException {
        S[] source = restResult.getData();
        T[] result = null;
        try {
            result = ArrayUtils.convertArrayType(source, sourceType, targetType);
        } catch (Exception e) {
            LOGGER.error("assemble result data from restresult throw exception!", e);
            // conver to the business exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_ASSEMBLEDATAFAIL,
                    "assemble result data from restresult throw exception!Cause:" + e.getMessage());
        }
        return result;
    }
}
