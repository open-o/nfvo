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
package org.openo.nfvo.monitor.umc.util.filescaner;

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
 * 
 * @version 1.0
 */

public class StringScanner {
	// =========================================================================
	// CONSTANTS
	// =========================================================================
	public static final char END_REACHED = (char) -1;

	// =========================================================================
	// INSTANCE VARIABLES
	// =========================================================================
	protected int length = 0;

	protected int position = 0;

	protected int pos_marker = 0;

	protected char[] buffer = null;

	// =========================================================================
	// PUBLIC CLASS METHODS
	// =========================================================================
	// =========================================================================
	// CONSTRUCTORS
	// =========================================================================
	/**
	 * Initialize the new instance with the string that should be scanned.
	 * 
	 * @param stringToScan
	 */
	public StringScanner(String stringToScan) {
		super();
		length = stringToScan.length();
		buffer = new char[length];
		stringToScan.getChars(0, length, buffer, 0);
	} // StringScanner()

	// =========================================================================
	// PUBLIC INSTANCE METHODS
	// =========================================================================
	/**
	 * Returns true, if the given character indicates that the end of the
	 * scanned string is reached.
	 * 
	 * @param character
	 * @return
	 */
	public static boolean endReached(char character) {
		return character == END_REACHED;
	} // endReached()

	// -------------------------------------------------------------------------

	/**
	 * Returns true, if the given character does <b>not</b> indicate that the
	 * end of the scanned string si reached.
	 * 
	 * @param character
	 * @return
	 */
	public static boolean endNotReached(char character) {
		return !endReached(character);
	} // endNotReached()

	// -------------------------------------------------------------------------

	/**
	 * Returns the string the scanner was initialized with
	 * 
	 * @return
	 */
	public String toString() {
		return new String(buffer);
	} // toString()

	// -------------------------------------------------------------------------

	/**
	 * Moves the position pointer count characters. positive values move
	 * forwards, negative backwards. The position never becomes negative !
	 * 
	 * @param count
	 */
	public void skip(int count) {
		position += count;
		if (position < 0) {
			position = 0;
		}

	} // skip()

	// -------------------------------------------------------------------------

	/**
	 * Returns the character at the current position without changing the
	 * position, that is subsequent calls to this method return always the same
	 * character.
	 * 
	 * @return
	 */
	public char peek() {
		return position < getLength() ? buffer[position] : END_REACHED;
	} // skip()

	// -------------------------------------------------------------------------

	/**
	 * Returns the character at the current position and increments the position
	 * afterwards by 1.
	 * 
	 * @return
	 */
	public char nextChar() {
		final char next = peek();
		if (endNotReached(next)) {
			skip(1);
		}

		return next;
	} // nextChar()

	// -------------------------------------------------------------------------

	/**
	 * Returns true, if the scanner has reached the end and a further invocation
	 * of nextChar() would return the END_REACHED character.
	 * 
	 * @return
	 */
	public boolean atEnd() {
		return endReached(peek());
	} // atEnd()

	// -------------------------------------------------------------------------

	/**
	 * Returns true, if the scanner has not yet reached the end.
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return !atEnd();
	} // hasNext()

	// -------------------------------------------------------------------------

	/**
	 * Returns the next character that is no whitespace and leaves the position
	 * pointer one character after the returned one.
	 * 
	 * @return
	 */
	public char nextNoneWhitespaceChar() {
		char next = nextChar();
		while ((endNotReached(next)) && (Character.isWhitespace(next))) {
			next = nextChar();
		}
		return next;
	} // nextNoneWhitespaceChar()

	// -------------------------------------------------------------------------

	/**
	 * Returns the current position in the string
	 * 
	 * @return
	 */
	public int getPosition() {
		return position;
	} // getPosition()

	// -------------------------------------------------------------------------

	/**
	 * Remembers the current position for later use with restorePosition()
	 */
	public void markPosition() {
		pos_marker = position;
	} // markPosition()

	// -------------------------------------------------------------------------

	/**
	 * Restores the position to the value of the latest markPosition() call
	 */
	public void restorePosition() {
		setPosition(pos_marker);
	} // restorePosition()

	// -------------------------------------------------------------------------

	// =========================================================================
	// PROTECTED INSTANCE METHODS
	// =========================================================================
	/**
	 * @return
	 */
	protected int getLength() {
		return length;
	} // length()

	// -------------------------------------------------------------------------
	/**
	 * @param pos
	 */
	protected void setPosition(int pos) {
		if ((pos >= 0) && (pos <= getLength())) {
			position = pos;
		}

	} // setPosition()

	// -------------------------------------------------------------------------

}
