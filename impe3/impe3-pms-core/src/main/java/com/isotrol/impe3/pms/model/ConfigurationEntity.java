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
 * Entity that represents a configuration.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "CONFIGURATION")
public class ConfigurationEntity extends VersionedEntity {
	/** Configuration values. */
	@ElementCollection
	@JoinTable(name = "CONFIGURATION_VALUE", joinColumns = @JoinColumn(name = "CNFG_ID", nullable = false))
	@MapKeyColumn(name = "NAME", length = Lengths.NAME)
	private Map<String, ConfigurationValue> values;

	/** Default constructor. */
	public ConfigurationEntity() {
	}

	/**
	 * Returns the configuration values.
	 * @return The configuration values.
	 */
	public Map<String, ConfigurationValue> getValues() {
		if (values == null) {
			values = Maps.newHashMap();
		}
		return values;
	}
}
