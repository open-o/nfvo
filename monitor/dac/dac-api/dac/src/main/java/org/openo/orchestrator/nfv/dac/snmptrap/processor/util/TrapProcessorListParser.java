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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.openo.orchestrator.nfv.dac.common.util.filescan.FastFileSystem;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.TrapProcessor;
import org.openo.orchestrator.nfv.dac.snmptrap.util.TrapConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrapProcessorListParser
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapProcessorListParser.class);

    private static final String XMLFILE = "*-trapfilter-process.xml";

    private static TrapProcessorListParser instance = null;

    private static TrapProcessor trapProcessor = null;

    private TrapProcessorListParser()
    {
    }

    public static TrapProcessorListParser getInstance()
    {
        if (instance == null)
        {
            instance = new TrapProcessorListParser();
        }
        return instance;
    }

    public TrapProcessor getTrapProcessor()
    {
        if (trapProcessor == null)
        {
            List<ProcessorWrapper> processorList = loadXmlFile();
            trapProcessor = sortProcessor(processorList);
        }
        return trapProcessor;
    }

    /**
     * ���ض�ȡ�����ļ�
     * @return
     */
    private List<ProcessorWrapper> loadXmlFile()
    {
        List<ProcessorWrapper> processorList = new ArrayList<ProcessorWrapper>();
        try
        {
            File[] files = FastFileSystem.getFiles(XMLFILE);
            for (File file : files)
            {
	            Element rootElement = TrapConfUtil.getElementFromXmlFile(file);
	            Element trapProcessorListElement = rootElement.getChild(XmlTags.TRAP_PROCESSOR_LIST);
	            if (trapProcessorListElement != null)
	            {
	                List processors = trapProcessorListElement.getChildren(XmlTags.PROCESSOR);
	                for (int i = 0, s = processors.size(); i < s; i++)
	                {
	                    ProcessorWrapper pw = parseProcessor((Element) processors.get(i));
	                    processorList.add(pw);
	                }
	            }
            }
        }
        catch (IOException e)
        {
            LOGGER.error(XMLFILE + " not valid.", e);
        }
        catch (JDOMException ex)
        {
            LOGGER.error(XMLFILE + " not valid.", ex);
        }
        return processorList;
    }

    /**
    * sort trap processor and return the first one
    * @param processorList
    */
    private TrapProcessor sortProcessor(List<ProcessorWrapper> processorList)
    {
        ProcessorWrapper[] processorWrapers = new ProcessorWrapper[processorList.size()];
        processorList.toArray(processorWrapers);
        Arrays.sort(processorWrapers);
        for (int i = 0; i < processorWrapers.length - 1; i++)
        {
            TrapProcessor processor = processorWrapers[i].getProcessor();
            TrapProcessor next = processorWrapers[i + 1].getProcessor();
            processor.setNext(next);
        }
        return processorWrapers[0].getProcessor();
    }

    private static ProcessorWrapper parseProcessor(Element eleProcessor)
    {
        String errorHint1 = "class: ";
        String errorHint2 = " not found";
        String errorHint3 = "cant get instance";
        String className = TrapConfUtil.getElementValue(eleProcessor, XmlTags.CLASS);
        String indexStr = TrapConfUtil.getElementValue(eleProcessor, XmlTags.INDEX);
        try
        {
            Class processorClass = Class.forName(className);
            TrapProcessor processor = (TrapProcessor) processorClass.newInstance();
            ProcessorWrapper wrapper = new ProcessorWrapper(processor);
            int index = 1000;
            if (indexStr != null && !indexStr.trim().equals(""))
            {
                index = Integer.parseInt(indexStr.trim());
            }
            wrapper.setIndex(index);
            return wrapper;
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error(errorHint1 + className + errorHint2, e);
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error(errorHint1 + className + errorHint2, e);
        }
        catch (InstantiationException e)
        {
            LOGGER.error(errorHint1 + className + errorHint3, e);
        }
        return null;
    }

    private static class XmlTags
    {

        public static String TRAP_PROCESSOR_LIST = "TrapProcessorList";

        public static String PROCESSOR = "Processor";

        public static String CLASS = "class";

        /**
         * processor sort index
         */
        public static String INDEX = "index";
    }

    private static class ProcessorWrapper implements Comparable
    {
        private TrapProcessor processor = null;

        private int index = 0;

        public ProcessorWrapper(TrapProcessor pprocessor)
        {
            processor = pprocessor;
        }

        public TrapProcessor getProcessor()
        {
            return processor;
        }

        public void setIndex(int pindex)
        {
            this.index = pindex;
        }

        public int compareTo(Object o)
        {
            return new Integer(index).compareTo(new Integer(((ProcessorWrapper) o).index));
        }
    }

}
