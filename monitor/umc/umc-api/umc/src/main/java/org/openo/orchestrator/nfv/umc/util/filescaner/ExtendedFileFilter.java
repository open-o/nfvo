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
package org.openo.orchestrator.nfv.umc.util.filescaner;

import java.io.File;
import java.io.FileFilter;

public class ExtendedFileFilter implements FileFilter {
	

	private StringPattern stringPattern = null;

	/**
	 * ���캯��
	 * 
	 * @param pattern
	 * @param ignoreCase
	 */
	public ExtendedFileFilter(String pattern, boolean ignoreCase) {
		stringPattern = createStringPattern(pattern, ignoreCase);
	}

	// -------------------------------------------------------------------------

	

	/**
	 * Tests if a specified file should be included in a file list.
	 * 
	 * @param filePath
	 *            the directory in which the file was found.
	 * @return true if and only if the name should be included in the file list,
	 *         false otherwise.
	 */
	public boolean accept(File filePath) {
		return filePath.isDirectory() || stringPattern.matches(filePath.getName());
	} // accept()

	/**
	 * Tests if a specified file should be included in a file list.
	 * 
	 * @param filePath
	 *            the directory in which the file was found.
	 * @param dir
	 * @return true if and only if the name should be included in the file list,
	 *         false otherwise.
	 * @JTIUP
	 */
	public boolean accept(File filePath, boolean dir) {
		return stringPattern.matches(filePath.getName());
	} // accept()

	// -------------------------------------------------------------------------
	/**
	 * @param pattern
	 * @param ignoreCase
	 * @return
	 */
	protected StringPattern createStringPattern(String pattern, boolean ignoreCase) {
		StringPattern strPattern = new StringPattern(pattern, ignoreCase);
		strPattern.multiCharWildcardMatchesEmptyString(true);
		return strPattern;
	}

	
	public StringPattern getStringPattern() {
		return stringPattern;
	}
	
	@Override
	public String toString() {
		return "ExtendedFileFilter [stringPattern=" + stringPattern + "]:["+stringPattern.hasIgnoreCase()+"]";
	}

}
