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
package org.openo.nfvo.monitor.dac.common.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openo.nfvo.monitor.dac.datarp.DataMsgQueue;

import com.google.gson.Gson;

public class DacUtil {
    private static DataMsgQueue dataMsgQueue = null;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("YYYYMMDD HH:mm:ss", Locale.CHINESE);

    public static String timeFormat(Date date) {
        return timeFormat.format(date);
    }

    public static String dateTimeFormat(Date date) {
        return dateTimeFormat.format(date);
    }

    public static void setDataMsgQueue(DataMsgQueue obj) {
        dataMsgQueue = obj;
    }

    public static void putDataMsg(Vector<Object> msg) {
        dataMsgQueue.put(msg);
    }

    /**
     * @param formula SUM(logicvolumnsize)/1024.0
     * @param variable SUM(logicvolumnsize)
     * @param value 2048
     * @return 2048/1024.0
     */
    public static String replace(String formula, String variable, String value) {
        String result = "";
        int index = formula.indexOf(variable);
        if (index != 0 && index != -1) {
            result = result + formula.substring(0, index);
        }
        result = result + value;
        result = result + formula.substring(index + variable.length(), formula.length());
        return result;
    }

    public static String changeSHFilePath(String shFileName, String userName) {
        if (userName.equals("root")) {
            return shFileName;
        }

        StringTokenizer tokens = new StringTokenizer(shFileName, "/");
        int size = tokens.countTokens();
        String[] secs = new String[size];
        for (int i = 0; i < size; i++) {
            secs[i] = tokens.nextToken();
        }
        return "/home/" + userName + "/" + secs[size - 1];
    }

    public static Vector<String> getInfo(int line, int tokenIndex, String[] valueStr,
                                         boolean iflist, boolean iftokenall) {
        Vector<String> result = new Vector<>();
        if (!iflist) {
            String lineStr = valueStr[line - 1];
            StringTokenizer toks = new StringTokenizer(lineStr);
            int num = toks.countTokens();
            String[] tokens = new String[num];
            if (tokenIndex > num) {
                tokenIndex = num;
            }
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = toks.nextToken();
            }
            String resultItem = "";
            if (iftokenall) {
                for (int i = tokenIndex - 1; i < num; i++) {
                    resultItem = resultItem + tokens[i] + " ";
                }
                result.add(resultItem);
            } else {
                result.add(tokens[tokenIndex - 1]);
            }
        } else {
            for (int j = (line - 1); j < valueStr.length; j++) {
                String lineStr = valueStr[j];
                StringTokenizer toks = new StringTokenizer(lineStr);
                int num = toks.countTokens();
                String[] tokens = new String[num];
                if (tokenIndex > num) {
                    tokenIndex = num;
                }
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = toks.nextToken();
                }
                String resultItem = "";
                if (iftokenall) {
                    for (int i = tokenIndex - 1; i < num; i++) {
                        resultItem = resultItem + tokens[i] + " ";
                    }
                    result.add(resultItem.trim());
                } else {
                    result.add(tokens[tokenIndex - 1]);
                }
            }
        }
        return result;
    }

    public static Vector<String> delUnit(Vector initial, String unit) {
        Vector<String> result = new Vector<>();
        for (Object anInitial : initial) {
            String value = (String) anInitial;
            int unitIndex = value.indexOf(unit);
            if (unitIndex != -1) {
                value = value.substring(0, unitIndex);
            }
            result.add(value);
        }
        return result;
    }

    public static Vector<String> vectorSetScale(Vector<String> vec0, int scale) {
        if (vec0 == null) {
            return null;
        }
        int size = vec0.size();
        BigDecimal bdcl;
        for (int i = 0; i < size; i++) {
            bdcl = new BigDecimal((vec0.get(i)));
            vec0.setElementAt(bdcl.setScale(scale, BigDecimal.ROUND_HALF_UP).toString(), i);
        }
        return vec0;
    }

    public static String convertBeanToJson(Object o) {
        if (o == null) return "";
        Gson gson = new Gson();
        String str = gson.toJson(o);
        return str;
    }

	public static long toTimeticks(String strDate)
	{
		long value = 0;
		Vector<String> vecV = new Vector<String>();
		String reg = "(\\d+)";
		Matcher matcher = Pattern.compile(reg).matcher(strDate);
		while (matcher.find())
		{
			vecV.add(matcher.group());
		}
		switch (vecV.size())
		{
		case 4:
			value = Integer.parseInt(vecV.get(0).toString()) * 86400L + Integer.parseInt(vecV.get(1).toString())
					* 3600L + Integer.parseInt(vecV.get(2).toString()) * 60L + Integer.parseInt(vecV.get(3).toString());
			break;
		case 3:
			value = Integer.parseInt(vecV.get(0).toString()) * 3600L + Integer.parseInt(vecV.get(1).toString()) * 60L
					+ Integer.parseInt(vecV.get(2).toString());
			break;
		case 2:
			value = Integer.parseInt(vecV.get(0).toString()) * 60L + Integer.parseInt(vecV.get(1).toString());
			break;
		case 1:
			value = Long.parseLong(vecV.get(0).toString());
			break;
		}

		return value;
	}
}
