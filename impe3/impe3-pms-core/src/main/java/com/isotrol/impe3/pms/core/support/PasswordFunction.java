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

package com.isotrol.impe3.pms.core.support;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;


/**
 * Password calculation support function.
 * @author Andres Rodriguez.
 */
public enum PasswordFunction implements Function<String, String> {
	INSTANCE;

	public String apply(String from) {
		from = from.trim();
		try {
			final String input = Joiner.on("_impe3_").join(from, from, from);
			final byte[] data = input.getBytes("UTF-8");
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(data);
			final byte[] digest = md.digest();
			final StringBuilder sb = new StringBuilder(40);
			for (byte b : digest) {
				final String s = Integer.toHexString(b & 0xff);
				if (s.length() == 1) {
					sb.append('0');
				}
				sb.append(s);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e1) {
			// Should not happen.
			return null;
		} catch (NoSuchAlgorithmException e) {
			// Should not happen.
			return null;
		}
	}
}
