package org.openo.orchestrator.nfv.umc.pm.task.bean;

import org.openo.orchestrator.nfv.umc.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail information of counter.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PoCounterInfo
{
	private int attrId;  // counter id
	private String attrName;
	private String attrNameI18n;
	private String attrClmn;
	private String attrDataType = "NUMERIC";
//	private boolean key;
//	private int dtCountType;
//	private int locCountType;
//	private boolean realQuery;
//	private String reservationA;
//	private String reservationB;
//	private String reservationC;
//	private String poCounterDesc;
//	private int dataTypeShow;
//	private boolean visible;
	
	public void translate(String language){
	    I18n i18n = I18n.getInstance(language);
        
        if(i18n==null){
            return;
        }
        
        this.attrNameI18n = i18n.translate(this.attrName);
	}
}
