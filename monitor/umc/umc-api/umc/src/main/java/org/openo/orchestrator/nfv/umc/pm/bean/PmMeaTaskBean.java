package org.openo.orchestrator.nfv.umc.pm.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of performance measure task.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmMeaTaskBean implements Comparable<Object> {
    private String id = null;
    private String name = null;
	private ResourceType resourceType;
    private PmMoType moType;
    private Resource [] resources;
    private int activeStatus;
    private int granularity = 900;
    private long beginTime = new Date().getTime();
    private long endTime = new Date().getTime();
    
    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((PmMeaTaskBean) o).getId());
    }

}
