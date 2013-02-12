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

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.DependencySetManager;


/**
 * Value that represents an overriden component.
 * @author Andres Rodriguez
 */
@Embeddable
public class OverridenComponentValue implements WithConfiguration {
	/** Overrides configuration predicate. */
	public static final Predicate<OverridenComponentValue> OVERRIDE_CONFIG = new Predicate<OverridenComponentValue>() {
		public boolean apply(OverridenComponentValue input) {
			return input != null && input.configuration != null;
		}
	};
	/** Overrides dependencies predicate. */
	public static final Predicate<OverridenComponentValue> OVERRIDE_DEPS = new Predicate<OverridenComponentValue>() {
		public boolean apply(OverridenComponentValue input) {
			return input != null && input.dependencySet != null;
		}
	};

	/** Configuration. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OCMP_CNFG_ID", nullable = true)
	private ConfigurationEntity configuration;
	/** Dependency set. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OCMP_DPNS_ID", nullable = true)
	private DependencySetEntity dependencySet;

	/** Default constructor. */
	public OverridenComponentValue() {
	}

	/**
	 * Constructor.
	 * @param configuration Configuration.
	 * @param dependencySet Dependency set.
	 */
	public OverridenComponentValue(ConfigurationEntity configuration, DependencySetEntity dependencySet) {
		this.configuration = configuration;
		this.dependencySet = dependencySet;
	}

	/**
	 * Clones the value.
	 */
	public OverridenComponentValue clone(ConfigurationManager cm, DependencySetManager dsm) throws PMSException {
		if (configuration == null && dependencySet == null) {
			return null;
		}
		ConfigurationEntity c = (configuration != null) ? cm.duplicate(configuration) : null;
		DependencySetEntity d = (dependencySet != null) ? dsm.duplicate(dependencySet) : null;
		return new OverridenComponentValue(c, d);
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithConfiguration#getConfiguration()
	 */
	public ConfigurationEntity getConfiguration() {
		return configuration;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithConfiguration#setConfiguration(com.isotrol.impe3.pms.model.ConfigurationEntity)
	 */
	public void setConfiguration(ConfigurationEntity configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the dependency set.
	 * @return The dependency set.
	 */
	public DependencySetEntity getDependencySet() {
		return dependencySet;
	}

	/**
	 * Sets the dependency set.
	 * @param dependencySet The dependency set.
	 */
	public void setDependencySet(DependencySetEntity dependencySet) {
		this.dependencySet = dependencySet;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(configuration, dependencySet);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OverridenComponentValue) {
			final OverridenComponentValue v = (OverridenComponentValue) obj;
			return equal(configuration, v.configuration) && equal(dependencySet, v.dependencySet);
		}
		return false;
	}
}
