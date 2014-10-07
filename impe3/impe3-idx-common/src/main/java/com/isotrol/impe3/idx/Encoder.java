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


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The OpenCms Encoder class provides static methods to decode and encode data.<p>
 * 
 * The methods in this class are substitutes for <code>java.net.URLEncoder.encode()</code> and
 * <code>java.net.URLDecoder.decode()</code>. Use the methods from this class in all OpenCms core classes to ensure the
 * encoding is always handled the same way.<p>
 * 
 * The de- and encoding uses the same coding mechanism as JavaScript, special characters are replaxed with
 * <code>%hex</code> where hex is a two digit hex number.<p>
 * 
 * <b>Note:</b> On the client side (browser) instead of using corresponding <code>escape</code> and
 * <code>unescape</code> JavaScript functions, better use <code>encodeURIComponent</code> and
 * <code>decodeURIComponent</code> functions wich are work properly with unicode characters. These functions are
 * supported in IE 5.5+ and NS 6+ only.
 * 
 * @author Michael Emmerich
 * @author Alexander Kandzior (a.kandzior@alkacon.com)
 */
public final class Encoder {
	private static final Logger logger = LoggerFactory.getLogger(Encoder.class);
	
	private static final Pattern PAT_UNICODE = Pattern.compile("%u([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])");

	private static final Map<String, String> UNI2LATIN;

	static {
		UNI2LATIN = new HashMap<String, String>();
		UNI2LATIN.put("2018", "\u0027");
		UNI2LATIN.put("2019", "\u0027");
		UNI2LATIN.put("201C", "\"");
		UNI2LATIN.put("201D", "\"");
		UNI2LATIN.put("2022", "\u00b7");
	}

	/** Flag to indicate if the Java 1.4 encoding method (with encoding parameter) is supported by the JVM */
	private static boolean cNewEncoding = true;

	/** Flag to indicate if the Java 1.4 decoding method (with encoding parameter) is supported by the JVM */
	private static boolean cNewDecoding = true;

	/** Default encoding for JavaScript decodeUriComponent methods is UTF-8 by w3c standard */
	public static final String C_URI_ENCODING = "UTF-8";

	/**
	 * Constructor
	 */
	private Encoder() {
	}

	/**
	 * This method is a substitute for <code>URLEncoder.encode()</code>. Use this in all OpenCms core classes to ensure
	 * the encoding is always handled the same way.<p>
	 * 
	 * In case you don't know what encoding to use, set the value of the <code>encoding</code> parameter to
	 * <code>null</code>. This will use the default encoding, which is propably the right one.<p>
	 * 
	 * It also solves a backward compatiblity issue between Java 1.3 and 1.4, since 1.3 does not support an explicit
	 * encoding parameter and always uses the default system encoding.<p>
	 * 
	 * @param source the String to encode
	 * @param encoding the encoding to use (if null, the system default is used)
	 * @param fallbackToDefaultEncoding If true, the method will fallback to the default encoding (Java 1.3 style), if
	 * false, the source String will be returned unencoded
	 * @return the encoded source String
	 */
	public static String encode(String source, String encoding, boolean fallbackToDefaultEncoding) {
		if (source == null) {
			return null;
		}
		if (encoding != null) {
			if (cNewEncoding) {
				try {
					return URLEncoder.encode(source, encoding);
				} catch (java.io.UnsupportedEncodingException e) {
					logger.warn("Encoding unsupported exception.");
				} catch (java.lang.NoSuchMethodError n) {
					cNewEncoding = false;
				}
			}
			if (!fallbackToDefaultEncoding) {
				return source;
			}
		}
		// Fallback to default encoding
		try {
			return URLEncoder.encode(source, C_URI_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.warn("UTF-8 Encoding unsupported exception.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Encodes a String using the default encoding.
	 * 
	 * @param source the String to encode
	 * @return String the encoded source String
	 */
	public static String encode(String source) {
		return encode(source, C_URI_ENCODING, true);
	}

	/**
	 * This method is a substitute for <code>URLDecoder.decode()</code>. Use this in all OpenCms core classes to ensure
	 * the encoding is always handled the same way.<p>
	 * 
	 * In case you don't know what encoding to use, set the value of the <code>encoding</code> parameter to
	 * <code>null</code>. This will use the default encoding, which is propably the right one.<p>
	 * 
	 * It also solves a backward compatiblity issue between Java 1.3 and 1.4, since 1.3 does not support an explicit
	 * encoding parameter and always uses the default system encoding.<p>
	 * 
	 * @param source The string to decode
	 * @param encoding The encoding to use (if null, the system default is used)
	 * @param fallbackToDefaultDecoding If true, the method will fallback to the default encoding (Java 1.3 style), if
	 * false, the source String will be returned undecoded
	 * @return The decoded source String
	 */
	public static String decode(String source, String encoding, boolean fallbackToDefaultDecoding) {
		if (source == null) {
			return null;
		}
		if (encoding != null) {
			if (cNewDecoding) {
				try {
					return URLDecoder.decode(source, encoding);
				} catch (java.io.UnsupportedEncodingException e) {
					logger.warn("Decoding unsupported exception.");
				} catch (java.lang.NoSuchMethodError n) {
					cNewDecoding = false;
				}
			}
			if (!fallbackToDefaultDecoding) {
				return source;
			}
		}
		// Fallback to default decoding
		try {
			return URLDecoder.decode(source, C_URI_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.warn("UTF-8 Encoding unsupported exception.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Decodes a String using the default encoding.
	 * 
	 * @param source the String to decode
	 * @return String the decoded source String
	 */
	public static String decode(String source) {
		return decode(source, C_URI_ENCODING, true);
	}

	/**
	 * Encodes a String in a way that is compatible with the JavaScript escape function.
	 * 
	 * @param source The textstring to be encoded.
	 * @return The JavaScript escaped string.
	 */
	public static String escape(String source, String encoding) {
		StringBuffer ret = new StringBuffer();

		// URLEncode the text string. This produces a very similar encoding to JavaSscript
		// encoding, except the blank which is not encoded into a %20.
		String enc = encode(source, encoding, true);
		StringTokenizer t = new StringTokenizer(enc, "+");
		while (t.hasMoreTokens()) {
			ret.append(t.nextToken());
			if (t.hasMoreTokens()) {
				ret.append("%20");
			}
		}
		return ret.toString();
	}

	/**
	 * Encodes a String in a way that is compatible with the JavaScript escape function. Muliple blanks are encoded
	 * _multiply _with %20.
	 * 
	 * @param source The textstring to be encoded.
	 * @return The JavaScript escaped string.
	 */
	public static String escapeWBlanks(String source, String encoding) {

		// URLEncode the text string. This produces a very similar encoding to JavaSscript
		// encoding, except the blank which is not encoded into a %20.
		StringBuffer ret = new StringBuffer();
		String enc = encode(source, encoding, true);
		for (int z = 0; z < enc.length(); z++) {
			if (enc.charAt(z) == '+') {
				ret.append("%20");
			} else {
				ret.append(enc.charAt(z));
			}
		}
		return ret.toString();
	}

	/**
	 * Escapes a String so it may be printed as text content or attribute value in a HTML page or an XML file.<p>
	 * 
	 * This method replaces the following characters in a String: <ul> <li><b>&lt;</b> with &amp;lt; <li><b>&gt;</b>
	 * with &amp;gt; <li><b>&amp;</b> with &amp;amp; <li><b>&quot;</b> with &amp;quot; </ul>
	 * 
	 * @param source the string to escape
	 * @return the escaped string
	 * 
	 * @see #escapeHtml(String)
	 */
	public static String escapeXml(String source) {
		if (source == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(source.length() * 2);
		int terminatorIndex;
		for (int i = 0; i < source.length(); ++i) {
			char ch = source.charAt(i);
			switch (ch) {
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '&':
					// Don't escape already escaped international and special characters
					if ((terminatorIndex = source.indexOf(";", i)) > 0) if (source.substring(i + 1, terminatorIndex)
						.matches("#[0-9]+")) result.append(ch);
					else {
						String quote = "";
						try {
							quote = source.substring(i, terminatorIndex + 1);
						} catch (Exception e) {}

						if ("&amp;".equals(quote) || "&lt;".equals(quote) || "&gt;".equals(quote)
							|| "&quot;".equals(quote)) {
							result.append(ch);
						} else {
							result.append("&amp;");
						}
					}
					else result.append("&amp;");
					break;
				case '"':
					result.append("&quot;");
					break;
				default:
					result.append(ch);
			}
		}
		return new String(result);
	}

	/**
	 * Escapes special characters in a HTML-String with their number-based entity representation, for example &amp;
	 * becomes &amp;#38;.<p>
	 * 
	 * A character <code>num</code> is replaced if<br> <code>((ch != 32) && ((ch > 122) || (ch < 48) || (ch == 60) ||
	 * (ch == 62)))</code><p>
	 * 
	 * @param source the String to escape
	 * @return String the escaped String
	 * 
	 * @see #escapeXml(String)
	 */
	public static String escapeHtml(String source) {
		int terminatorIndex;
		if (source == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(source.length() * 2);
		for (int i = 0; i < source.length(); i++) {
			int ch = source.charAt(i);
			// Avoid escaping already escaped characters;
			if ((ch == 38) && ((terminatorIndex = source.indexOf(";", i)) > 0)) {
				if (source.substring(i + 1, terminatorIndex).matches("#[0-9]+|lt|gt|amp|quote")) {
					result.append(source.substring(i, terminatorIndex + 1));
					// Skip remaining chars up to (and including) ";"
					i = terminatorIndex;
					continue;
				}
			}
			if ((ch != 32) && ((ch > 122) || (ch < 48) || (ch == 60) || (ch == 62))) {
				result.append("&#");
				result.append(ch);
				result.append(";");
			} else {
				result.append((char) ch);
			}
		}
		return new String(result);
	}

	/**
	 * Escapes non ASCII characters in a HTML-String with their number-based entity representation, for example &amp;
	 * becomes &amp;#38;.<p>
	 * 
	 * A character <code>num</code> is replaced if<br> <code>(ch > 255)</code><p>
	 * 
	 * @param source the String to escape
	 * @return String the escaped String
	 * 
	 * @see #escapeXml(String)
	 */
	public static String escapeNonAscii(String source) {
		if (source == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(source.length() * 2);
		for (int i = 0; i < source.length(); i++) {
			int ch = source.charAt(i);
			if (ch > 255) {
				result.append("&#");
				result.append(ch);
				result.append(";");
			} else {
				result.append((char) ch);
			}
		}
		return new String(result);
	}

	/**
	 * Unescape non ascii chars.
	 * @param source the String to unescape
	 * @return unescape string
	 */
	public static String unescapeNonAscii(String source) {
		if (source == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(source.length() * 2);
		for (int i = 0; i < source.length(); i++) {
			char ch = source.charAt(i);
			if (i + 1 < source.length()) {
				char t2 = source.charAt(i + 1);
				if ((ch == '&' && t2 == '#')
					|| (source.length() > i + 5 && ch == '&' && t2 == 'a' && source.charAt(i + 2) == 'm'
						&& source.charAt(i + 3) == 'p' && source.charAt(i + 4) == ';') && source.charAt(i + 5) == '#') {
					StringBuffer sb = new StringBuffer();
					int j = 0;
					if (t2 == '#') {
						j = i + 2;
					} else {
						j = i + 6;
					}

					boolean enc = false;
					while (!enc && j < source.length()) {
						char t3 = source.charAt(j);
						if (t3 != ';') {
							sb.append(t3);
						} else {
							enc = true;
						}
						j++;
					}
					if (enc) {
						char noAncii = (char) Integer.parseInt(sb.toString());
						result.append(noAncii);
						i = j - 1;
					}
				} else {
					result.append(ch);
				}
			} else {
				result.append(ch);
			}
		}
		return new String(result);
	}

	/**
	 * Decodes a String in a way that is compatible with the JavaScript unescape function, if the string is already
	 * unescaped does nothing.
	 * 
	 * @param source The String to be decoded.
	 * @return The JavaScript unescaped String.
	 */
	public static String unescape(String source, String encoding) {
		if (source == null) {
			return null;
		}

		// Eliminamos los % correspondientes a unicode

		String desource = desescaparUnicode(source);
		/*
		 * source=source.replaceAll("%u([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])","&#$1;");
		 * 
		 * source=source.replaceAll("%u([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])","\\\\u$1");
		 * source=source.replaceAll("%u([0-9a-fA-F][0-9a-fA-F])","\\\\u$1");
		 * source=source.replaceAll("%u([0-9a-fA-F])","\\\\u$1");
		 */

		String alteredSource = desource.replaceAll("\n", "");
		alteredSource = alteredSource.replaceAll("\t", "");
		// Eliminamos los % correspondientes a js.
		alteredSource = alteredSource.replaceAll("%[0-9a-fA-F][0-9a-fA-F]", "");

		if (alteredSource.indexOf("%") >= 0) {
			// Yet unescaped
			return desource;
		}
		/*
		 * No soporta carecteres unicode if (alteredSource.matches("(.*(%[^0-9a-fA-F].|%.[^0-9a-fA-F]).*|.*%.|.*%)") ) {
		 * // Yet unescaped return source; }
		 */

		int len = desource.length();
		// to use standard decoder we need to replace '+' with "%20" (space)
		StringBuffer preparedSource = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			char c = desource.charAt(i);
			if (c == '+') {
				preparedSource.append("%20");
			} else {
				preparedSource.append(c);
			}
		}

		return decode(preparedSource.toString(), encoding, true);

	}

	private static String desescaparUnicode(String cadena) {
		boolean encontrado;
		String str = cadena;
		do {
			Matcher m = PAT_UNICODE.matcher(str);
			encontrado = m.find();
			if (encontrado) {
				str = m.replaceFirst(toCadena(m.group(1)));
			}
		} while (encontrado);
		return str;
	}

	/**
	 * Dado una cadena representando el valor en Hexadecimal de un caracter unicode devuelve ese caracter.
	 * @param punto
	 * @return
	 */
	private static String toCadena(String punto) {
		String latin = (String) UNI2LATIN.get(punto.toUpperCase());
		if (latin != null) {
			return latin;
		}
		int entero = Integer.parseInt(punto, 16);
		if (entero >= (int) Character.MIN_VALUE && entero <= Character.MAX_VALUE) {
			return new StringBuffer(1).append((char) entero).toString();
		} else {
			return "";
		}
	}

	/**
	 * Changes the encoding of a byte array that represents a String.<p>
	 * 
	 * @param input the byte array to convert
	 * @param oldEncoding the current encoding of the byte array
	 * @param newEncoding the new encoding of the byte array
	 * @return byte[] the byte array encoded in the new encoding
	 */
	public static byte[] changeEncoding(byte[] input, String oldEncoding, String newEncoding) {
		if ((oldEncoding == null) || (newEncoding == null)) {
			return input;
		}
		if (oldEncoding.trim().equalsIgnoreCase(newEncoding.trim())) {
			return input;
		}
		byte[] result = input;
		try {
			result = (new String(input, oldEncoding)).getBytes(newEncoding);
		} catch (UnsupportedEncodingException e) {
			// return value will be input value
			logger.warn("Return value will be input value");
		}
		return result;
	}

	/**
	 * Re-decodes a String that has not been correctly decoded and thus has scrambled character bytes.<p>
	 * 
	 * This is an equivalent to the JavaScript "decodeURIComponent" function. It converts from the default "UTF-8" to
	 * the currently selected system encoding.<p>
	 * 
	 * @param input the String to convert
	 * @return String the converted String
	 */
	public static String redecodeUriComponent(String input) {
		if (input == null) {
			return input;
		}
		return new String(changeEncoding(input.getBytes(), C_URI_ENCODING, "ISO-8859-1"));
	}

	/**
	 * Reemplaza las tildes y las �
	 * 
	 * @param source The textstring to be encoded.
	 * @return The JavaScript escaped string.
	 */
	public static String escapeLog(String source) {

		if (source == null) {
			return null;
		}
		StringBuffer sourcemod = new StringBuffer();

		for (int z = 0; z < source.length(); z++) {
			if (source.charAt(z) == '\u00e1') {
				sourcemod.append("a");
			} else if (source.charAt(z) == '\u00e9') {
				sourcemod.append("e");
			} else if (source.charAt(z) == '\u00ed') {
				sourcemod.append("i");
			} else if (source.charAt(z) == '\u00f3') {
				sourcemod.append("o");
			} else if (source.charAt(z) == '\u00fa') {
				sourcemod.append("u");
			} else if (source.charAt(z) == '\u00c1') {
				sourcemod.append("A");
			} else if (source.charAt(z) == '\u00c9') {
				sourcemod.append("E");
			} else if (source.charAt(z) == '\u00cd') {
				sourcemod.append("I");
			} else if (source.charAt(z) == '\u00d3') {
				sourcemod.append("O");
			} else if (source.charAt(z) == '\u00da') {
				sourcemod.append("U");
			} else if (source.charAt(z) == '\u00d1') {
				sourcemod.append("N");
			} else if (source.charAt(z) == '\u00f1') {
				sourcemod.append("n");
			} else {
				sourcemod.append(source.charAt(z));
			}
		}
		return sourcemod.toString();
	}

}
