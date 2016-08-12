package org.openo.orchestrator.nfv.umc.pm.adpt.roc.service;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.RocConfiguration;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity.ResourceEntity;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;
import org.openo.orchestrator.nfv.umc.util.ExtensionAccess;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

public class ResourceServiceConsumer{

    private static final DebugPrn logger = new DebugPrn(ResourceServiceConsumer.class.getName());

    private IPmResourceRestService rocService;

    public ResourceServiceConsumer(String neType) {
    	
    	try
    	{
	    	Class<IPmResourceRestService> serviceClass = ExtensionAccess.getExtensionClass(IPmResourceRestService.class.getName(), neType);
	    	this.rocService = ConsumerFactory.createConsumer(
	                RocConfiguration.getRocServerAddr(), serviceClass);
    	}
    	catch (Exception e)
    	{
    		logger.info("No IPmResourceRestService implement for " + neType);
    	}
    }


    /**
     * get ne informap by roc rest api
     * 
     * @param oid
     * @return
     * @throws RestRequestException
     */
    @SuppressWarnings("rawtypes")
    public Map getNeInfoMap(String oid) throws RestRequestException {
        Map paraMap = null;
        if (rocService != null)
        {
	        try {
	            String jsonStr = rocService.getResourceList(oid);
	            logger.info(oid + " jsonstr:" + jsonStr);
	            ObjectMapper objectMapper = new ObjectMapper();
	            paraMap = objectMapper.readValue(jsonStr, Map.class);
	        } catch (Exception e) {
	            //if get nothing, doesn't throw RestRequestException, just return null
	            return paraMap;
	        }
	        if (paraMap.containsKey("data") && ((List) paraMap.get("data")).size()>0) {
	            paraMap = (Map) ((List) paraMap.get("data")).get(0);
	        }
	        else
	        {
	        	paraMap = null;
	        }
        }
        return paraMap;
    }


    public ResourceEntity[] queryAllResource() throws RestRequestException {
    	if (rocService != null)
    	{
    		return this.rocService.queryAllResource().getData();
    	}
    	else
    	{
    		return null;
    	}
    }
}
