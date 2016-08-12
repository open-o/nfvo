/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.dac.snmptrap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.jsmiparser.parser.SmiDefaultParser;
import org.jsmiparser.phase.file.FileParserPhase;
import org.jsmiparser.smi.SmiMib;
import org.jsmiparser.smi.SmiObjectType;
import org.jsmiparser.smi.SmiSymbolMap;
import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.util.filescan.FastFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * load mib file, cache leaf information to parse trap bind value
 */
public class MibFileResolver
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MibFileResolver.class);

    private static MibFileResolver instance = null;
//    private MibUtil mibUtil;
    private SmiDefaultParser parser;
    private Vector<File> vecMibs;
//    private StringBuffer vecMibPaths;
    private Vector<File> mibFiles;
//    private MibNodeInfo nodeInfo;
    private String serializedFile;
    private Vector<String> modules;

    private MibFileResolver(String serializedFilePath)
    {
//        vecMibPaths = new StringBuffer();
//    	mibUtil = new MibUtil();
        vecMibs = new Vector<File>();
        mibFiles = new Vector<File>();
//        nodeInfo = new MibNodeInfo();
        modules = new Vector<String>();

        serializedFile = serializedFilePath;

        if (parser == null)
        {
        	parser = new SmiDefaultParser();
        }
    }

    public static MibFileResolver getInstance(String serializedFilePath)
    {
        if (instance == null)
        {
            instance = new MibFileResolver(serializedFilePath);
            instance.loadMibFiles();
        }
        return instance;
    }

    private Vector getCurMibFiles()
    {
    	 File[] files = FastFileSystem.getFiles(DacConst.MIBFILES);
    	 mibFiles.addAll(Arrays.asList(files));
    	 return mibFiles;
    }

    private static boolean isNewFileAdd(ArrayList<String> oldFiles, Vector<File> newFiles)
    {
    	for (File file : newFiles)
    	{
    		if (!oldFiles.contains(file.getName()))
    		{
    			return true;
    		}
    	}
    	return false;
    }

    private void loadMibFiles()
    {
    	Yaml yaml = new Yaml();
        File serFile = new File(serializedFile);
        if (serFile.exists())
        {
            try
            {
            	MibUtil.setNodeInfo(yaml.loadAs(new FileInputStream(serializedFile), MibNodeInfo.class));

            }
            catch (FileNotFoundException e)
            {
            	LOGGER.warn(serializedFile + "is not found. " + e);
            }
        }

        vecMibs = getCurMibFiles();
        if (isNewFileAdd(MibUtil.getLoadedMibModule(), vecMibs))
        {
        	ArrayList<URL> urlList = new ArrayList<URL>();
			for (File file : vecMibs) {
				try {
					if (!isMibModuleLoaded(file.getName()))
					{
						urlList.add(file.toURI().toURL());
						MibUtil.setLoadedMibModule(file.getName());
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			SmiDefaultParser parser = new SmiDefaultParser();
			FileParserPhase fp = parser.getFileParserPhase();
			fp.setInputUrls(urlList);
			SmiMib mib = parser.parse();
			SmiSymbolMap<SmiObjectType> objectMap = mib.getObjectTypes();
			Collection<SmiObjectType> list = objectMap.getAll();
			Iterator<SmiObjectType> iterator = list.iterator();
			while (iterator.hasNext()) {
				SmiObjectType objectType = iterator.next();
				if (isLeaf(objectType))
				{
					MibUtil.addNodeInfo(objectType.getOidStr(), objectType.getId());
				}
			}

            try
            {
                yaml.dump(MibUtil.getNodeInfo(), new FileWriter(serializedFile));
            }
            catch (Exception e)           {
            	LOGGER.warn("Saving serialized mib file error. " + e);
            }
//            finally
//            {
//                if (out != null)
//                {
//                    try
//                    {
//                        out.close();
//                    }
//                    catch (IOException e)
//                    {
//                    	LOGGER.warn("IOException. " + e);
//                    }
//                    out = null;
//                }
//            }
        }
    }

    private boolean isMibModuleLoaded(String name) {
    	name = name.toUpperCase();
    	name = name.split("\\.")[0];
		if (modules.contains(name))
		{
			return true;
		}
		else
		{
			modules.add(name);
			return false;
		}

	}

    private boolean isLeaf(SmiObjectType objectType)
    {
    	return objectType.getNode().getChildren().size() == 0;
    }
}
