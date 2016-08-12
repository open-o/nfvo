/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.drill.wrapper.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 10188044
 * @date 2015-8-17
 *       <p/>
 *       Array Utils used in the drill module
 */
public class ArrayUtils {

    /**
     * check whether the given array is empty (null or length equals 0)
     *
     * @param array:the given array
     */
    public static boolean isArrayEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * check whether the given array is not empty (not null and length great than 0)
     *
     * @param array the given array
     */
    public static boolean isArrayNotEmpty(Object[] array) {
        return (array != null && array.length > 0);
    }

    /**
     * convert the S[] to T[], provided that there is a constructor method in T using the parameter
     * S
     *
     * @param source the original array
     * @param sourceType the original array type
     * @param targetType the result arrsy type
     * @return the target array T[]
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public static <S, T> T[] convertArrayType(S[] source, Class<S> sourceType, Class<T> targetType)
            throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (isArrayEmpty(source)) {
            return (T[]) Array.newInstance(targetType, 0);
        } else {
            Constructor<T> targetConstructor = targetType.getConstructor(new Class[] {sourceType});
            T[] target = (T[]) Array.newInstance(targetType, source.length);
            for (int i = 0; i < source.length; i++) {
                T targetObj = targetConstructor.newInstance(new Object[] {source[i]});
                target[i] = targetObj;
            }
            return target;
        }
    }

    /**
     * copy from common-lang3:ArrayUtils combine several arrays into one array
     *
     * @param array1 one array
     * @param array2 several arrays
     * @return combined array
     */
    public static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked")
        // OK, because array is of type T
        final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because: - it would be a wasted check
             * most of the time - safer, in case check turns out to be too strict
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName()
                        + " in an array of " + type1.getName(), ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
