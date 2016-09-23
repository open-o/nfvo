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


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;

public class Calculator {
    public String calculateItem(Map variables, String formula) throws MonitorException {

        Set vKeySet = variables.keySet();
        Object[] keys = vKeySet.toArray();
        Arrays.sort(keys);
        JexlContext jexlContext = new MapContext();
        for (int i = keys.length - 1; i >= 0; i--) {
            String s = keys[i].toString();
            String s1 = s.replace('.', 'a');
            formula = formula.replaceAll(s, s1);
            jexlContext.set(s1, variables.get(s));
        }

        try {
            while (true) {
                int eIndex = formula.indexOf("E");
                if (eIndex == -1) {
                    break;
                }
                int startIndex = getBackwardNumIndex(formula, eIndex);
                int endIndex = getForwardNumIndex(formula, eIndex);
                String eNum = formula.substring(eIndex + 1, endIndex);
                String tenNumString = null;
                int num = Integer.parseInt(eNum);
                if (num > 5) {
                    int tenNum = 1;
                    for (int i = 0; i < (num - 5); i++) {
                        tenNum = tenNum * 10;
                    }
                    tenNumString = "100000.0 * " + Integer.toString(tenNum);
                } else if (num > 0) {
                    int tenNum = 1;
                    for (int i = 0; i < num; i++) {
                        tenNum = tenNum * 10;
                    }
                    tenNumString = Integer.toString(tenNum);
                } else if (num < 0) {
                    double tenNum = 1;
                    for (int i = 0; i > num; i--) {
                        tenNum = tenNum * 0.1;
                    }
                    tenNumString = Double.toString(tenNum);
                }
                String variable = formula.substring(startIndex, endIndex);
                String headVariable = formula.substring(startIndex, eIndex);
                formula = formula.replaceFirst(variable,
                        "(" + headVariable + " * " + tenNumString + ")");
            }
            JexlEngine jexlEngine = new JexlEngine();
            Expression expression = jexlEngine.createExpression(formula);
            Object result = expression.evaluate(jexlContext);
            if (result instanceof Double) {
                Double dd = (Double) result;

                NumberFormat formater = new DecimalFormat("#.00");
                formater.setRoundingMode(RoundingMode.HALF_UP);
                return formater.format(dd);
            }
            return result.toString();
        } catch (Exception e) {
            throw new MonitorException("Error!Something wrong happened! ", e);
        }
    }

    private int getForwardNumIndex(String formula, int eIndex) {
        int i;
        for (i = eIndex + 1; i < formula.length(); i++) {
            char charNum = formula.charAt(i);
            if ((charNum < '0' || charNum > '9') && (i != eIndex + 1)) {
                break;
            }
        }
        return i;
    }

    private int getBackwardNumIndex(String formula, int eIndex) {
        int i;
        for (i = eIndex - 1; i >= 0; i--) {
            char charNum = formula.charAt(i);
            if ((charNum < '0' || charNum > '9') && (charNum != '.')) {
                break;
            } else {
                if (i == 0) {
                    return 0;
                }
            }
        }
        return (i + 1);
    }
    //<"cpuidleratio",{92.12}>,100-cpuidleratio
    public List<String> calculate(Map<String, List<String>> listMap, String formula)
            throws MonitorException {
        while (true) {
            int index = formula.indexOf("SUM(");
            if (index == -1) {
                break;
            }
            int rightbrace = formula.indexOf(")", index);
            if (rightbrace == -1) {
                throw new MonitorException(
                        "Right braces doesn't appear to exist in SUM():" + formula);
            }
            String listName = formula.substring(index + 4, rightbrace);
            List<String> vec = listMap.get(listName);
            if (vec == null) {
                throw new MonitorException(
                        "List " + listName + " doesn't appear to exist in listMap.");
            }
            double sumResult = 0;
            for (String numString : vec) {
                double thisNum = Double.parseDouble(numString);
                sumResult = sumResult + thisNum;
            }
            String sumString = Double.toString(sumResult);
            String nameString = "SUM(" + listName + ")";
            formula = DacUtil.replace(formula, nameString, sumString);
        }
        while (true) {
            int index = formula.indexOf("COUNT(");
            if (index == -1) {
                break;
            }
            int rightbrace = formula.indexOf(")", index);
            if (rightbrace == -1) {
                throw new MonitorException(
                        "Right braces doesn't appear to exist in COUNT():" + formula);
            }
            String listName = formula.substring(index + 6, rightbrace);
            List<String> list = listMap.get(listName);
            if (list == null) {
                throw new MonitorException(
                        "List " + listName + " doesn't appear to exist in listMap.");
            }
            int count = list.size();
            if (count == 0) {
                throw new MonitorException("List " + listName + "'s lenght is zero.");
            }
            String countString = Integer.toString(count);
            formula = DacUtil.replace(formula, "COUNT(" + listName + ")", countString);
        }

        List<String> listVariables = new ArrayList<>();
        Set<String> keySet = listMap.keySet();
        for (String listName : keySet) {
            if (formula.contains(listName)) {
                listVariables.add(listName);
            }
        }
        if (listVariables.size() == 0) {
            List<String> result = new ArrayList<>();
            result.add(calculateItem(new HashMap(), formula));
            return result;
        } else {
            List<String> list = listMap.get(listVariables.get(0));
            int longestSize = list.size();
            if (longestSize == 0) {
                throw new MonitorException("List " + listVariables.get(0) + "'s size is 0");
            }
            int longestIndex = 0;

            for (int i = 1, size = listVariables.size(); i < size; i++) {
                List<String> otherList = listMap.get(listVariables.get(i));
                if (otherList.size() > longestSize) {
                    longestSize = otherList.size();
                    longestIndex = i;
                }
                if (otherList.size() == 0) {
                    throw new MonitorException("ArrayList " + listVariables.get(i) + "'s size is 0");
                }
            }

            if (longestSize > 1) {
                for (int i = 0, size = listVariables.size(); i < size; i++) {
                    if (i == longestIndex) {
                        continue;
                    }
                    List<String> otherList = listMap.get(listVariables.get(i));
                    if (otherList.size() != longestSize && otherList.size() != 1) {
                        throw new MonitorException("List " + listVariables.get(i)
                                + "'s size is not " + longestSize + " and it is not 1");
                    }
                }
            }

            List<String> result = new ArrayList<>();
            for (int j = 0; j < longestSize; j++) {
                String newFormula = formula;
                for (String listVariable : listVariables) {
                    List<String> thislist = listMap.get(listVariable);
                    String thisListItem;
                    if (thislist.size() > j) {
                        thisListItem = String.valueOf(thislist.get(j));
                    } else {
                        thisListItem = String.valueOf(thislist.get(0));
                    }
                    newFormula = newFormula.replaceAll(String.valueOf(listVariable), thisListItem);//100-92.12
                }
                String resultItem;
                try {
                    resultItem = calculateItem(new HashMap(), newFormula);
                } catch (MonitorException e) {
                    throw new MonitorException(e.getMessage() + "(OldFormula=" + formula
                            + " NewFormula=" + newFormula + ")", e);
                }
                result.add(resultItem);
            }
            return result;
        }
    }
	public List fcalculate(Map listMap, String formula, String[] variable) throws MonitorException
	{
	    List retList = new ArrayList();
		Object obj = null;
		try
		{
			JexlContext jexlContext = new MapContext();
            JexlEngine jexlEngine = new JexlEngine();
            Expression expression = jexlEngine.createExpression(formula);
			int sizej = ((List) (listMap.get(variable[0]))).size();
			// 瀵筁ist涓殑姣忎竴椤硅繘琛岃绠�			for (int j = 0; j < sizej; j++)
			{
				// 将每一项数据代入表达式。
				for (int i = 0, sizei = variable.length; i < sizei; i++)
				{
					//jexlContext.set(variable[i], ((List) (listMap.get(variable[i]))).get(j));
					jexlContext.set(variable[i], ((List) (listMap.get(variable[i]))).get(i));
				}
				obj = expression.evaluate(jexlContext);
				if (obj instanceof Double)
				{
					Double dd = (Double) obj;
					double dd1 = (dd.doubleValue() * 1000);
					long ld1 = (long) dd1;
					double dd2 = ld1 / 1000.0;
					String sResult = Double.toString(dd2);
					retList.add(sResult);
				} else
				{
					throw new MonitorException("formular's result is not double type" + formula);
				}
			}

			return retList;
		}
		catch (Exception e)
		{
			throw new MonitorException(
					"parsing expression, or expression neither an expression or a reference! or evaluate error"
							+ e.getMessage());
		}
	}
}
