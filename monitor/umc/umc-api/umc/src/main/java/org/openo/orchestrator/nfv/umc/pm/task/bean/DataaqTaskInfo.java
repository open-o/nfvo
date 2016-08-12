package org.openo.orchestrator.nfv.umc.pm.task.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Detail information of performance data collection task.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataaqTaskInfo {
	private String proxyIp;
	private int measureGranularity;
	private Date beginTime;
	private Date endTime;
	private String pmType;
	private String neId;
	private int jobId;
//	private String moType;
	private String neType;
	private Date taskCreateTime;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("proxyIp=");
		sb.append(proxyIp);
		sb.append(",jobId=");
		sb.append(jobId);
		sb.append(",neId=");
		sb.append(neId);
//		sb.append(",moType=");
//		sb.append(moType);
		sb.append(",taskCreateTime=");
		sb.append(taskCreateTime);
		return sb.toString();
	}
}
