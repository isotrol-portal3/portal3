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
package com.isotrol.impe3.web20.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a Counter Type.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_COUNTER_TYPE")
@NamedQuery(name = CounterTypeEntity.BY_NAME, query = "from CounterTypeEntity as e where e.name = ?")
public class CounterTypeEntity extends LongIdEntity {
	/** Query by counter type name. */
	public static final String BY_NAME = "counterType.byName";

	/** Counter Type name. */
	@Column(name = "CNTY_NAME", length = Lengths.NAME, unique = true, updatable = false, nullable = false)
	private String name;

	/**
	 * Returns the counter type name.
	 * @return The counter type name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the counter type name.
	 * @param name The counter type name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return CounterTypeEntity.class;
	}
}