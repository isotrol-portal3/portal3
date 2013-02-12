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

package com.isotrol.impe3.core.modules;


import java.lang.reflect.Method;

import net.sf.derquinsej.i18n.Localized;

import com.google.common.base.Predicate;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.support.Named;
import com.isotrol.impe3.core.support.NamedSupport;


/**
 * Dependency relationship.
 * @author Andres Rodriguez.
 */
public class Dependency extends Relationship {
	/** Name. */
	private final Named name;
	/** If the dependency is required. */
	private final boolean required;
	/** If the dependency is internal. */
	private final boolean internal;
	/** If the dependency is the configuration. */
	private final boolean configuration;

	/** Predicate for required dependencies. */
	public static final Predicate<Dependency> IS_REQUIRED = new Predicate<Dependency>() {
		public boolean apply(Dependency input) {
			return input.required;
		}
	};

	static final Predicate<Dependency> IS_INTERNAL = new Predicate<Dependency>() {
		public boolean apply(Dependency input) {
			return input.internal;
		}
	};

	static final Predicate<Dependency> IS_EXTERNAL = new Predicate<Dependency>() {
		public boolean apply(Dependency input) {
			return !input.internal && !input.configuration;
		}
	};

	Dependency(final String beanName, final Class<?> type, final Method method, final boolean required) {
		super(beanName, type);
		this.name = new NamedSupport(method);
		this.required = required;
		this.internal = Modules.isInternalDependency(type);
		this.configuration = ConfigurationDefinition.IS_CONFIGURATION.apply(type);
	}

	/**
	 * @see com.isotrol.impe3.core.support.Named#getDescription()
	 */
	public final Localized<String> getDescription() {
		return name.getDescription();
	}

	/**
	 * @see com.isotrol.impe3.core.support.Named#getName()
	 */
	public final Localized<String> getName() {
		return name.getName();
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isInternal() {
		return internal;
	}

	public boolean isConfiguration() {
		return configuration;
	}
}
