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
package org.openo.nfvo.monitor.umc.pm.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugPrn{

	private static Logger LOGGER;
	public DebugPrn(String className)
	{
		LOGGER = LoggerFactory.getLogger(className);
	};
	public void debug(String msg)
	{
		LOGGER.debug(msg);
	}
	public void debug(String msg, Throwable t)
	{
		LOGGER.debug(msg, t);
	}
	public void info(String msg)
	{
		LOGGER.info(msg);
	}
	public void info(String msg, Throwable t)
	{
		LOGGER.info(msg, t);
	}
	public void warn(String msg)
	{
		LOGGER.warn(msg);
	}
	public void warn(Throwable t)
	{
		LOGGER.warn("", t);
	}
	public void warn(String msg, Throwable t)
	{
		LOGGER.warn(msg, t);
	}
	public void error(String msg)
	{
		LOGGER.error(msg);
	}
	public void error(String msg, Throwable t)
	{
		LOGGER.error(msg, t);
	}
	
}
