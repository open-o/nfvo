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
