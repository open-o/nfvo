package org.openo.orchestrator.nfv.umc.pm.services;

import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.common.PmOsfUtil;

public class PmService{
//    private static final DebugPrn dMsg = new DebugPrn(PmService.class.getName());

    public static void reStartAllPmTask(String proxyIp) throws PmException {
        PmOsfUtil.reStartAllPmTask(proxyIp);
    }




}
