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

package com.isotrol.impe3.pms.core;


import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Environment Manager.
 * @author Andres Rodriguez.
 */
public interface EnvironmentManager {
	/** Default environment name. */
	String NAME = "DEFAULT";
	/** Default environment description. */
	String DESCRIPTION = "Default Environment";

	/**
	 * Gets the default environment, creating it if needed.
	 * @param user The user creating the environment.
	 * @return The default environment.
	 */
	EnvironmentEntity getDefaultEnvironment(UserEntity user) throws PMSException;

	/**
	 * Gets the default environment.
	 * @return The default environment.
	 * @throw IllegalStateException if the default environment does not exist.
	 */
	EnvironmentEntity getDefaultEnvironment() throws PMSException;

	/**
	 * Gets the environment with the specified name.
	 * @param name Environment name.
	 * @return The requested environment or {@code null} if it does not exist.
	 */
	EnvironmentEntity getEnvironment(String name);
}
