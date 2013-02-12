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


import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.google.common.collect.Maps;


/**
 * Entity that represents a set of dependencies. Weak entity.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "PMS_DEPENDENCY_SET")
public class DependencySetEntity extends VersionedEntity implements WithDependencies {
	/** Dependencies. */
	@ElementCollection
	@JoinTable(name = "PMS_DEPENDENCY_ITEM", joinColumns = @JoinColumn(name = "DPNS_ID", nullable = false))
	@MapKeyColumn(name = "DPNS_DEPENDENCY_NAME", length = Lengths.NAME)
	private Map<String, RequiredDependencyValue> dependencies;

	/** Default constructor. */
	public DependencySetEntity() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithDependencies#getDependencies()
	 */
	public Map<String, RequiredDependencyValue> getDependencies() {
		if (dependencies == null) {
			dependencies = Maps.newHashMap();
		}
		return dependencies;
	}
}
