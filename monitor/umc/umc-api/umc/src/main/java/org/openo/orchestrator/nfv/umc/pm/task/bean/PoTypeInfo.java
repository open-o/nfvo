package org.openo.orchestrator.nfv.umc.pm.task.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail information of performance object.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PoTypeInfo
{

	private String moTypeId;
	private String pmType;
	private String monitorName;
	private String dataTableName;
	private PoCounterInfo[] poCounterInfo ;
}
