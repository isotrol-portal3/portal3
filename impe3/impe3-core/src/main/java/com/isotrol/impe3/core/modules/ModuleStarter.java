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


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.isotrol.impe3.api.modules.Module;


/**
 * Fluent interface to start a module.
 * @author Andres Rodriguez.
 * @param <T> Module class.
 */
public interface ModuleStarter<T extends Module> {
	/**
	 * Supplies an external or configuration dependency to the module.
	 * @param name Dependency name.
	 * @param value Dependency value.
	 * @return This starter for method chaining.
	 * @throws NullPointerException if any of the arguments is null.
	 * @throws IllegalArgumentException if the dependency has incorrect name or value.
	 * @throws IllegalArgumentException if the dependency is an internal dependency.
	 */
	ModuleStarter<T> put(String name, Object value);

	/**
	 * Supplies an internal dependency to the module.
	 * @param type Dependency type.
	 * @param value Dependency value.
	 * @return This starter for method chaining.
	 * @throws NullPointerException if any of the arguments is null.
	 * @throws IllegalArgumentException if the type is not an internal dependency.
	 * @throws ClassCastException if the type is not an incorrect type.
	 */
	ModuleStarter<T> set(Class<?> type, Object value);

	/**
	 * Starts the module.
	 * @param parent Parent application context.
	 * @return The started module.
	 * @throws IllegalStateException if there are unsatisfied dependencies.
	 * @throws BeansException if the bean factory could not be initialized.
	 */
	StartedModule<T> start(ApplicationContext parent) throws BeansException, IllegalStateException;
}
