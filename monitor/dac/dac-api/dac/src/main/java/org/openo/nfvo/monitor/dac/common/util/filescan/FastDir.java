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
package org.openo.nfvo.monitor.dac.common.util.filescan;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class FastDir implements Serializable{
	
	private static final long serialVersionUID = 1770274579888614564L;
	private String dir=null;
	private Map<String,String> subFiles=new HashMap<String,String>();
	private Map<String,String> subignoreCaseFiles=new HashMap<String,String>();
	private Map<String,List<String>> commSearchFileMap=new HashMap<String, List<String>>();
	
	private List<FastDir> subDirs=new ArrayList<FastDir>();
	private static Logger logger = Logger.getLogger(FastDir.class);
	private static Map<String,String> COMM_FILE_NAMES=new HashMap<String,String>();
	static{
		COMM_FILE_NAMES.put("*-extendsdesc.xml", "*-extendsdesc.xml");
		COMM_FILE_NAMES.put("*-extendsimpl.xml", "*-extendsimpl.xml");
		COMM_FILE_NAMES.put("*-monitor-map.xml", "*-monitor-map.xml");
		COMM_FILE_NAMES.put("*.mib", "*.mib");
	}
	
	
	private static List<String> COMM_FILE_SUFFIXS=new ArrayList<String>();
	static{
		COMM_FILE_SUFFIXS.add("-extendsdesc.xml");
		COMM_FILE_SUFFIXS.add("-extendsimpl.xml");
		COMM_FILE_SUFFIXS.add("-monitor-map.xml");
		COMM_FILE_SUFFIXS.add(".mib");
	}
	
	FastDir(File dir){ 
		this.dir=dir.getAbsolutePath();
	}
	
	

	public File getDir() {
		return new File(dir);
	}
	public Map<String, String> getSubFiles() {
		return subFiles;
	}
	public List<FastDir> getSubDirs() {
		return subDirs;
	}
	
	public void addSubDir(FastDir subDir){
		subDirs.add(subDir);
	}
	
	
	public void addSubFile(String path, File subFile){
		//Ŀ¼���Ǿ�Եģ�ѹ���ڴ�ʹ��
		subFiles.put(path, subFile.getName());
		subignoreCaseFiles.put(path.toLowerCase(), subFile.getName());
		for(String suffix:COMM_FILE_SUFFIXS){
			if(path.endsWith(suffix)){
				List<String> subFiles=commSearchFileMap.get("*"+suffix);
				if(subFiles==null){
					subFiles=new ArrayList<String>();
					commSearchFileMap.put("*"+suffix, subFiles);
				}
				subFiles.add(subFile.getName());
			}
		}
		
		
	}
	
	
	public List<File> filterFiles(FileFilter filter){
		
		
		if(filter instanceof ExtendedFileFilter){
			ExtendedFileFilter extFilter=(ExtendedFileFilter)filter;
			StringPattern strPattern=extFilter.getStringPattern();
			String pattern=null;
			if(strPattern.hasIgnoreCase()){
				pattern=strPattern.getPattern().toLowerCase();
				
			}else{
				 pattern=strPattern.getPattern();
			}
			if(COMM_FILE_NAMES.containsKey(pattern)){
				List<String> subFiles=commSearchFileMap.get(pattern);
				return buildResultFile(subFiles);
			}
			
			List<File> tmpList=new ArrayList<File>();
			Collection<String> tmpFiles=subFiles.values();
			for(String tmpFile:tmpFiles){
				if(strPattern.matches(tmpFile)){
					tmpList.add(new File(dir+File.separator+tmpFile));
				}
			}
			return tmpList;
		}
		
		List<File> tmpList=new ArrayList<File>();
		Collection<String> tmpFiles=subFiles.values();
		for(String tmpFile:tmpFiles){
			File tempFile=new File(dir+File.separator+tmpFile);
			if(filter.accept(tempFile)){
				tmpList.add(tempFile);
			}
		}
		return tmpList;
	}
	
	
	
	
	private List<File> buildResultFile(List<String> subFiles) {
		if(subFiles==null){
			return null;
		}
		List<File> tmpList=new ArrayList<File>();
		for(String fileName:subFiles){
			tmpList.add(new File(dir+File.separator+fileName));
		}
		return tmpList;
	}



	public File getFileByName(String fileName, boolean ignoreCase){
		if(ignoreCase){
			if(subignoreCaseFiles.get(fileName.toLowerCase())==null){
				return null;
			}
			return new File(dir+File.separator+subignoreCaseFiles.get(fileName.toLowerCase())) ;
		}else{
			if(subFiles.get(fileName)==null){
				return null;
			}
			return new File(dir+File.separator+subFiles.get(fileName));
		}
	}
	
	
	
	
	public String toString(String prefix) {
		
		StringBuffer buffer=new StringBuffer();
		
		buffer.append(dir);
		buffer.append("\n");
		buffer.append(prefix);
		buffer.append("  +");
		Collection<String> subFileSet=subFiles.values();
		for(String subFile:subFileSet){
			buffer.append(subFile);
			buffer.append("\n");
			buffer.append(prefix);
			buffer.append("  +");
		}
		
		for(FastDir subDir:subDirs){
			//buffer.append(subDir.getDir().getName());
			buffer.append("\n");
			buffer.append(prefix);
			buffer.append("  +");
			buffer.append(subDir.toString(prefix+"  "));
		}
		
		return buffer.toString();
	}
	@Override
    public String toString() {
		
		StringBuffer buffer=new StringBuffer();
		
		buffer.append(dir);
		buffer.append("\n  +");
		Collection<String> subFileSet=subFiles.values();
		for(String subFile:subFileSet){
			buffer.append(subFile);
			buffer.append("\n +");
		}
		
		for(FastDir subDir:subDirs){
			buffer.append(subDir.getDir());
			buffer.append("\n  +");
			buffer.append(subDir.toString("  "));
		}
		
		return buffer.toString();
	}
	
	
}
