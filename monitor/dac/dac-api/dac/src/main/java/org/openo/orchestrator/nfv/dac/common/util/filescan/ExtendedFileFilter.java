package org.openo.orchestrator.nfv.dac.common.util.filescan;

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
