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

import static com.google.common.base.Objects.equal;

import com.google.common.base.Objects;

/**
 * 
 * @author Emilio Escobar Reyero
 * 
 */
public final class NRRangeQuery extends NodeQuery {

	private static final long serialVersionUID = -5026205843429586848L;

	private final String fieldName;
	private final String lowerVal;
	private final String upperVal;
	private final boolean includeLower;
	private final boolean includeUpper;

	protected NRRangeQuery(String fieldName, String lowerVal, String upperVal,
			boolean includeLower, boolean includeUpper) {
		this.fieldName = fieldName;
		this.lowerVal = lowerVal;
		this.upperVal = upperVal;
		this.includeLower = includeLower;
		this.includeUpper = includeUpper;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getLowerVal() {
		return lowerVal;
	}

	public String getUpperVal() {
		return upperVal;
	}

	public boolean isIncludeLower() {
		return includeLower;
	}

	public boolean isIncludeUpper() {
		return includeUpper;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getBoost(), fieldName, lowerVal, upperVal, includeLower,
				includeUpper);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NRRangeQuery) {
			final NRRangeQuery q = (NRRangeQuery) obj;

			return getBoost() == q.getBoost() 
					&& includeLower == q.includeLower
					&& includeUpper == q.includeUpper
					&& equal(fieldName, q.fieldName)
					&& equal(lowerVal, q.lowerVal)
					&& equal(upperVal, q.upperVal);
		}
		return false;
	}

}
