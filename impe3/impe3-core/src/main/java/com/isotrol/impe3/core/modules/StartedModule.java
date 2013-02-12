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


import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.modules.Module;


/**
 * Interface representing a running module.
 * @author Andres Rodriguez.
 * @param <T> Module class.
 */
public interface StartedModule<T extends Module> {
	/**
	 * Returns the module definition.
	 * @return The module definition.
	 */
	ModuleDefinition<T> getModuleDefinition();

	/**
	 * Returns an exported bean.
	 * @param name Name of the exported bean.
	 * @return The exported bean.
	 * @throws IllegalArgumentException if the requested bean is not exported.
	 */
	Object getProvision(String name);

	/**
	 * Returns a type-safe accessor to the provisions of the module.
	 * @return An implementation of the module class.
	 */
	T getModule();

	/**
	 * Returns an action object.
	 * @param context Action context.
	 * @return The requested action.
	 * @throws IllegalStateException if the module is not a component module.
	 * @throws IllegalArgumentException if the module provides no action with the requested name.
	 */
	Object getAction(ActionContext context);

	/**
	 * Stops a running module. The behaviour of any method after closing is unspecified.
	 */
	void stop();
}
