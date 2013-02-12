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


import static com.google.common.base.Preconditions.checkNotNull;
import net.sf.derquinse.lucis.Result;

import com.isotrol.impe3.nr.api.AbstractNodeRepositoryRequestAdapter;
import com.isotrol.impe3.nr.api.NodeRepositoriesService;
import com.isotrol.impe3.nr.api.NodeRepositoryRequest;


/**
 * A NodeRepository based on a NodeRepositoriesService implementation.
 * @author Andres Rodriguez Chamorro
 */
public class NamedNodeRepository extends AbstractNodeRepositoryRequestAdapter {
	/** Service. */
	private final NodeRepositoriesService service;
	/** Repository name. */
	private final String name;

	/**
	 * Constructor.
	 * @param service Service.
	 * @param config Configuration.
	 */
	public NamedNodeRepository(NodeRepositoriesService service, NamedNodeRepositoryConfig config) {
		this.service = checkNotNull(service);
		this.name = checkNotNull(checkNotNull(config).repositoryKey());
	}

	@Override
	protected Result execute(NodeRepositoryRequest request) {
		return service.execute(name, request);
	}

}
