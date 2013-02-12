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

import com.isotrol.impe3.core.support.Named;
import com.isotrol.impe3.core.support.NamedSupport;


/**
 * Provision relationship.
 * @author Andres Rodriguez.
 */
public final class ConnectorProvision extends Provision {
	/** Name. */
	private final Named name;

	ConnectorProvision(final String beanName, final Class<?> type, final Method method) {
		super(beanName, type);
		this.name = new NamedSupport(method);
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

	/**
	 * Returns the module type that can provide the current provision.
	 */
	ModuleType getModuleType() {
		return ModuleType.CONNECTOR;
	}
}
