/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.dac.snmptrap.util;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.VariableBinding;

public class TrapConfUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrapConfUtil.class);
    public static final String COOLSTART = "0.0.0.0";
    public static final String WARMSTART = "0.0.0.1";
    public static final String LINKDOWN = "0.0.0.2";
    public static final String LINKUP = "0.0.0.3";
    public static final String AUTHENTICATIONFAIL = "0.0.0.4";
    public static final String EGPNEIGHBORLOSS = "0.0.0.5";
    public static final String ENTERPRISESPECIFIC = "0.0.0.6";
//	private static MibNodeInfo mibNodeInfo = null;

//    public static void setMibNodeInfo(MibNodeInfo mibNodeInfo)
//    {
//        TrapConfUtil.mibNodeInfo = mibNodeInfo;
//    }
//
//    public static MibNodeInfo getMibNodeInfo() {
//		return mibNodeInfo;
//	}

	public static Element getElementFromXmlFile(File file) throws JDOMException, IOException {

        if (file == null) {
            LOGGER.warn(file.getAbsolutePath() + "XmlFile:file is null");
            return null;
        }

        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(file);
        return document.getRootElement();
    }

    public static String getElementValue(Element xmlElement, String attributeName)
    {
        Attribute xmlAttribute = xmlElement.getAttribute(attributeName);
        if (xmlAttribute != null)
            return xmlAttribute.getValue();
        else
            return null;
    }

    /**
     * when snmp trap v1,trapOid = enterprise oid + SpecificType
     */
    @SuppressWarnings("unchecked")
	public static String getTrapOID(PDU trapPDU)
    {
        String trapOID = null;
        if (trapPDU.getType() == PDU.V1TRAP)
        {
        	PDUv1 pduV1 = (PDUv1)trapPDU;
            switch (pduV1.getGenericTrap())
            {
                case 0:
                    trapOID = COOLSTART;
                    break;
                case 1:
                    trapOID = WARMSTART;
                    break;
                case 2:
                    trapOID = LINKDOWN;
                    break;
                case 3:
                    trapOID = LINKUP;
                    break;
                case 4:
                    trapOID = AUTHENTICATIONFAIL;
                    break;
                case 5:
                    trapOID = EGPNEIGHBORLOSS;
                    break;
                case 6:
                    trapOID = pduV1.getEnterprise() + "." + pduV1.getSpecificTrap();
                    break;
                default:
                	LOGGER.error("trapPDU's type is unknown!");
                    break;
            }
        }
        else
        {
            Vector<VariableBinding> allVars = trapPDU.getVariableBindings();

                for (VariableBinding vb : allVars)
                {
                    String oid = vb.getOid().toString();
                    if (oid.startsWith("1.3.6.1.6.3.1.1.4.1"))
                    {
                        trapOID = vb.getVariable().toString();
                        break;
                    }
                }
        }
        return trapOID;
    }
}
