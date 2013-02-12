/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.nr.api;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xerces.util.XMLChar;


/**
 * Implements the encode and decode routines as specified for XML name to SQL identifier conversion in ISO
 * 9075-14:2003.<br/> If a character <code>c</code> is not valid at a certain position in an XML 1.0 NCName it is
 * encoded in the form: '_x' + hexValueOf(c) + '_' <p/> Note that only the local part of a
 * {@link org.apache.jackrabbit.core.QName} is encoded / decoded. A URI namespace will always be valid and does not need
 * encoding.
 */
public final class ISO9075 {

	/** Pattern on an encoded character */
	private static final Pattern ENCODE_PATTERN = Pattern.compile("_x\\p{XDigit}{4}_");

	/** Padding characters */
	private static final char[] PADDING = new char[] {'0', '0', '0'};

	// /** All the possible hex digits */
	// private static final String HEX_DIGITS = "0123456789abcdefABCDEF";

	private static final String X = "_x";

	private ISO9075() {
		throw new AssertionError();
	}

	/**
	 * Encodes <code>name</code> as specified in ISO 9075.
	 * @param name the <code>String</code> to encode.
	 * @return the encoded <code>String</code> or <code>name</code> if it does not need encoding.
	 */
	public static String encode(String name) {
		// quick check for root node name
		if (name.length() == 0) {
			return name;
		}
		if (XMLChar.isValidName(name) && name.indexOf(X) < 0) {
			// already valid
			return name;
		} else {
			// encode
			StringBuffer encoded = new StringBuffer();
			for (int i = 0; i < name.length(); i++) {
				if (i == 0) {
					// first character of name
					if (XMLChar.isNameStart(name.charAt(i))) {
						if (needsEscaping(name, i)) {
							// '_x' must be encoded
							encode('_', encoded);
						} else {
							encoded.append(name.charAt(i));
						}
					} else {
						// not valid as first character -> encode
						encode(name.charAt(i), encoded);
					}
				} else if (!XMLChar.isName(name.charAt(i))) {
					encode(name.charAt(i), encoded);
				} else {
					if (needsEscaping(name, i)) {
						// '_x' must be encoded
						encode('_', encoded);
					} else {
						encoded.append(name.charAt(i));
					}
				}
			}
			return encoded.toString();
		}
	}

	/**
	 * Decodes the <code>name</code>.
	 * @param name the <code>String</code> to decode.
	 * @return the decoded <code>String</code>.
	 */
	public static String decode(String name) {
		// quick check
		if (name.indexOf(X) < 0) {
			// not encoded
			return name;
		}
		StringBuffer decoded = new StringBuffer();
		Matcher m = ENCODE_PATTERN.matcher(name);
		while (m.find()) {
			m.appendReplacement(decoded, Character.toString((char) Integer.parseInt(m.group().substring(2, 6), 16)));
		}
		m.appendTail(decoded);
		return decoded.toString();
	}

	// -------------------------< internal >-------------------------------------

	/**
	 * Encodes the character <code>c</code> as a String in the following form: <code>"_x" + hex value of c + "_"</code>.
	 * Where the hex value has four digits if the character with possibly leading zeros. <p/> Example: ' ' (the space
	 * character) is encoded to: _x0020_
	 * @param c the character to encode
	 * @param b the encoded character is appended to <code>StringBuffer</code> <code>b</code>.
	 */
	private static void encode(char c, StringBuffer b) {
		b.append(X);
		String hex = Integer.toHexString(c);
		b.append(PADDING, 0, 4 - hex.length());
		b.append(hex);
		b.append("_");
	}

	/**
	 * Returns true if <code>name.charAt(location)</code> is the underscore character and the following character
	 * sequence is 'xHHHH_' where H is a hex digit.
	 * @param name the name to check.
	 * @param location the location to look at.
	 * @throws ArrayIndexOutOfBoundsException if location > name.length()
	 */
	private static boolean needsEscaping(String name, int location) {
		return name.charAt(location) == '_';
	}
}