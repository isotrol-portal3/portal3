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

package com.isotrol.impe3.samples.message;


import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;


/**
 * Simple connector service module.
 * @author Andres Rodriguez
 */
@Path("optional-echo-module.xml")
@Name("Simple Optional Message Echo Connector Module")
@Description("Simple Connector Module with an optional dependency example")
public interface OptionalEchoModule extends Module {
	/** Provided service. */
	@Name("Echo Service")
	MessageService echo();

	/** Required config. */
	void config(EchoConfig config);

	/** Optional service. */
	@Name("Input Service")
	void service(MessageService config);

}
