package org.openo.orchestrator.nfv.umc.pm.naf;

import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * History performance data detail information reported by DAC.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PmNafData {

	private String neId;
	private String poId;
	private long collectTime;
	private int granularity;
	private Properties data;
}
