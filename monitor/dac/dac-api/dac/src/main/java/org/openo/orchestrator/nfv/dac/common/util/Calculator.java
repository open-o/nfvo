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
package org.openo.orchestrator.nfv.dac.common.util;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.openo.orchestrator.nfv.dac.dataaq.common.MonitorException;

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
            Object result = (Object)expression.evaluate(jexlContext);
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

    public Vector<String> calculate(Map<String, Vector<String>> vectorMap, String formula)
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
            String vecName = formula.substring(index + 4, rightbrace);
            Vector<String> vec = vectorMap.get(vecName);
            if (vec == null) {
                throw new MonitorException(
                        "Vector " + vecName + " doesn't appear to exist in vectorMap.");
            }
            double sumResult = 0;
            for (String numString : vec) {
                double thisNum = Double.parseDouble(numString);
                sumResult = sumResult + thisNum;
            }
            String sumString = Double.toString(sumResult);
            String nameString = "SUM(" + vecName + ")";
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
            String vecName = formula.substring(index + 6, rightbrace);
            Vector<String> vec = vectorMap.get(vecName);
            if (vec == null) {
                throw new MonitorException(
                        "Vector " + vecName + " doesn't appear to exist in vectorMap.");
            }
            int count = vec.size();
            if (count == 0) {
                throw new MonitorException("Vector " + vecName + "'s lenght is zero.");
            }
            String countString = Integer.toString(count);
            formula = DacUtil.replace(formula, "COUNT(" + vecName + ")", countString);
        }

        Vector<String> vecVariables = new Vector<>();
        Set<String> keySet = vectorMap.keySet();
        for (String vecName : keySet) {
            if (formula.contains(vecName)) {
                vecVariables.add(vecName);
            }
        }
        if (vecVariables.size() == 0) {
            Vector<String> result = new Vector<>();
            result.add(calculateItem(new HashMap(), formula));
            return result;
        } else {
            Vector<String> vec = vectorMap.get(vecVariables.get(0));
            int longestSize = vec.size();
            if (longestSize == 0) {
                throw new MonitorException("Vector " + vecVariables.get(0) + "'s size is 0");
            }
            int longestIndex = 0;

            for (int i = 1, size = vecVariables.size(); i < size; i++) {
                Vector<String> otherVec = vectorMap.get(vecVariables.get(i));
                if (otherVec.size() > longestSize) {
                    longestSize = otherVec.size();
                    longestIndex = i;
                }
                if (otherVec.size() == 0) {
                    throw new MonitorException("Vector " + vecVariables.get(i) + "'s size is 0");
                }
            }

            if (longestSize > 1) {
                for (int i = 0, size = vecVariables.size(); i < size; i++) {
                    if (i == longestIndex) {
                        continue;
                    }
                    Vector<String> otherVec = vectorMap.get(vecVariables.get(i));
                    if (otherVec.size() != longestSize && otherVec.size() != 1) {
                        throw new MonitorException("Vector " + vecVariables.get(i)
                                + "'s size is not " + longestSize + " and it is not 1");
                    }
                }
            }

            Vector<String> result = new Vector<>();
            for (int j = 0; j < longestSize; j++) {
                String newFormula = formula;
                for (String vecVariable : vecVariables) {
                    Vector<String> thisVec = vectorMap.get(vecVariable);
                    String thisVecItem;
                    if (thisVec.size() > j) {
                        thisVecItem = String.valueOf(thisVec.get(j));
                    } else {
                        thisVecItem = String.valueOf(thisVec.get(0));
                    }
                    newFormula = newFormula.replaceAll(String.valueOf(vecVariable), thisVecItem);
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
	public Vector fcalculate(Map vectorMap, String formula, String[] variable) throws MonitorException
	{
		Vector retVec = new Vector();
		Object obj = null;
		try
		{
			JexlContext jexlContext = new MapContext();
            JexlEngine jexlEngine = new JexlEngine();
            Expression expression = jexlEngine.createExpression(formula);
			int sizej = ((Vector) (vectorMap.get(variable[0]))).size();
			// 对Vector中的每一项进行计算
			for (int j = 0; j < sizej; j++)
			{
				// 将每一项数据代入表达式。
				for (int i = 0, sizei = variable.length; i < sizei; i++)
				{
					jexlContext.set(variable[i], ((Vector) (vectorMap.get(variable[i]))).get(j));
				}
				obj = (Object)expression.evaluate(jexlContext);
				if (obj instanceof Double)
				{
					Double dd = (Double) obj;
					double dd1 = (dd.doubleValue() * 1000);
					long ld1 = (long) dd1;
					double dd2 = ld1 / 1000.0;
					String sResult = Double.toString(dd2);
					retVec.add(sResult);
				} else
				{
					throw new MonitorException("formular's result is not double type" + formula);
				}
			}

			return retVec;
		}
		catch (Exception e)
		{
			throw new MonitorException(
					"parsing expression, or expression neither an expression or a reference! or evaluate error"
							+ e.getMessage());
		}
	}
}
