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
package org.openo.orchestrator.nfv.dac.snmptrap.processor.util;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.entity.TrapBindInfo;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.entity.TrapDescInfo;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.entity.TrapMappingData;
import org.openo.orchestrator.nfv.dac.snmptrap.queue.TrapSendMsgQueue;
import org.openo.orchestrator.nfv.dac.snmptrap.util.MibUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

public class TrapProcessorUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapProcessorUtil.class);

    private static HashMap judgeBindOids = new HashMap();
    private static Set allRestoreTraps = new HashSet();
    public static void processEntityOids(StringBuilder strEntity, List<String> entityOids, PDU trapPDU)
    {
        Vector<VariableBinding> allVars = trapPDU.getVariableBindings();
        for (int i = 0; i < entityOids.size(); i++)
        {
            String entityOid = entityOids.get(i);
            for (VariableBinding vb : allVars)
            {

                String oid = vb.getOid().toString();
                if (oid.indexOf(entityOid) != -1)
                {
                    String entity = vb.getVariable().toString();
                    strEntity.append("@");
                    strEntity.append(entity);
                    break;
                }
            }
        }
    }


    public static Properties parseBindingVar(PDU trapPDU, Map bindVar)
    {
    	Properties prop = new Properties();
		Vector<VariableBinding> allVars = trapPDU.getVariableBindings();
		for (VariableBinding vb : allVars) {
			String bindOid = vb.getOid().toString();
			String realOid = isExist(bindVar, bindOid);
			if (realOid != null) {
//				String part = bindOid.replaceFirst(realOid, "");
				List listBind = (List) bindVar.get(realOid);
				Iterator iter = listBind.iterator();
				String oidLable = ((TrapBindInfo) iter.next()).getBindDesc();
				if (oidLable.equalsIgnoreCase("")
						|| oidLable.equalsIgnoreCase(null)) {
					oidLable = MibUtil.getNodeLable(
							realOid);
				}

				String value = vb.getVariable().toString();
				Iterator iter2 = listBind.iterator();
				String desc = "";
				while (iter2.hasNext()) {
					TrapBindInfo entry = (TrapBindInfo) iter2.next();
					String formular = (String) entry.getBindFormula();
					if (formular != null && isMatch(value, formular)) {
						desc = (String) entry.getBindValueDesc();
						break;
					}
				}
				if (desc.equalsIgnoreCase("")) {
					desc = value;
				}
				prop.setProperty(oidLable, desc);
			} else {
				if (!isLableIgnore(bindOid)) {
					String oidLable = MibUtil
							.getNodeLable(bindOid);
					String value = vb.getVariable().toString();
					prop.setProperty(oidLable, value);
				}
			}
		}
		return prop;

	}

    public static String parseJudgeValueByBindVar(PDU trapPDU, Map bindVar, Map judgeBindOid)
    {
        Vector<VariableBinding> allVars = trapPDU.getVariableBindings();
        StringBuffer info = new StringBuffer();
        for (VariableBinding vb : allVars)
        {
            String bindOid = vb.getOid().toString();
            String realOid = isExist(bindVar, bindOid);
            Iterator iter = judgeBindOid.keySet().iterator();
            while (iter.hasNext())
            {
                String jBindOid = iter.next().toString();
                if (jBindOid.equalsIgnoreCase("null"))
                {
                    return "";
                }
                Object objToken = judgeBindOid.get(jBindOid);
                int tokenValue;
                tokenValue = (objToken != null) ? Integer.parseInt(objToken.toString()) : -1;
                if ((bindOid + ".").indexOf(jBindOid + ".") != -1)
                {
                    if (realOid != null)
                    {
//                        String part = bindOid.replaceFirst(realOid, "");
                        List listBind = (List) bindVar.get(realOid);
//                        Iterator iter2 = listBind.iterator();
//                        String oidLable = ((TrapBindInfo) iter2.next()).getBindDesc();
//                        if (oidLable.equalsIgnoreCase("") || oidLable.equalsIgnoreCase(null))
//                        {
//                            oidLable = TrapConfUtil.getMibNodeInfo().getNodeLable(realOid);
//                        }
//                        info.append(oidLable + part + ":");
                        String value = vb.getVariable().toString().trim();

                        Iterator iter3 = listBind.iterator();
                        String desc = "";
                        while (iter3.hasNext())
                        {
                            TrapBindInfo entry = (TrapBindInfo) iter3.next();
                            String formular = (String) entry.getBindFormula();
                            if (formular != null && isMatch(value, formular))
                            {
                                desc = (String) entry.getBindValueDesc();
                                break;
                            }
                        }
                        if (desc.equalsIgnoreCase(""))
                        {
                            desc = value;
                        }
                        String[] values = desc.split("\\s");
                        desc = (tokenValue != -1 && values.length > tokenValue) ? values[tokenValue - 1] : desc;
                        info.append(desc);
                    }
                    else
                    {
//                        String oidLable = TrapConfUtil.getMibNodeInfo().getNodeLable(bindOid);
//                        info.append(oidLable + ":");
                        String value = vb.getVariable().toString().trim();
                        String[] values = value.split("\\s");
                        value = (tokenValue != -1 && values.length > tokenValue) ? values[tokenValue - 1] : value;
                        info.append(value );
                    }
                    break;
                }
            }
        }
        return info.toString();
    }

    // is bindvalue exist
    private static String isExist(Map bindVars, String strOid)
    {
        String preOid = null;
        Iterator iter = bindVars.keySet().iterator();
        while (iter.hasNext())
        {
            String exitOid = iter.next().toString();
            if ((strOid + ".").indexOf(exitOid + ".") != -1)
            {
                preOid = exitOid;
                break;
            }
        }
        return preOid;
    }

    private static boolean isMatch(String value, String formular)
    {
        if (formular == null || formular.equalsIgnoreCase(""))
        {
            return false;
        }
        String strFom[] = formular.split(",");
        String strExp = strFom[0];
        String strValue = strFom[1];
        if (strExp.equalsIgnoreCase("="))
        {
            if (value.equals(strValue))
                return true;
        }
        if (strExp.equalsIgnoreCase("!="))
        {
            if (!value.equals(strValue))
                return true;
        }
        if (strExp.equalsIgnoreCase(">"))
        {
            try
            {
                if (Long.parseLong(value) > Long.parseLong(strValue))
                    return true;
            }
            catch (Exception e)
            {
                LOGGER.error(e.getMessage());
            }
        }
        if (strExp.equalsIgnoreCase("<"))
        {
            try
            {
                if (Long.parseLong(value) < Long.parseLong(strValue))
                    return true;
            }
            catch (Exception e)
            {
            	 LOGGER.error(e.getMessage());
            }
        }
        if (strExp.equalsIgnoreCase(">="))
        {
            try
            {
                if (Long.parseLong(value) >= Long.parseLong(strValue))
                    return true;
            }
            catch (Exception e)
            {
            	 LOGGER.error(e.getMessage());
            }
        }
        if (strExp.equalsIgnoreCase("<="))
        {
            try
            {
                if (Long.parseLong(value) <= Long.parseLong(strValue))
                    return true;
            }
            catch (Exception e)
            {
            	 LOGGER.error(e.getMessage());
            }
        }
        if (strExp.equalsIgnoreCase("contains"))
        {
            if (value.indexOf(strValue) != -1)
                return true;
        }
        if (strExp.equalsIgnoreCase("!contains"))
        {
            if (value.indexOf(strValue) == -1)
                return true;
        }
        if (strExp.equalsIgnoreCase("range(B_E)"))
        {
            String rangeValue[] = strValue.split("_");
            try
            {
                if ((Long.parseLong(value) > Long.parseLong(rangeValue[0]))
                    && (Long.parseLong(value) < Long.parseLong(rangeValue[1])))
                    return true;
            }
            catch (Exception e)
            {
            	 LOGGER.error(e.getMessage());
            }
        }
        return false;
    }

    public static Properties parseBindingVarByMibFiles(PDU trapPDU)
    {
        Vector<VariableBinding> allVars = trapPDU.getVariableBindings();
        Properties prop = new Properties();
        for (VariableBinding vb : allVars)
        {
            String oid = vb.getOid().toString();
            String oidLable = MibUtil.getNodeLable(oid);
            String value = vb.getVariable().toString().trim();
//                String realOid = TrapConfUtil.getMibNodeInfo().isExit(oid);
            // sysUpTime, snmpTrapOid
            if (isLableIgnore(oid))
            {
            	continue;
            }
            prop.put(oidLable, value);
        }
        return prop;
    }

    public static String parseJudgeValueByMibFiles(PDU trapPDU, Map judgeBindOid)
    {
        Vector<VariableBinding> allVars = trapPDU.getVariableBindings();
        StringBuffer info = new StringBuffer();
        for (VariableBinding vb : allVars)
        {
            String oid = vb.getOid().toString();
            Iterator iter = judgeBindOid.keySet().iterator();
            while (iter.hasNext())
            {
                String bindOid = iter.next().toString();
                if (bindOid.equalsIgnoreCase("null"))
                {
                    return "";
                }
                Object objToken = judgeBindOid.get(bindOid);
                int tokenValue;
                tokenValue = (objToken != null) ? Integer.parseInt(objToken.toString()) : -1;
                if ((oid + ".").indexOf(bindOid + ".") != -1)
                {
//                    String oidLable = TrapConfUtil.getMibNodeInfo().getNodeLable(oid);
                    String value = vb.getVariable().toString().trim();
//                        String realOid = TrapConfUtil.getMibNodeInfo().isExit(oid);
                    String[] values = value.split("\\s");
                    value = (tokenValue != -1 && values.length > tokenValue) ? values[tokenValue - 1] : value;
                    info.append(value);
                    break;
                }
            }
        }
        return info.toString();
    }


	public static void sendTrapData(TrapData trapData) {
		trapData.setAlarmRaiseTime(new Date());
		TrapSendMsgQueue.getInstance().put(trapData);
	}

    public static TrapDescInfo getTrapDescInfo(String oid)
    {
        TrapDescInfo trapDescInfo = TrapProcessorConfig.getTrapDescInfo(oid);
        if (trapDescInfo == null)
        {
            oid = processOid(oid);
            trapDescInfo = TrapProcessorConfig.getTrapDescInfo(oid);
        }
        return trapDescInfo;
    }

    public static TrapMappingData getTrapMappingData(String oid)
    {
        TrapMappingData trapMappingData = TrapProcessorConfig.getTrapMappingData(oid);
        if (trapMappingData == null)
        {
            oid = processOid(oid);
            trapMappingData = TrapProcessorConfig.getTrapMappingData(oid);
        }
        return trapMappingData;
    }

    /**
     * if .1.3.6.1.4.1.89.35.1.40.19 get nothing, use .1.3.6.1.4.1.89.35.1.40.0.19 to query
     * if .1.3.6.1.4.1.89.35.1.40.0.19 get nothing, use.1.3.6.1.4.1.89.35.1.40.19 to query
     */
    public static String processOid(String oid)
    {
        String tmp[] = oid.split("[.]");
        int pos = oid.lastIndexOf(".");
        int index = tmp.length - 2;
        //System.out.println(tmp[index]);
        if (tmp[index].equals("0"))
        {
            oid = oid.substring(0, pos - 1) + tmp[index + 1];
        }
        else
        {
            oid = oid.substring(0, pos) + ".0." + tmp[index + 1];
        }
        return oid;
    }


    public static Map getJudgeBindOidByRelateOid(String relateOid)
    {
        Map relateOids =  (Map)judgeBindOids.get(relateOid);
        if (relateOids == null)
        {
            relateOid = processOid(relateOid);
            relateOids =  (Map)judgeBindOids.get(relateOid);
        }
        return relateOids;
    }

    public static void addJudgeBindOid(String relOid, Map judgeOidsMap)
    {
        judgeBindOids.put(relOid, judgeOidsMap);
    }

    public static boolean isRestoreTraps(String oid)
    {
        boolean flag = allRestoreTraps.contains(oid);
        if (!flag)
        {
            oid = processOid(oid);
            flag = allRestoreTraps.contains(oid);
        }
        return flag;
    }

    public static void addRestoreTrap(String oid)
    {
        allRestoreTraps.add(oid);
    }

    private static boolean isLableIgnore(String oid)
    {
    	if (oid != null && (oid.indexOf("1.3.6.1.2.1.1.3") != -1 || oid.indexOf("1.3.6.1.6.3.1.1.4.1") != -1))
    	{
    		return true;
    	}
    	return false;
    }
}
