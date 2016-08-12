package org.openo.orchestrator.nfv.umc.pm.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.openo.orchestrator.nfv.umc.pm.adpt.roc.RocAdptImpl;
import org.openo.orchestrator.nfv.umc.pm.bean.Resource;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;

public class ResourceServiceWrapper {
    /**
     * @param resTypeId
     * @return
     * @throws RestRequestException 
     */
    public static List<Resource> queryResources(String resTypeId) throws RestRequestException {
        if (resTypeId == null || resTypeId.isEmpty()) {
            List<String> resTypeIdList = RocAdptImpl.getNeTypeIds();
            List<Resource> resList = new ArrayList<Resource>();
            for (int i = 0; i < resTypeIdList.size(); i++) {
                resList.addAll(RocAdptImpl.getResourcesByType(resTypeIdList.get(i)));
            }

            return resList;
        }

        return RocAdptImpl.getResourcesByType(resTypeId);
    }

}
