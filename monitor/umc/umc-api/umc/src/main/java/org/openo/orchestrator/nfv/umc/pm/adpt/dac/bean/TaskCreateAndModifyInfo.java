package org.openo.orchestrator.nfv.umc.pm.adpt.dac.bean;

import java.util.Properties;

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
public class TaskCreateAndModifyInfo
{
    private int taskId;
    private int granularity;
    private String monitorName;
    private String[] columnName; 
    private Properties commParam; //properties[0] = {key, value}
}
