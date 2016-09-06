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
package org.openo.orchestrator.nfv.dac.common.util.filescan;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 * @JTIUSN
 */

public class StringExaminer extends StringScanner {
	// =========================================================================
	// CONSTANTS
	// =========================================================================

	// =========================================================================
	// INSTANCE VARIABLES
	// =========================================================================
	private boolean ignoreCase = false;

	// private static final int buffSize = 100;

	/**
	 * @return
	 */
	protected boolean hasIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * @param newValue
	 */
	protected void ignoreCase(boolean newValue) {
		ignoreCase = newValue;
	}

	// =========================================================================
	// CLASS METHODS
	// =========================================================================

	// =========================================================================
	// CONSTRUCTORS
	// =========================================================================
	/**
	 * Initialize the new instance with the string to examine. <br>
	 * The string will be treated case-sensitive.
	 * 
	 * @param stringToExamine
	 *            The string that should be examined
	 */
	public StringExaminer(String stringToExamine) {
		this(stringToExamine, false);
	} // StringExaminer()

	// -------------------------------------------------------------------------

	/**
	 * Initialize the new instance with the string to examine.
	 * 
	 * @param stringToExamine
	 *            The string that should be examined
	 * @param ignoreCase
	 *            Specified whether or not treating the string case insensitive
	 */
	public StringExaminer(String stringToExamine, boolean _ignoreCase) {
		super(stringToExamine);
		ignoreCase(_ignoreCase);
	} // StringExaminer()

	// -------------------------------------------------------------------------

	// =========================================================================
	// PUBLIC INSTANCE METHODS
	// =========================================================================
	/**
	 * Increments the position pointer up to the last character that matched the
	 * character sequence in the given matchString. Returns true, if the
	 * matchString was found, otherwise false.
	 * <p>
	 * If the matchString was found, the next invocation of method nextChar()
	 * returns the first character after that matchString.
	 * 
	 * @param matchString
	 *            The string to look up
	 * @return
	 */
	public boolean skipAfter(String matchString) {
		char ch = '-';
		char matchChar = ' ';
		boolean found = false;
		int index = 0;

		if ((matchString == null) || (matchString.length() == 0)) {
			return false;
		}

		ch = nextChar();
		while ((endNotReached(ch)) && (!found)) {
			matchChar = matchString.charAt(index);
			if (charsAreEqual(ch, matchChar)) {
				index++;
				if (index >= matchString.length())
				// whole matchString checked ?
				{
					found = true;
				}
				else {
					ch = nextChar();
				}
			}
			else {
				if (index == 0) {
					ch = nextChar();
				}
				else {
					index = 0;
				}
			}
		}
		return found;
	} // skipAfter()

	// -------------------------------------------------------------------------

	/**
	 * Increments the position pointer up to the first character before the
	 * character sequence in the given matchString. Returns true, if the
	 * matchString was found, otherwise false.
	 * <p>
	 * If the matchString was found, the next invocation of method nextChar()
	 * returns the first character of that matchString from the position where
	 * it was found inside the examined string.
	 * 
	 * @param matchString
	 *            The string to look up
	 * @return
	 */
	public boolean skipBefore(String matchString) {
		boolean found = false;

		found = skipAfter(matchString);
		if (found) {
			skip(0 - matchString.length());
		}

		return found;
	} // skipBefore()

	// -------------------------------------------------------------------------

	/**
	 * Returns the a string containing all characters from the current position
	 * up to the end of the examined string. <br>
	 * The character position of the examiner is not changed by this method.
	 * 
	 * @return
	 */
	public String peekUpToEnd() {
		return upToEnd(true);
	} // peekUpToEnd()

	// -------------------------------------------------------------------------

	/**
	 * Returns the a string containing all characters from the current position
	 * up to the end of the examined string. <br>
	 * The character position is put to the end by this method. That means the
	 * next invocation of nextChar() returns END_REACHED.
	 * 
	 * @return
	 */
	public String upToEnd() {
		return upToEnd(false);
	} // upToEnd()

	// -------------------------------------------------------------------------

	// =========================================================================
	// PROTECTED INSTANCE METHODS
	// =========================================================================
	/**
	 * @param char1
	 * @param char2
	 * @return
	 */
	protected boolean charsAreEqual(char char1, char char2) {
		return (hasIgnoreCase()) ? (Character.toUpperCase(char1) == Character.toUpperCase(char2)) : (char1 == char2);
	} // charsAreEqual()

	// -------------------------------------------------------------------------

	/**
	 * Returns the a string containing all characters from the current position
	 * up to the end of the examined string. <br>
	 * Depending on the peek flag the character position of the examiner is
	 * unchanged (true) after calling this method or points behind the strings
	 * last character.
	 * 
	 * @return
	 */
	protected String upToEnd(boolean peek) {
		char result = '-';
		int lastPosition = 0;
		// final int buffSize = 100;
		StringBuffer buffer = new StringBuffer(100);

		lastPosition = getPosition();
		result = nextChar();
		while (endNotReached(result)) {
			buffer.append(result);
			result = nextChar();
		}
		if (peek) {
			setPosition(lastPosition);
		}

		return buffer.toString();
	} // upToEnd()

	// -------------------------------------------------------------------------

}
