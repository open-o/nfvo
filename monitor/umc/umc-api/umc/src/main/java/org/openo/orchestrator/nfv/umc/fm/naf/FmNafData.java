package org.openo.orchestrator.nfv.umc.fm.naf;

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
public class FmNafData {

	private String eventId;
	private int eventType;//1 alarm, 2 restore, 3 notification
	private long eventCode;
	private int eventSystemType;//20105 trap alarm,20104 status alarm
	private int eventSubType;//0 Communications, 3 Equipment
	private long eventRaiseTime;
//	private String eventName;
//	private String eventReason;
//	private String eventRepairAction;
	private int eventServerity;//1 critical, 2 major, 3 minor, 4 critical, 0 info
	private String eventDetailInfo;
    private String neIp;
    private String neType;
    private String neId;
}
