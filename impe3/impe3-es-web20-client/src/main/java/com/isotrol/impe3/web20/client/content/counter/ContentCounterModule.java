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
package com.isotrol.impe3.web20.client.content.counter;


import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.web20.api.CountersService;


/**
 * Web 2.0 Content Counters Module
 * @author Andres Rodriguez
 */
@Name("Componentes para Contadores de Contenidos")
public interface ContentCounterModule extends Module {
	/** Counter component. */
	@Name("Contador")
	ContentCounterComponent counter();

	/** Counter component. */
	@Name("Contenidos m\u00e1s vistos")
	GreatestHitsComponent greatestHits();

	/** Exported Action. */
	ContentCounterAction countAction();

	/** Module configuration. */
	void config(ContentCounterConfig c);

	/** Counter service */
	@Name("Servicio de Contadores")
	void service(CountersService service);
}
