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

package com.isotrol.impe3.connectors.hessian;


import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Configuration for Node Repository Hessian implementations
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public interface NRServiceConfig extends Configuration {
	/** Server side service url */
	@Name("URL Servicio")
	String serviceUrl();

	/** Microcaching time (ms). */
	@Name("Microcacheo (ms)")
	@Description("Activa el microcach\00e9 si no nulo. El valor introducido se ajusta al intervalo 800 - 80000 (ms)")
	@Optional
	Integer microcache();
}
