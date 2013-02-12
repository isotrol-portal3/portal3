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

package com.isotrol.impe3.pms.api;


import java.util.Arrays;


/**
 * Enumeration of the possible "correctness" states of certain objects.
 * @author Andres Rodriguez
 */
public enum Correctness {
	/** Ok: object is correct. */
	OK,
	/** Warning: object has probles but is still usable. */
	WARN,
	/** The object can't be used. */
	ERROR;

	/**
	 * Merges a collection of correctness values.
	 * @param values Values to merge.
	 * @return Merge result.
	 */
	public static Correctness fold(Iterable<Correctness> values) {
		Correctness result = OK;
		if (values != null) {
			for (Correctness value : values) {
				result = result.merge(value);
				if (result == ERROR) {
					return ERROR;
				}
			}
		}
		return result;
	}

	/**
	 * Merges a collection of correctness values.
	 * @param values Values to merge.
	 * @return Merge result.
	 */
	public static Correctness fold(Correctness... values) {
		if (values == null) {
			return OK;
		}
		return fold(Arrays.asList(values));
	}

	/**
	 * Merges the current correctness with another, resulting in the worst.
	 * @param other Value to merge.
	 * @return The worst value or the current one if the argument is {@code null}
	 */
	public Correctness merge(Correctness other) {
		if (other == null || other == this) {
			return this;
		}
		return compareTo(other) > 0 ? this : other;
	}
}
