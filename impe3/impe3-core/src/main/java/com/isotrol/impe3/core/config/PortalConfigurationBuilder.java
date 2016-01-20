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

package com.isotrol.impe3.core.config;


import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.PortalConfiguration;


/**
 * Configuration builder interface.
 * @author Andres Rodriguez.
 * 
 * @param <T> Configuration interface.
 */
public interface PortalConfigurationBuilder<T extends PortalConfiguration> extends Builder<T> {
	/**
	 * Sets a configuration value.
	 * @param parameter Parameter name.
	 * @param value Parameter value.
	 * @return This builder for method chaining.
	 * @throws NullPointerException if the parameter is null.
	 * @throws IllegalArgumentException if the parameter does not exist or the value is of an incorrect type.
	 */
	PortalConfigurationBuilder<T> set(String parameter, Object value);
}
