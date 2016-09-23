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
package org.openo.nfvo.monitor.umc.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openo.nfvo.monitor.umc.UMCApp;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjiangping
 * @date 2016/6/27 8:57:33
 * @description 
 */
public class ExtensionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionUtil.class);


    private static Map<String, Map<String, List<Object>>> singletonInstanceMap;
    private static Map<String,  Map<String, List<Class<?>>>> extensionClassMap;
    
    private static Reflections reflections;

    /**
     * @author wangjiangping
     * @date 2016/6/27 15:10:12
     * @description Build up key-MappingClass rules when server start up.
     */
    public static void init(String[] packageUrls) {
       
        singletonInstanceMap = new HashMap<String, Map<String, List<Object>>>();
        extensionClassMap = new HashMap<String, Map<String, List<Class<?>>>>();

        for (String packageUrl : packageUrls) {
            reflections = new Reflections(packageUrl);
            scanExtensionImpl();
        }
    }

	private static void scanExtensionImpl()  {
        Set<Class<?>> providersClasses = reflections.getTypesAnnotatedWith(ExtensionImpl.class);

        if (!providersClasses.isEmpty()) {
            for (Class<?> classEntity : providersClasses) {
            	injectInstance(classEntity);  
            }
        }

    }

    private static void injectInstance(Class<?> classEntity)
    {
    	 try {
    		if (!Modifier.isAbstract(classEntity.getModifiers()))
    		{
    			ExtensionImpl ExtAnnotations = classEntity.getAnnotation(ExtensionImpl.class );
    	        for(String key : ExtAnnotations.keys())
    	        {
    	            ExtensionUtil.addExtensionImpl(ExtAnnotations.entensionId(), key, classEntity, ExtAnnotations.isSingleton());
    	        }
    		}
		} catch (SecurityException | IllegalArgumentException
				| InstantiationException | IllegalAccessException e) {
			LOGGER.warn("Init class failed,classname:" + classEntity.getName(), e);
		}  
    }
    
    public static void addExtensionImpl(String extensionId, String key, Class<?> classEntity, boolean isSingleton) throws InstantiationException, IllegalAccessException {
    	if (isSingleton)
    	{
    		putInstanceToMap(extensionId, key, classEntity.newInstance());
    	}
    	else
    	{
    		putClassToMap(extensionId, key, classEntity);
    	}
    }

    private static void putClassToMap(String extensionId, String key, Class<?> className)
    {
    	Map<String, List<Class<?>>> map;
    	if (!extensionClassMap.containsKey(extensionId))
    	{
    		 map = new  HashMap<String, List<Class<?>>>();
    		 extensionClassMap.put(extensionId, map);
    	}
    	else
    	{
    		map = extensionClassMap.get(extensionId);
    	}
    	List<Class<?>> classList;
    	if (map.containsKey(key))
    	{
    		classList = map.get(key);
    	}
    	else
    	{
    		classList = new ArrayList<Class<?>>();
    		map.put(key, classList);
    	}
    	classList.add(className);
    }
    
    private static void putInstanceToMap(String extensionId, String key, Object instance)
    {
    	Map<String, List<Object>> map;
    	if (!singletonInstanceMap.containsKey(extensionId))
    	{
    		 map = new  HashMap<String, List<Object>>();
    		 singletonInstanceMap.put(extensionId, map);
    	}
    	else
    	{
    		map = singletonInstanceMap.get(extensionId);
    	}
    	List<Object> classList;
    	if (map.containsKey(key))
    	{
    		classList = map.get(key);
    	}
    	else
    	{
    		classList = new ArrayList<Object>();
    		map.put(key, classList);
    	}
    	classList.add(instance);
    }
    
    public static Object getInstance(String extensionId, String key) {
    	Object[] instances = getInstances(extensionId, key);
    	if (instances.length > 0)
    	{
    		return instances[0];
    	}
    	else
    	{
    		return null;
    	}
    }
    
	public static Object[] getInstances(String extensionId, String key) {
		List<Object> instances = new ArrayList<Object>();
		if (singletonInstanceMap.containsKey(extensionId))
		{
			Map<String, List<Object>> instancesMap = singletonInstanceMap.get(extensionId);
			if (instancesMap.containsKey(key))
			{
				List<Object> objects = instancesMap.get(key);
				for(Object obj : objects)
				{
					instances.add((Object)obj);
				}
			}
		}
		if (extensionClassMap.containsKey(extensionId))
		{
			Map<String, List<Class<?>>> classesMap = extensionClassMap.get(extensionId);
			if (classesMap.containsKey(key))
			{
				List<Class<?>> classNameList = new ArrayList<Class<?>>();
				classNameList.addAll(classesMap.get(key));
				for (Class<?> oneClass : classNameList)
				{
					try {
						instances.add(oneClass.newInstance());
						LOGGER.debug("create newInstance:" + oneClass.getName());
					} catch (InstantiationException | IllegalAccessException e) {
						LOGGER.warn("getPmNafDataProcesses failed, ID=" + key, e);
					}
				}
				
			}
		}
		return instances.toArray(new Object[0]);
	}
    
    /**
     * for test.
     */
    public static void main(String[] args) {
        String[] packageUrls =
                new String[] {UMCApp.class.getPackage().getName(), "com.zte.ums.zenap.umc.ext"};
        init(packageUrls);
        
        System.out.println(extensionClassMap.keySet());
        System.out.println(extensionClassMap.entrySet());
    }
}
