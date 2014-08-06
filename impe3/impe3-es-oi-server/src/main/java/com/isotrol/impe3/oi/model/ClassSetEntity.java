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
package com.isotrol.impe3.oi.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a class set.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "OI_CLASS_SET")
@NamedQuery(name = ClassSetEntity.BY_NAME, query = "from ClassSetEntity as e where e.name = ?")
public class ClassSetEntity extends LongIdEntity {
	/** Query by class set name. */
	public static final String BY_NAME = "classSet.byName";

	/** Class set name. */
	@Column(name = "CLSS_NAME", length = Lengths.NAME, unique = true, updatable = false, nullable = false)
	private String name;

	/**
	 * Returns the class set name.
	 * @return The class set name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the tag class name.
	 * @param name The class set name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return ClassSetEntity.class;
	}
}
