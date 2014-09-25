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
package com.isotrol.impe3.idx.oc.extractors;


/**
 * Clase de utilidad
 * @author Juan Manuel Valverde Ramírez
 */
public class ExtractorUtil {

	private ExtractorUtil() {
		//
	}

	/**
	 * Removes "unwanted" control chars from the given content.<p>
	 * 
	 * @param content the content to remove the unwanted control chars from
	 * 
	 * @return the content with the unwanted control chars removed
	 */
	protected static String removeControlChars(String content) {

		if (content == null || content.trim().length() == 0) {
			// to avoid later null pointer exceptions an empty String is returned
			return "";
		}

		char[] chars = content.toCharArray();
		StringBuffer result = new StringBuffer(chars.length);
		boolean wasUnwanted = false;
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];

			int type = Character.getType(ch);
			switch (type) {

				// punctuation
				case Character.CURRENCY_SYMBOL:
				case Character.CONNECTOR_PUNCTUATION:
				case Character.FINAL_QUOTE_PUNCTUATION:
				case Character.INITIAL_QUOTE_PUNCTUATION:
				case Character.DASH_PUNCTUATION:
				case Character.START_PUNCTUATION:
				case Character.END_PUNCTUATION:
				case Character.OTHER_PUNCTUATION:
					// letters
				case Character.OTHER_LETTER:
				case Character.MODIFIER_LETTER:
				case Character.UPPERCASE_LETTER:
				case Character.TITLECASE_LETTER:
				case Character.LOWERCASE_LETTER:
					// digits
				case Character.DECIMAL_DIGIT_NUMBER:
					// spaces
				case Character.SPACE_SEPARATOR:
					result.append(ch);
					wasUnwanted = false;
					break;

				// line separators
				case Character.LINE_SEPARATOR:
					result.append('\n');
					wasUnwanted = true;
					break;

				// symbols
				case Character.MATH_SYMBOL:
				case Character.OTHER_SYMBOL:
					// other stuff:
				case Character.CONTROL:
				case Character.COMBINING_SPACING_MARK:
				case Character.ENCLOSING_MARK:
				case Character.FORMAT:
				case Character.LETTER_NUMBER:
				case Character.MODIFIER_SYMBOL:
				case Character.NON_SPACING_MARK:
				case Character.PARAGRAPH_SEPARATOR:
				case Character.PRIVATE_USE:
				case Character.SURROGATE:
				case Character.UNASSIGNED:
				case Character.OTHER_NUMBER:
				default:
					if (!wasUnwanted) {
						result.append('\n');
						wasUnwanted = true;
					}
			}
		}

		return result.toString();
	}
}
