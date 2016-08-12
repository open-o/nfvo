package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of performance history data querying condition.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmQueryConditionBean {
    private Resource[] resources;

    private String resourceTypeId;

    private String moTypeId;

    private String[] counterOrIndexId;

    private int granularity = 900;

    private long beginTime;

    private long endTime;

    private int pageNo;

    private int pageSize;

}
