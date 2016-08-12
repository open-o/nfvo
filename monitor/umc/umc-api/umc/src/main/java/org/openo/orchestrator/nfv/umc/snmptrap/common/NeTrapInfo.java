package org.openo.orchestrator.nfv.umc.snmptrap.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class NeTrapInfo {
	private String neId;
	private String neTypeId;
	private TrapInfo trapInfo;
	
	public boolean isTrapSupport(String trapOid)
	{
		String[] trapIds = trapInfo.getTrapId();
		if (trapIds == null || trapIds.length == 0)
		{
			return true;
		}
		for (String trapId : trapIds)
		{
			if (trapOid.indexOf(trapId) != -1)
			{
				return true;
			}
		}
		return false;
	}
}
