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

package com.isotrol.impe3.palette.content.filter;


import javax.ws.rs.DefaultValue;

import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Parametrized query component configuration.
 * @author Manuel Ruiz
 * @author Andres Rodriguez
 */
public interface ParametrizedQueryConfig extends WithQueryType, WithConditionalClause, WithParameterSource, WithField,
	Configuration {
	/** Parameter Value */
	@Name("Par\u00e1metro")
	String parameter();
	/** Allow multiple values. */
	@Name("Permitir valores m\u00faltiples")
	@DefaultValue("false")
	boolean allowMultiple();
	/** Consilidate multiple values. */
	@Name("Consolidar valores m\u00faltiples")
	@Optional
	ConditionalClause consolidate();
}
