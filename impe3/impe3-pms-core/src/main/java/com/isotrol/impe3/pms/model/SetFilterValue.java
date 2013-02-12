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

package com.isotrol.impe3.pms.model;


import static com.google.common.base.Objects.equal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.isotrol.impe3.nr.api.FilterType;


/**
 * Value that represents a set filter.
 * @author Andres Rodriguez
 */
@Embeddable
public class SetFilterValue {
	public static final Function<SetFilterValue, FilterType> TYPE = new Function<SetFilterValue, FilterType>() {
		public FilterType apply(SetFilterValue from) {
			return from.getType();
		};
	};

	/** Filter type. */
	@Column(name = "SETF_TYPE", nullable = false)
	@Enumerated
	private FilterType type;
	/** Description. */
	@Column(name = "SETF_DESC", nullable = true)
	private String description;

	/** Default constructor. Only for hibernate. */
	@SuppressWarnings("unused")
	private SetFilterValue() {
	}

	/**
	 * Constructor.
	 * @param type Dependency set.
	 * @param description Description.
	 */
	public SetFilterValue(FilterType type, String description) {
		this.type = type;
		this.description = description;
	}

	/**
	 * Returns the type.
	 * @return The type.
	 */
	public FilterType getType() {
		return type;
	}

	/**
	 * Returns the description.
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(type, description);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SetFilterValue) {
			final SetFilterValue v = (SetFilterValue) obj;
			return equal(type, v.type) && equal(description, v.description);
		}
		return false;
	}
}
