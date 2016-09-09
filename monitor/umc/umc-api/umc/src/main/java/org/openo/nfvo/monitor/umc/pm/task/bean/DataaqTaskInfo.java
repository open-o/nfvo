/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.monitor.umc.pm.task.bean;

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
