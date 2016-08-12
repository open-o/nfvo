package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of counter.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmCounter {
    private String id;
    private String name;
	private String dataType;

}
