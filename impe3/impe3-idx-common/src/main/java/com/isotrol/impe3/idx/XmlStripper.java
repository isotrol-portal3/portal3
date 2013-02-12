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

package com.isotrol.impe3.idx;


/**
 * Quickly remove all HTML/XML/SGML tags.
 * <p>
 * This class implements a very fast tag stripper. It is not exactly careful about stripping tags... it just rips them
 * all out.
 * </p>
 * <p>
 * This class is borrowed from my old OpenCms Lucene search code, which is still available (albeit worthless) at
 * http://aleph-null.tv
 * @author mbutcher
 */
public class XmlStripper {

	public final static String INICIO_CDATA = "<![CDATA[";
	public final static char[] INICIO_CDATACHAR = INICIO_CDATA.toCharArray();
	public final static String FIN_CDATA = "]]>";
	public final static char[] FIN_CDATACHAR = FIN_CDATA.toCharArray();

	/**
	 * Ruthlessly strips all tags out of a string. Not good if you are trying to capture data inside of the tags.
	 * Tags/Elements are considered anything that begins with a &gt;. Yes, this is pretty shallow, but it works, and
	 * it's fast. If you don't care about speed, look at the HTMLParser that comes with Lucene. *
	 * <p>
	 * Note that this will include contents of script or style tags that do not use comment tags to hide their contents.
	 * </p>
	 * <p>
	 * This will work for XML, too.
	 * </p>
	 * @param char[] html
	 * @return String stripped of all tags/elements.
	 */
	public static String strip(char[] doc) {
		StringBuffer sb = new StringBuffer();
		char lastChar = ' '; // basically, prevents leading whitespace
		boolean write = true;
		for (int i = 0; i < doc.length; ++i) {
			if (doc[i] == '<') {
				boolean esCdata = false;
				if (i + INICIO_CDATACHAR.length < doc.length) {
					esCdata = true;
					for (int j = 0; j < INICIO_CDATACHAR.length; ++j) {
						if (INICIO_CDATACHAR[j] != doc[i + j]) {
							esCdata = false;
							break;
						}
					}
				}

				if (esCdata) {
					i += INICIO_CDATACHAR.length - 1;
				} else {
					write = false;
				}

			} else if (doc[i] == ']' && i + FIN_CDATACHAR.length < doc.length) {
				boolean esCdata = true;
				for (int j = 0; j < FIN_CDATACHAR.length; ++j) {
					if (FIN_CDATACHAR[j] != doc[i + j]) {
						esCdata = false;
						break;
					}
				}
				if (esCdata) {
					i += FIN_CDATACHAR.length - 1;
				}
			} else if (doc[i] == '>') {
				write = true;
				sb.append(' ');
			} else if (write && !(isWhitespace(doc[i]) && isWhitespace(lastChar))) {
				sb.append(doc[i]);
			}
			lastChar = doc[i];
		}
		return sb.toString();
	}

	public static boolean isWhitespace(char c) {
		char[] ws = {' ', '\n', '\t'};
		for (int i = 0; i < ws.length; ++i) {
			if (ws[i] == c) return true;
		}
		return false;
	}
}
