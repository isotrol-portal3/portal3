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

package com.isotrol.impe3.connectors.nr;


import com.isotrol.impe3.api.metadata.Copyright;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.metadata.Version;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.nr.api.NodeRepositoriesService;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * A NodeRepository based on a NodeRepositoriesService module.
 * @author Andres Rodriguez Chamorro
 */
@Name("Repositorio de nodos por nombre")
@Description("Modulo que selecciona un repositorio de nodos por nombre entre aquellos ofertados por un servicio de repositorios")
@Copyright("Isotrol, SA. (GPL3)")
@Version("1.0")
@Path("named.xml")
public interface NamedNodeRepositoryModule extends Module {
	/** Provided service. */
	@Name("Servicio")
	NodeRepository service();

	/** Repositories Service. */
	@Name("Servicio de repositorios")
	void repositories(NodeRepositoriesService repos);

	/** Configuration. */
	void config(NamedNodeRepositoryConfig c);

}
