package org.openo.orchestrator.nfv.umc.pm.adpt.roc.service;

import org.openo.orchestrator.nfv.umc.pm.adpt.roc.RocConfiguration;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity.ResourceTypeResponse;
import org.openo.orchestrator.nfv.umc.pm.bean.ResourceType;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;


/**
 * 
 */
public class ModelServiceConsumer {
    private static final DebugPrn logger = new DebugPrn(ModelServiceConsumer.class.getName());

    private static IModelRestService rocModelService;

    private static IModelRestService getRocModelServiceProxy() {
        if (rocModelService == null) {
            rocModelService =
                    ConsumerFactory.createConsumer(RocConfiguration.getRocServerAddr(),
                            IModelRestService.class);
        }

        return rocModelService;
    }

    /**
     * @param id
     * @return
     * @throws RestRequestException
     */
    public static ResourceType queryResourceType(String id) throws RestRequestException {
        logger.info("queryResourceType. id = " + id);

        try {
            ResourceTypeResponse[] response = getRocModelServiceProxy().queryResourceType(id);
            logger.info("response : " + response[0]);
            return new ResourceType(response[0].getId(), response[0].getName());
        } catch (Exception e) {
            throw new RestRequestException("roc model rest request error.", e);
        }
    }

}
