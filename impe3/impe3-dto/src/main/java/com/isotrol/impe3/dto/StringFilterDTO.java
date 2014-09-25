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

package com.isotrol.impe3.dto;


import java.io.Serializable;


/**
 * DTO for a string filter representation.
 * @author Andres Rodriguez
 */
public class StringFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -8482026123752821787L;

	/**
	 * Returns a new EXACT filter.
	 * @param field Field to filter.
	 * @return
	 */
	public static StringFilterDTO exact(String value) {
		return value != null ? new StringFilterDTO(value, StringMatchMode.EXACT) : null;
	}

	public static StringFilterDTO prefix(String value) {
		return value != null ? new StringFilterDTO(value, StringMatchMode.PREFIX) : null;
	}

	public static StringFilterDTO in(String value) {
		return value != null ? new StringFilterDTO(value, StringMatchMode.IN) : null;
	}
	
	private static StringFilterDTO toOr(StringFilterDTO filter) {
		if (filter == null) {
			return null;
		}
		filter.setConjunction(false);
		return filter;
	}

	/**
	 * Returns a new EXACT filter.
	 * @param field Field to filter.
	 * @return
	 */
	public static StringFilterDTO orExact(String value) {
		return toOr(exact(value));
	}

	public static StringFilterDTO orPrefix(String value) {
		return toOr(prefix(value));
	}

	public static StringFilterDTO orIn(String value) {
		return toOr(in(value));
	}
	
	/** Field value. */
	private String value;
	/** Match mode. */
	private StringMatchMode matchMode;
	/** Whether the filter is to be used in a conjunction. */
	private boolean conjunction = true;

	/**
	 * Constructor.
	 * @param value Field value.
	 * @param matchMode Match mode.
	 */
	public StringFilterDTO(String value, StringMatchMode matchMode) {
		this.value = value;
		this.matchMode = matchMode;
	}

	/**
	 * Default constructor.
	 */
	public StringFilterDTO() {
	}

	/**
	 * Returns the field value.
	 * @return The field value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the field value.
	 * @param name The field value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the match mode.
	 * @return The match mode (never {@code null}).
	 */
	public StringMatchMode getMatchMode() {
		return StringMatchMode.safe(matchMode);
	}

	/**
	 * Sets the match mode.
	 * @param ascending The match mode.
	 */
	public void setMatchMode(StringMatchMode matchMode) {
		this.matchMode = matchMode;
	}
	
	/**
	 * Returns whether the filter is to be used in a conjunction.
	 * @return True if the filter is to be used in a conjunction.
	 */
	public boolean isConjunction() {
		return conjunction;
	}
	
	/**
	 * Sets whether the filter is to be used in a conjunction.
	 * @param conjunction True if the filter is to be used in a conjunction.
	 */
	public void setConjunction(boolean conjunction) {
		this.conjunction = conjunction;
	}
}
