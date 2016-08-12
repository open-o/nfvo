package org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Detail information of performance threshold alarm.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class FmAlarmData
{

	private String alarmKey;
	private String oid;
	private int eventType;//alarm type
	private long code;//alarm code
	private int systemType;//system code
	private Date alarmRaiseTime;//alarm time
	private int serverity;//alarm level
	private String detailInfo;
    private String devIp;
    private String moc;
    
    public String toString()
    {
    	return "alarmKey:" + alarmKey + " oid:" + oid + " eventType:" + eventType + " code:" + code
    			+ " alarmRaiseTime:" + alarmRaiseTime.toString() + " serverity:" + serverity 
    			+ " detailInfo:" + detailInfo + " systemType:" + systemType + " devIp:" + devIp + " moc:" + moc;
    }

	public String getAlarmKey() {
		return alarmKey;
	}

	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public int getSystemType() {
		return systemType;
	}

	public void setSystemType(int systemType) {
		this.systemType = systemType;
	}

	public Date getAlarmRaiseTime() {
		return alarmRaiseTime;
	}

	public void setAlarmRaiseTime(Date alarmRaiseTime) {
		this.alarmRaiseTime = alarmRaiseTime;
	}

	public int getServerity() {
		return serverity;
	}

	public void setServerity(int serverity) {
		this.serverity = serverity;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getDevIp() {
		return devIp;
	}

	public void setDevIp(String devIp) {
		this.devIp = devIp;
	}

}
