package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of granularity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GranularityBean {

	private String name;
	private int value;
}
