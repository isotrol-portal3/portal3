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

package com.isotrol.impe3.api.component;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;


/**
 * Value representing an exported action.
 * @author Andres Rodriguez
 */
public final class Action {
	private final String actionName;
	private final String registrationName;

	private Action(final String actionName, final String registrationName) {
		this.actionName = checkNotNull(actionName, "The action name must be provided");
		this.registrationName = registrationName;
	}

	/**
	 * Creates an exported action.
	 * @param actionName Name of the action to export.
	 * @param registrationName Name to use for the registratio..
	 * @return The requested action or {@code null} if the registration name is {@code null}.
	 * @throws NullPointerException if the action name is {@code null}.
	 */
	public static Action of(final String actionName, final String registrationName) {
		if (registrationName == null) {
			return null;
		}
		return new Action(actionName, registrationName);
	}

	/**
	 * Creates an exported action. The action will be registered with its name.
	 * @param name Name of the action.
	 * @return The requested action.
	 * @throws NullPointerException if the action name is {@code null}.
	 */
	public static Action of(final String name) {
		return of(name, name);
	}
	
	public String getActionName() {
		return actionName;
	}
	
	public String getRegistrationName() {
		return registrationName;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(actionName, registrationName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Action) {
			final Action key = (Action) obj;
			return equal(actionName, key.registrationName) && equal(actionName, key.registrationName);
		}
		return false;
	}
}
