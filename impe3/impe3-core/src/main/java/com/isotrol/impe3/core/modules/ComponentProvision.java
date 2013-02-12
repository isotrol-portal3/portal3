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

import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.support.Named;
import com.isotrol.impe3.core.support.NamedSupport;


/**
 * Provision relationship.
 * @author Andres Rodriguez.
 */
public final class ComponentProvision extends Provision {
	/** Name. */
	private final Named name;
	/** The component definition. */
	private final ComponentDefinition<?> component;

	ComponentProvision(final Method method, final ComponentDefinition<?> component) {
		super(method.getName(), component.getType());
		this.component = component;
		if (method.isAnnotationPresent(Name.class)) {
			this.name = new NamedSupport(method);
		} else {
			this.name = component;
		}
	}

	public ComponentDefinition<?> getComponent() {
		return component;
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
		return ModuleType.COMPONENT;
	}
}
