package org.openo.orchestrator.nfv.umc.pm.task.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Detail information of counter's threshold.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PoCounterThreshold {
	private String neId;
	private String neTypeId;
	private String pmType;
	private int attrId;
	private String attrName;
	private int alarmCode;
	private int isAlarm;
	private int direction;
	private double critical;
	private double major;
	private double minor;
	private double warning;

}
