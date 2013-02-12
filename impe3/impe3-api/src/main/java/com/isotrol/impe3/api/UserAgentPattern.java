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

package com.isotrol.impe3.api;


import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.base.Predicate;


/**
 * Value representing an user agent predicate.
 * @author Andres Rodriguez
 */
public abstract class UserAgentPattern implements Predicate<String> {
	private static final UserAgentPattern NONE = new None();

	/** Default constructor. */
	private UserAgentPattern() {
	}

	/**
	 * Creates a null user agent pattern.
	 * @return The requested pattern.
	 */
	public static UserAgentPattern none() {
		return NONE;
	}

	/**
	 * Creates an exact match user agent pattern.
	 * @params ua UA string to match.
	 * @return The requested pattern.
	 */
	public static UserAgentPattern exact(String ua) {
		if (ua == null || ua.length() == 0) {
			return NONE;
		}
		return new Exact(ua);
	}

	/**
	 * Creates a regular expression-based user agent pattern.
	 * @params re Regular expression to match.
	 * @return The requested pattern.
	 * @throws PatternSyntaxException if unable to parse the argument.
	 */
	public static UserAgentPattern re(String re) {
		if (re == null || re.length() == 0) {
			return NONE;
		}
		return new RE(re);
	}

	/**
	 * Creates a regular expression-based user agent pattern.
	 * @params re Regular expression to match.
	 * @return The requested pattern or an always false pattern if unable to parse the argument.
	 */
	public static UserAgentPattern safeRE(String re) {
		try {
			return re(re);
		} catch (PatternSyntaxException e) {
			return none();
		}
	}

	private static final class None extends UserAgentPattern {
		/** Default constructor. */
		private None() {
		}

		public boolean apply(String input) {
			return false;
		}
	}

	private static final class Exact extends UserAgentPattern {
		private final String string;

		/** Default constructor. */
		private Exact(String string) {
			// Checked not null by caller
			this.string = string;
		}

		public boolean apply(String input) {
			return string.equals(input);
		}
	}

	private static final class RE extends UserAgentPattern {
		private final Pattern pattern;

		/** Default constructor. */
		private RE(String re) {
			// Checked not null by caller
			this.pattern = Pattern.compile(re);
		}

		public boolean apply(String input) {
			if (input == null) {
				return false;
			}
			return pattern.matcher(input).matches();
		}
	}

}
