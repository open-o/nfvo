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
package org.openo.orchestrator.nfv.umc.pm.common;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskException;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskManager;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskUtil;
import org.openo.orchestrator.nfv.umc.pm.task.bean.DataaqTaskInfo;
import org.openo.orchestrator.nfv.umc.pm.task.cache.DataaqTaskCacheUtil;
import org.openo.orchestrator.nfv.umc.pm.task.threshold.ThresholdDataQueue;
import org.openo.orchestrator.nfv.umc.snmptrap.common.TrapMsgQueue;

public class PmOsfUtil
{
	public static DebugPrn dMsg = new DebugPrn(PmOsfUtil.class.getName());
	// 2013-4-26 Save queue and thread pool size from the configuration file
	private static HashMap<String, Hashtable<String, String>> hmQueueInfo;
	private static ThresholdDataQueue pmDataQueue = null;
	private static TrapMsgQueue trapMsgQueue =null;
	
	/**
	 * Read the configuration file, return the root element
	 */
	public static Element generateElementfromFile(String fileName) throws PmException
	{
		File configFile = GeneralFileLocaterImpl.getGeneralFileLocater().getFile("system", fileName);
		if (configFile == null)
		{
			throw new PmException("it not found file error" + ":" + fileName, null);
		}

		Document doc = null;
		Element element = null;
		SAXBuilder saxBuilder = new SAXBuilder(false);

		try
		{
			doc = saxBuilder.build(configFile);
			element = doc.getRootElement();
		}
		catch (JDOMException ex)
		{
			throw new PmException("read config file fail", ex);
		}
		catch (IOException ex)
		{
			throw new PmException("read config file fail", ex);
		}

		return element;
	}

	/**
	 * To re send the server's performance to PRXOY
	 * @throws ProxyLinkDownException 
	 * @throws PmException 
	 * @throws ProxyLinkDownException 
	 */
	public static void reStartAllPmTask(String proxyIp) throws PmException
	{
		List<DataaqTaskInfo> taskInfos = DataaqTaskCacheUtil.getInstance().getAllTaskInfo();
		for (DataaqTaskInfo dataaqTaskInfo : taskInfos) {
			PmTaskManager taskManager = new PmTaskManager();
			String taskProxyIp = dataaqTaskInfo.getProxyIp();
			if(proxyIp.equals(taskProxyIp))
			{
				try {
					taskManager.createTask(dataaqTaskInfo);
				} catch (PmTaskException e) {
					throw new PmException("exception when RestartAllPmTask:" + proxyIp, e);
				}
			}
		}
	}

	
	private static void parseQueuesMapFile()
	{
		Element elementRoot = null;
		List<?> listQueueInfo = null;
		Element eleDeviceInfo = null;
		String name = null;
		String maxQueueSize = null;
		String maxThreadNum = null;

		try {
			elementRoot = generateElementfromFile("umc-queues.xml");
		} catch (PmException e) {
			dMsg.error(e.getMessage());
		}

		if (null == elementRoot)
		{
			dMsg.error("umc-queues.xml error!");
			return;
		}
		hmQueueInfo = new HashMap<String, Hashtable<String, String>>();
		listQueueInfo = elementRoot.getChildren();
		if ((null == listQueueInfo) || (listQueueInfo.size() == 0))
		{
			return;
		}
		
		for (int num = 0; num < listQueueInfo.size(); num++)
		{
			Hashtable<String, String> htQueue = new Hashtable<String, String>();
			eleDeviceInfo = (Element) listQueueInfo.get(num);
			name = eleDeviceInfo.getAttributeValue("name");
			maxQueueSize = eleDeviceInfo.getAttributeValue("maxQueueSize");
			maxThreadNum = eleDeviceInfo.getAttributeValue("maxThreadNum");
			htQueue.put("maxQueueSize", maxQueueSize);
			htQueue.put("maxThreadNum", maxThreadNum);
			hmQueueInfo.put(name, htQueue);
		}
	}
	
	/**
	 * Gets the task queue configuration file information
	 */
	public static Hashtable<String, String> getHmQueueInfo(String queueName)
	{
		if (hmQueueInfo == null)
		{
			parseQueuesMapFile();
		}
		return hmQueueInfo.get(queueName);
	}
	
	public static void setPmDataQueue(ThresholdDataQueue obj)
	{
		pmDataQueue = obj;
	}
	
	public static void putPmDataToThresholdQueue(Map<?, ?> attrList)
	{
		if (pmDataQueue == null)
		{
			ThresholdDataQueue pmDataQueue = new ThresholdDataQueue();
			setPmDataQueue(pmDataQueue);
			pmDataQueue.start();
		}
		pmDataQueue.put(attrList);
	}
	
	public static final String MML_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Returns the data type of the corresponding Java based on the field type of the database
	 * 
	 * @param dbFieldType Field type of database
	 * @return  
	 * String JAVA_LANG_CLASS_INTEGER = "java.lang.Integer"; 
	 * String JAVA_LANG_CLASS_DOUBLE = "java.lang.Double"; 
	 * String JAVA_LANG_CLASS_FLOAT = "java.lang.Float"; 
	 * String JAVA_LANG_CLASS_LONG = "java.lang.Long"; 
	 * String JAVA_LANG_CLASS_SHORT = "java.lang.Short"; 
	 * String JAVA_LANG_CLASS_STRING = "java.lang.String"; 
	 * String JAVA_LANG_CLASS_BYTE = "java.lang.Byte"; 
	 * String JAVA_LANG_CLASS_BOOLEAN = "java.lang.Boolean";
	 * String JAVA_UTIL_CLASS_DATE = "java.util.Date"; 
	 * String JAVA_SQL_CLASS_DATE = "java.sql.Date";
	 * String JAVA_SQL_CLASS_TIME = "java.sql.Time"; 
	 * String JAVA_SQL_CLASS_TIMESTAMP = "java.sql.Timestamp";
	 */
	public static String getJavaDataTyeOfDbFieldType(String strDbFieldType) {
		if (strDbFieldType == null) {
			return PmConst.JAVA_LANG_CLASS_STRING;
		}
		String javaDataType = null;
		String dbFieldType = strDbFieldType.toUpperCase();
		
		//Oracle
		if (dbFieldType.startsWith("VARCHAR2")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_STRING;
		} else if(dbFieldType.startsWith("UNIVARCHAR")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_STRING;
		}
		else if ((dbFieldType.startsWith("NUMBER") || dbFieldType
				.startsWith("NUMERIC"))/** && dbFieldType.endsWith(")")* */
		) {
			String[] aStrs = dbFieldType.split(",");
			if (aStrs.length == 1) {
				javaDataType = PmConst.JAVA_LANG_CLASS_INTEGER;
			} else if (aStrs.length == 2) {
				int i = aStrs[1].trim().length();
				String str = aStrs[1].substring(0, i - 1);
				if (Integer.parseInt(str) > 0) {
					javaDataType = PmConst.JAVA_LANG_CLASS_DOUBLE;
				} else {
					int i1 = aStrs[0].lastIndexOf("(");
					String str1 = aStrs[0].substring(i1 + 1);
					if (Integer.parseInt(str1) > 10) {
						javaDataType = PmConst.JAVA_LANG_CLASS_LONG;
					} else {
						javaDataType = PmConst.JAVA_LANG_CLASS_INTEGER;
					}
				}
			} else { // Does not conform to the format definition, as string processing
				PmTaskUtil.dMsg
						.warn("##ueppm getJavaDataTyeOfDbFieldType: error parameter is "
								+ dbFieldType);
				javaDataType = PmConst.JAVA_LANG_CLASS_STRING;
			}
		} else if (dbFieldType.equals("DATE")) {
			javaDataType = PmConst.JAVA_SQL_CLASS_TIMESTAMP;
		}
		// Data type conversion of server SQL and Sybase database
		else if (dbFieldType.equals("INT")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_INTEGER;
		} else if (dbFieldType.equals("SMALLINT")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_INTEGER;
		} else if (dbFieldType.equals("TINYINT")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_INTEGER;
		} else if (dbFieldType.equals("BIGINT")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_LONG;
		} else if (dbFieldType.equals("REAL")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_DOUBLE;
		} else if (dbFieldType.equals("FLOAT")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_FLOAT;
		} else if (dbFieldType.equals("DECIMAL")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_DOUBLE;
		} else if (dbFieldType.equals("DATETIME")) {
			javaDataType = PmConst.JAVA_SQL_CLASS_TIMESTAMP;
		} else if (dbFieldType.equals("SMALLDATETIME")) {
			javaDataType = PmConst.JAVA_SQL_CLASS_TIMESTAMP;
		} else if (dbFieldType.equals("MONEY")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_DOUBLE;
		} else if (dbFieldType.equals("SMALLMONEY")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_DOUBLE;
		} else if (dbFieldType.startsWith("VARCHAR")) {
			javaDataType = PmConst.JAVA_LANG_CLASS_STRING;
		} else {
			dMsg.warn("##pm getJavaDataTyeOfDbFieldType: error parameter is "
							+ dbFieldType);
			javaDataType = PmConst.JAVA_LANG_CLASS_STRING;
		}
		return javaDataType;
	}
	
    public static Date parseDate(String sDate, String datePattern) {
        DateFormat sdf = new SimpleDateFormat(datePattern);
        try {
            return sdf.parse(sDate);
        } catch (ParseException e) {
        	dMsg.info("Parse failed. sDate = " + sDate, e);
        }

        return new Date(0);
    }
}
