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
package com.isotrol.impe3.web20.client.counter;

import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.web20.api.CountersService;

/**
 * Web 2.0 Counters Module
 * @author Emilio Escobar Reyero
 */
@Path("module-web20-counter.xml")
@Bundle
@Name("module.name")
@Description("module.desc")
public interface CounterModule extends Module {

	/** Action exporter component. */
	@Name("component")
	CounterComponent component();

	/** Action exporter component. */
	@Name("exporter")
	ActionExporterComponent exporter();

	/** Exported Action. */
	Object counter();
	
	/** Counter configuration. */
	void moduleConfig(CounterConfig config);
	
	/** Counter service*/
	void service(CountersService service);
}
