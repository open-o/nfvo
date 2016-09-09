/**
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
package org.openo.nfvo.monitor.umc.waf.bean;

import java.util.Date;

public class MockAlarmData
{
	private String alarmKey = null;
	private String oid;
	private byte eventType;
	private short systemType;
	private long code;
	private long reason;
	private Date stampHappenTime;
	private byte serverity;
	private String detailInfo;
	private String specificProblem;
	private String[] customAttrs;
    private String devIp;

	public MockAlarmData()
	{

	}

	public String getOid()
	{
		return oid;
	}

	public void setOid(String oid)
	{
		this.oid = oid;
	}

	public byte getEventType()
	{
		return eventType;
	}

	public void setEventType(byte eventType)
	{
		this.eventType = eventType;
	}

	public short getSystemType()
	{
		return systemType;
	}

	public void setSystemType(short systemType)
	{
		this.systemType = systemType;
	}

	public long getCode()
	{
		return code;
	}

	public void setCode(long code)
	{
		this.code = code;
	}

	public long getReason()
	{
		return reason;
	}

	public void setReason(long reason)
	{
		this.reason = reason;
	}

	public Date getStampHappenTime()
	{
		return stampHappenTime;
	}

	public void setStampHappenTime(Date stampHappenTime)
	{
		this.stampHappenTime = stampHappenTime;
	}

	public byte getServerity()
	{
		return serverity;
	}

	public void setServerity(byte serverity)
	{
		this.serverity = serverity;
	}

	public String getDetailInfo()
	{
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo)
	{
		this.detailInfo = detailInfo;
	}

	public String getSpecificProblem()
	{
		return this.specificProblem;
	}

	public void setSpecificProblem(String specificProblem)
	{
		this.specificProblem = specificProblem;
	}

	public String[] getCustomAttrs()
	{
		return customAttrs;
	}

	public void setCustomAttrs(String[] customAttrs)
	{
		this.customAttrs = customAttrs;
	}


	public MockAlarmData(String oid, byte eventType, short systemType, long code, long reason, Date stampHappenTime,
			byte serverity, String detailInfo)
	{
		super();
		this.oid = oid;
		this.eventType = eventType;
		this.systemType = systemType;
		this.code = code;
		this.reason = reason;
		this.stampHappenTime = stampHappenTime;
		this.serverity = serverity;
		this.detailInfo = detailInfo;

	}

	public MockAlarmData(String oid, byte eventType, short systemType, long code, Date stampHappenTime, byte serverity,
			String detailInfo, String specificProblem)
	{
		super();
		this.oid = oid;
		this.eventType = eventType;
		this.systemType = systemType;
		this.code = code;
		this.stampHappenTime = stampHappenTime;
		this.serverity = serverity;
		this.detailInfo = detailInfo;
		this.specificProblem = specificProblem;
	}

	public String getAlarmKey()
	{
		return alarmKey;
	}

	public void setAlarmKey(String alarmKey)
	{
		this.alarmKey = alarmKey;
	}

    /**
     * @return the devIp
     */
    public String getDevIp()
    {
        return this.devIp;
    }

    /**
     * @param devIp the devIp to set
     */
    public void setDevIp(String devIp)
    {
        this.devIp = devIp;
    }
}
