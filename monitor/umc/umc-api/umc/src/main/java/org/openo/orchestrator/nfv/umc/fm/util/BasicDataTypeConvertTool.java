/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.fm.util;


import java.util.ArrayList;
import java.util.List;

import org.openo.orchestrator.nfv.umc.fm.db.entity.SystemType;

import com.google.gson.Gson;

public class BasicDataTypeConvertTool {
    public static int[] convertByteArray2IntArray(byte[] bytes) {
        if (bytes != null) {
            int[] res = new int[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                res[i] = bytes[i];
            }
            return res;
        }
        return new int[0];
    }

    public static byte[] convertIntArray2ByteArray(int[] ints) {
        if (ints != null) {
            byte[] bytes = new byte[ints.length];
            for (int i = 0; i < ints.length; i++) {
                bytes[i] = (byte) ints[i];
            }
            return bytes;
        }
        return new byte[0];
    }

    public static long[] convertLongList2longArray(ArrayList<Long> list) {
        if (list == null) {
            return new long[0];
        }
        long[] array = new long[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static String[] longArray2StringArray(long[] longArray) {
        if (longArray == null) {
            return new String[] {};
        }
        String[] strArray = new String[longArray.length];
        for (int i = 0; i < longArray.length; ++i) {
            strArray[i] = Long.toString(longArray[i]);
        }
        return strArray;
    }

    public static String longArray2String(long[] array, String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        String result = "";
        for (long temp : array) {
            result += String.valueOf(temp) + separator;
        }
        int location = result.length() - separator.length() - 1;
        return result.substring(0, location);
    }

    public static Integer[] convert2IntegerObjectArray(int[] ints) {
        if (ints != null) {
            Integer[] integerArray = new Integer[ints.length];
            for (int i = 0; i < ints.length; i++) {
                integerArray[i] = ints[i];
            }
            return integerArray;
        }
        return new Integer[0];
    }

    public static Integer[] convert2IntegerObjectArray(String[] ints) {
        if (ints != null) {
            Integer[] integerArray = new Integer[ints.length];
            for (int i = 0; i < ints.length; i++) {
                integerArray[i] = Integer.valueOf(ints[i]);
            }
            return integerArray;
        }
        return new Integer[0];
    }

    public static Long[] convert2LongObjectArray(long[] longs) {
        if (longs != null) {
            Long[] integerArray = new Long[longs.length];
            for (int i = 0; i < longs.length; i++) {
                integerArray[i] = longs[i];
            }
            return integerArray;
        }
        return new Long[0];
    }

    public static String objectToString(Object o) {
        if (o == null) return "";
        Gson gson = new Gson();
        String str = gson.toJson(o);
        return str;
    }

    public static ArrayList<SystemType> list2ArrayList(List<SystemType> list) {
        ArrayList<SystemType> array = new ArrayList<SystemType>();
        for (int i = 0; i < list.size(); i++) {
            array.add(list.get(i));
        }
        return array;
    }
}
