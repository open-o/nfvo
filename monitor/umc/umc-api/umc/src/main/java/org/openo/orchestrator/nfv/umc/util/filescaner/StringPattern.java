package org.openo.orchestrator.nfv.umc.util.filescaner;

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
 * @JTIUSN
 * @author not attributable
 * @version 1.0
 */

public class StringPattern {
	// =========================================================================
	// CONSTANTS
	// =========================================================================
	protected final static String MULTI_WILDCARD = "*";

	protected final static char MULTICHAR_WILDCARD = '*';

	protected final static char SINGLECHAR_WILDCARD = '?';

	protected final static String SWITCH_ON = "+";

	protected final static String SWITCH_OFF = "-";

	private static final String INSPECT_PREFIX = "StringPattern(";

	private static final String INSPECT_SEPARATOR = ",\"";

	private static final String INSPECT_SUFFIX = "\")";

	// =========================================================================
	// INSTANCE VARIABLES
	// =========================================================================
	private boolean ignoreCase = false;

	/**
	 * Returns whether or not the pattern matching ignores upper and lower case
	 * 
	 * @return
	 */
	public boolean hasIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * Sets whether the pattern matching should ignore case or not
	 * 
	 * @param newValue
	 */
	public void setIgnoreCase(boolean newValue) {
		ignoreCase = newValue;
	}

	private String pattern = null;

	/**
	 * Returns the pattern as string.
	 * 
	 * @return
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Sets the pattern to a new value
	 * 
	 * @param newValue
	 */
	public void setPattern(String newValue) {
		pattern = newValue;
	}

	// -------------------------------------------------------------------------

	private Character digitWildcard = null;

	/**
	 * @return
	 */
	protected Character getDigitWildcard() {
		return digitWildcard;
	}

	/**
	 * @param newValue
	 */
	protected void digitWildcard(Character newValue) {
		digitWildcard = newValue;
	}

	// -------------------------------------------------------------------------

	private boolean multiCharWildcardMatchesEmptyString = false;

	/**
	 * Returns true, if this StringPattern allows empty strings at the position
	 * of the multi character wildcard ('*').
	 * <p>
	 * The default value is false.
	 * 
	 * @return
	 */
	public boolean hasMultiCharWildcardMatchesEmptyString() {
		return multiCharWildcardMatchesEmptyString;
	}

	/**
	 * sets whether or not this StringPattern allows empty strings at the
	 * position of the multi character wildcard ('*').
	 * <p>
	 * The default value is false.
	 * 
	 * @param newValue
	 */
	public void multiCharWildcardMatchesEmptyString(boolean newValue) {
		multiCharWildcardMatchesEmptyString = newValue;
	}

	// =========================================================================
	// CLASS METHODS
	// =========================================================================
	/**
	 * Returns true, if the given probe string matches the given pattern. <br>
	 * The character comparison is done case sensitive.
	 * 
	 * @param probe
	 *            The string to check against the pattern.
	 * @param pattern
	 *            The patter, that probably contains wildcards ( '*' or '?' )
	 * @return
	 */
	public static boolean match(String probe, String _pattern) {
		StringPattern stringPattern = new StringPattern(_pattern, false);
		return stringPattern.matches(probe);
	} // match()

	// -------------------------------------------------------------------------

	/**
	 * Returns true, if the given probe string matches the given pattern. <br>
	 * The character comparison is done ignoring upper/lower-case.
	 * 
	 * @param probe
	 *            The string to check against the pattern.
	 * @param pattern
	 *            The patter, that probably contains wildcards ( '*' or '?' )
	 * @return
	 */
	public static boolean matchIgnoreCase(String probe, String _pattern) {
		StringPattern stringPattern = new StringPattern(_pattern, true);
		return stringPattern.matches(probe);
	} // matchIgnoreCase()

	// -------------------------------------------------------------------------

	// =========================================================================
	// CONSTRUCTORS
	// =========================================================================
	/**
	 * Initializes the new instance with the string pattern and the selecteion,
	 * if case should be ignored when comparing characters.
	 * 
	 * @param pattern
	 *            The pattern to check against ( May contain '*' and '?'
	 *            wildcards )
	 * @param ignoreCase
	 *            Definition, if case sensitive character comparison or not.
	 */
	public StringPattern(String pattern, boolean ignoreCase) {
		this.setPattern(pattern);
		this.setIgnoreCase(ignoreCase);
	} // StringPattern()

	// -------------------------------------------------------------------------

	/**
	 * Initializes the new instance with the string pattern. The default is case
	 * sensitive checking.
	 * 
	 * @param pattern
	 *            The pattern to check against ( May contain '*' and '?'
	 *            wildcards )
	 */
	public StringPattern(String _pattern) {
		this(_pattern, false);
	} // StringPattern()

	// -------------------------------------------------------------------------

	/**
	 * Initializes the new instance with the string pattern and a digit wildcard
	 * character. The default is case sensitive checking.
	 * 
	 * @param pattern
	 *            The pattern to check against ( May contain '*', '?' wildcards
	 *            and the digit wildcard )
	 * @param digitWildcard
	 *            A wildcard character that stands as placeholder for digits
	 */
	public StringPattern(String _pattern, char _digitWildcard) {
		this(_pattern, false, _digitWildcard);
	} // StringPattern()

	// -------------------------------------------------------------------------

	/**
	 * Initializes the new instance with the string pattern and the selecteion,
	 * if case should be ignored when comparing characters plus a wildcard
	 * character for digits.
	 * 
	 * @param pattern
	 *            The pattern to check against ( May contain '*' and '?'
	 *            wildcards )
	 * @param ignoreCase
	 *            Definition, if case sensitive character comparison or not.
	 * @param digitWildcard
	 *            A wildcard character that stands as placeholder for digits
	 */
	public StringPattern(String _pattern, boolean _ignoreCase, char _digitWildcard) {
		setPattern(_pattern);
		setIgnoreCase(_ignoreCase);
		setDigitWildcardChar(_digitWildcard);
	} // StringPattern()

	// -------------------------------------------------------------------------

	// =========================================================================
	// PUBLIC INSTANCE METHODS
	// =========================================================================

	/**
	 * Tests if a specified string matches the pattern.
	 * 
	 * @param probe
	 *            The string to compare to the pattern
	 * @return true if and only if the probe matches the pattern, false
	 *         otherwise.
	 */
	public boolean matches(String probe) {
		StringExaminer patternIterator = null;
		StringExaminer probeIterator = null;
		char patternCh = '-';
		char probeCh = '-';
		String newPattern = null;
		String subPattern = null;
		int charIndex = 0;

		if (probe == null) {
			return false;
		}
		if (!this.hasMultiCharWildcardMatchesEmptyString()) {
			if (probe.length() == 0) {
				return false;
			}
		}

		patternIterator = this.newExaminer(this.getPattern());
		probeIterator = this.newExaminer(probe);

		probeCh = probeIterator.nextChar();
		patternCh = getPatternChar(patternIterator, probeCh);

		while ((endNotReached(patternCh)) && (endNotReached(probeCh))) {

			if (patternCh == MULTICHAR_WILDCARD) {
				patternCh = skipWildcards(patternIterator);
				if (endReached(patternCh)) {
					return true;
					// No more characters after multi wildcard - So everything
					// matches
				}
				else {
					patternIterator.skip(-1);
					// Position to first non-wildcard character
					newPattern = upToEnd(patternIterator);
					charIndex = newPattern.indexOf(MULTICHAR_WILDCARD);
					if (charIndex >= 0) {
						// Pattern contains another multi-char wildcard ?
						subPattern = newPattern.substring(0, charIndex);

						if (this.hasMultiCharWildcardMatchesEmptyString()) {
							probeIterator.skip(-1);

						}
						if (this.skipAfter(probeIterator, subPattern)) {
							patternIterator = this.newExaminer(newPattern.substring(charIndex));
							patternCh = probeCh;
							// To get it through the comparison at the end of
							// this method
						}
						else {
							// The substring up to the multi-value wildcard
							// doesn't match
							// the next characters in the probe
							return false;
						}
					}
					else { // No more multi-char wildcard in pattern
						probeIterator.skip(-1);
						return this.matchReverse(newPattern, probeIterator);
					}
				}
			}

			if (charsAreEqual(probeCh, patternCh)) {
				if (endNotReached(patternCh)) {
					probeCh = probeIterator.nextChar();
					patternCh = getPatternChar(patternIterator, probeCh);
				}
			}
			else {
				if (patternCh != MULTICHAR_WILDCARD) {
					return false;
					// character is not matching - return immediately
				}
			}
		} // while()

		return eventuallyMatched(probeCh, patternCh);
	} // matches()

	// -------------------------------------------------------------------------

	/**
	 * Returns the pattern string.
	 * 
	 * @see java.lang.Object#toString()
	 * @return
	 */
	public String toString() {
		if (getPattern() == null) {
			return super.toString();
		}
		else {
			return getPattern();
		}
	} // toString()

	// -------------------------------------------------------------------------

	/**
	 * Returns true if the pattern contains any '*' or '?' or digit wildcard
	 * wildcard character.
	 * 
	 * @return
	 */
	public boolean hasWildcard() {
		if (getPattern() == null) {
			return false;
		}

		if (hasDigitWildcard()) {
			if (getPattern().indexOf(digitWildcardChar()) >= 0) {
				return true;
			}
		}

		return (getPattern().indexOf(MULTI_WILDCARD) >= 0) || (getPattern().indexOf(SINGLECHAR_WILDCARD) >= 0);
	} // hasWildcard()

	// -------------------------------------------------------------------------

	/**
	 * Sets the given character as a wildcard character in this pattern to match
	 * only digits ('0'-'9'). <br>
	 * 
	 * @param digitWildcard
	 *            The placeholder character for digits
	 */
	public void setDigitWildcardChar(char _digitWildcard) {
		if (_digitWildcard <= 0) {
			digitWildcard(null);
		}
		else {
			digitWildcard(new Character(_digitWildcard));
		}
	} // setDigitWildcardChar()

	// -------------------------------------------------------------------------

	/**
	 * Returns true if the given object is equal to the receiver.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj instanceof StringPattern) {
			StringPattern otherPattern = (StringPattern) obj;
			if (hasIgnoreCase() != otherPattern.hasIgnoreCase()) {
				return false;
			}
			return getPattern().equals(otherPattern.getPattern());
		}
		return false;
	} // equals()

	// -------------------------------------------------------------------------

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return
	 */
	public int hashCode() {
		String temp = ignoreCaseAsString() + getPattern();
		return temp.hashCode();
	} // hashCode()

	// -------------------------------------------------------------------------

	// =========================================================================
	// PROTECTED INSTANCE METHODS
	// =========================================================================
	/**
	 * @param probeCh
	 * @param patternCh
	 * @return
	 */
	protected boolean eventuallyMatched(char probeCh, char patternCh) {
		if (!endReached(probeCh)) {
			return false;
		}

		if (endReached(patternCh)) {
			return true;
		}

		if (hasMultiCharWildcardMatchesEmptyString()) {
			return patternCh == MULTICHAR_WILDCARD;
		}
		else {
			return false;
		}
	} // eventuallyMatched()

	// -------------------------------------------------------------------------
	/**
	 * @return
	 */
	protected boolean hasDigitWildcard() {
		return getDigitWildcard() != null;
	} // hasDigitWildcard()

	// -------------------------------------------------------------------------
	/**
	 * @return
	 */
	protected char digitWildcardChar() {
		if (hasDigitWildcard()) {
			return getDigitWildcard().charValue();
		}
		else {
			return '\0';
		}
	} // digitWildcardChar()

	// -------------------------------------------------------------------------

	/**
	 * Moves the iterator position to the next character that is no wildcard.
	 * Doesn't skip digit wildcards and single-char wildcards !
	 * 
	 * @param iterator
	 * @return
	 */
	protected static char skipWildcards(StringExaminer iterator) {
		char result = '-';

		do {
			result = iterator.nextChar();
		}
		while (result == MULTICHAR_WILDCARD);
		return result;
	} // skipWildcards()

	// -------------------------------------------------------------------------

	/**
	 * Increments the given iterator up to the last character that matched the
	 * character sequence in the given matchString. Returns true, if the
	 * matchString was found, otherwise false.
	 * 
	 * @param matchString
	 *            The string to be found (must not contain *)
	 * @param examiner
	 * @param matchString
	 * @return
	 */
	protected boolean skipAfter(StringExaminer examiner, String matchString) {
		// Do not use the method of StringExaminer anymore, because digit
		// wildcard
		// support is in the charsAreEqual() method which is unknown to the
		// examiner.
		// return examiner.skipAfter( matchString ) ;

		char ch = '-';
		char matchChar = ' ';
		boolean found = false;
		int index = 0;

		if ((matchString == null) || (matchString.length() == 0)) {
			return false;
		}

		ch = examiner.nextChar();
		// while ((examiner.endNotReached(ch)) && (!found)) {
		while ((StringExaminer.endNotReached(ch)) && (!found)) {
			matchChar = matchString.charAt(index);
			if (charsAreEqual(ch, matchChar)) {
				index++;
				if (index >= matchString.length()) {
					// whole matchString checked ?
					found = true;
				}
				else {
					ch = examiner.nextChar();
				}
			}
			else {
				if (index == 0) {
					ch = examiner.nextChar();
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
	 * @param iterator
	 * @return
	 */
	protected static String upToEnd(StringExaminer iterator) {
		return iterator.upToEnd();
	} // upToEnd()

	// -------------------------------------------------------------------------
	/**
	 * @param pattern
	 * @param probeIterator
	 * @return
	 */
	protected boolean matchReverse(String _pattern, StringExaminer probeIterator) {
		String newPattern = null;
		String newProbe = null;
		StringPattern newMatcher = null;

		newPattern = MULTI_WILDCARD + _pattern;
		newProbe = upToEnd(probeIterator);
		newPattern = StringUtil.reverse(newPattern);
		newProbe = StringUtil.reverse(newProbe);
		newMatcher = new StringPattern(newPattern, hasIgnoreCase());
		newMatcher.multiCharWildcardMatchesEmptyString(hasMultiCharWildcardMatchesEmptyString());
		if (hasDigitWildcard()) {
			newMatcher.setDigitWildcardChar(digitWildcardChar());

		}
		return newMatcher.matches(newProbe);
	} // matchReverse()

	// -------------------------------------------------------------------------
	/**
	 * @param probeChar
	 * @param patternChar
	 * @return
	 */
	protected boolean charsAreEqual(char probeChar, char patternChar) {
		if (patternChar == SINGLECHAR_WILDCARD) {
			return true;
		}

		if (hasDigitWildcard()) {
			if (patternChar == digitWildcardChar()) {
				return Character.isDigit(probeChar);
			}
		}

		if (hasIgnoreCase()) {
			return Character.toUpperCase(probeChar) == Character.toUpperCase(patternChar);
		}
		else {
			return probeChar == patternChar;
		}
	} // charsAreEqual()

	// -------------------------------------------------------------------------
	/**
	 * @param character
	 * @return
	 */
	protected static boolean endReached(char character) {
		return character == StringExaminer.END_REACHED;
	} // endReached()

	// -------------------------------------------------------------------------
	/**
	 * @param character
	 * @return
	 */
	protected static boolean endNotReached(char character) {
		return !endReached(character);
	} // endNotReached()

	// -------------------------------------------------------------------------
	/**
	 * @param patternIterator
	 * @param probeCh
	 * @return
	 */
	protected static char getPatternChar(StringExaminer patternIterator, char probeCh) {
		final char patternCh = patternIterator.nextChar();
		return (patternCh == SINGLECHAR_WILDCARD) ? probeCh : patternCh;
	} // getPatternChar()

	// -------------------------------------------------------------------------
	/**
	 * @param str
	 * @return
	 */
	protected StringExaminer newExaminer(String str) {
		return new StringExaminer(str, hasIgnoreCase());
	} // newExaminer()

	// -------------------------------------------------------------------------
	/**
	 * @return
	 */
	protected String ignoreCaseAsString() {
		return hasIgnoreCase() ? SWITCH_ON : SWITCH_OFF;
	} // ignoreCaseAsString()

	// -------------------------------------------------------------------------
	/**
	 * @return
	 */
	protected String inspectString() {
		StringBuffer buffer = new StringBuffer(50);

		buffer.append(INSPECT_PREFIX);
		buffer.append(ignoreCaseAsString());
		buffer.append(INSPECT_SEPARATOR);
		buffer.append(getPattern());
		buffer.append(INSPECT_SUFFIX);

		return buffer.toString();
	} // inspectString()

	// -------------------------------------------------------------------------
	/**
	 * @return
	 */
	protected static StringUtil strUtil() {
		return StringUtil.current();
	} // strUtil()

	// -------------------------------------------------------------------------

}
