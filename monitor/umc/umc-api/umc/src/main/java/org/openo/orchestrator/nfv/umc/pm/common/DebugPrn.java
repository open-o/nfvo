package org.openo.orchestrator.nfv.umc.pm.common;


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
