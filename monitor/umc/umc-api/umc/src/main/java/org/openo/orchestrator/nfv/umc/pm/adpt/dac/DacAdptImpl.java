package org.openo.orchestrator.nfv.umc.pm.adpt.dac;


import org.openo.orchestrator.nfv.umc.pm.adpt.dac.bean.TaskCreateAndModifyInfo;
import org.openo.orchestrator.nfv.umc.pm.adpt.dac.bean.TaskCreateAndModifyInfoAck;
import org.openo.orchestrator.nfv.umc.pm.adpt.dac.bean.TaskDeleteInfo;
import org.openo.orchestrator.nfv.umc.pm.adpt.dac.bean.TaskDeleteInfoAck;
import org.openo.orchestrator.nfv.umc.pm.adpt.dac.service.resources.ProxyServiceConsumer;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class DacAdptImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(DacAdptImpl.class);

    /**
     * @param proxyIp
     * @param createInfo
     * @return
     */
    public static TaskCreateAndModifyInfoAck taskCreate(String proxyIp,
            TaskCreateAndModifyInfo createInfo) {
        ProxyServiceConsumer proxyService = new ProxyServiceConsumer(proxyIp);
        try {
            proxyService.addProxyTask(createInfo);

            return new TaskCreateAndModifyInfoAck(0, createInfo.getTaskId(), null, null);
        } catch (RestRequestException e) {
            LOGGER.error(e.getMessage(), e);
            return new TaskCreateAndModifyInfoAck(PmConst.STATUS_FAIL, createInfo.getTaskId(), e.getMessage(), e.getMessage());
        }
    }

    /**
     * @param proxyIp
     * @param deleteInfo
     * @return
     */
    public static TaskDeleteInfoAck taskDelete(String proxyIp, TaskDeleteInfo deleteInfo) {
        LOGGER.warn("Test ProxyAdptImpl.taskDelete");

        ProxyServiceConsumer proxyService = new ProxyServiceConsumer(proxyIp);
        try {
            proxyService.deleteProxyTask(deleteInfo.getTaskId() + "");
            return new TaskDeleteInfoAck();
        } catch (RestRequestException e) {
            LOGGER.error(e.getMessage(), e);
            return new TaskDeleteInfoAck(PmConst.STATUS_FAIL, e.getMessage(), e.getMessage());
        }
    }

    /**
     * @param proxyIp
     * @param modifyInfo
     * @return
     */
    public static TaskCreateAndModifyInfoAck taskModify(String proxyIp,
            TaskCreateAndModifyInfo modifyInfo) {
        LOGGER.warn("Test ProxyAdptImpl.taskModify");

        ProxyServiceConsumer proxyService = new ProxyServiceConsumer(proxyIp);
        try {
            proxyService.modifyProxyTask(modifyInfo);
            return new TaskCreateAndModifyInfoAck(0, modifyInfo.getTaskId(), null, null);
        } catch (RestRequestException e) {
            LOGGER.error(e.getMessage(), e);
            return new TaskCreateAndModifyInfoAck(PmConst.STATUS_FAIL, modifyInfo.getTaskId(), e.getMessage(), e.getMessage());
        }
    }

}
