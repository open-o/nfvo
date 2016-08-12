package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of history performance data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPmDataBean {
    private long beginTime;

    private long endTime;

    private int granularity = 900;

    private Resource resource;

    private ResourceType resourceType;

    private PmMoType moType;

    private PmData[] datas;
}
