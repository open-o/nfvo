package org.openo.orchestrator.nfv.umc.pm.datacollect.bean;

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
public class PmData {

	private long collectTime; // Collection time(ms)
	private int granularity;//Collection granularity
	private int jobId; // jobId
	private Properties[] values; // Collection datas
}
