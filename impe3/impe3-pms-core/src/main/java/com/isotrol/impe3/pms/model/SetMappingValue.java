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


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;


/**
 * Value that represents a content type mapping.
 * @author Andres Rodriguez
 */
@Embeddable
public class SetMappingValue implements MappingValue {
	/** Set. */
	@Column(name = "SRCM_SET", nullable = false)
	private String set;
	/** Mapping. */
	@Column(name = "MAPPING", length = Lengths.DESCRIPTION)
	private String mapping;

	/** Default constructor. */
	public SetMappingValue() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.MappingValue#getId()
	 */
	public UUID getId() {
		return null;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.MappingValue#isDeleted()
	 */
	public boolean isDeleted() {
		return false;
	}

	/**
	 * Returns the set name.
	 * @return The set name.
	 */
	public String getSet() {
		return set;
	}

	/**
	 * Sets the set name.
	 * @param set The set name.
	 */
	public void setSet(String set) {
		this.set = set;
	}

	/**
	 * Returns the mapping.
	 * @return The mapping.
	 */
	public String getMapping() {
		return mapping;
	}

	/**
	 * Sets the mapping.
	 * @param mapping The mapping.
	 */
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return Objects.hashCode(set, mapping);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof SetMappingValue) {
			final SetMappingValue m = (SetMappingValue) obj;
			return Objects.equal(set, m.set) && Objects.equal(mapping, m.mapping);
		}
		return false;
	}

}
