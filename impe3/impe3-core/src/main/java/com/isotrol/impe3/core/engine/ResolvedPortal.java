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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.impl.LocalParamsFactory;


/**
 * Portal resolution result.
 */
public final class ResolvedPortal {
	private final PathSegments path;
	private final PortalModel portalModel;
	private final LocalParams parameters;

	public ResolvedPortal(final PathSegments path, final PortalModel portalModel, final Map<String, Object> parameters) {
		this.path = checkNotNull(path);
		this.portalModel = checkNotNull(portalModel);
		this.parameters = LocalParamsFactory.of(parameters);
	}

	public PathSegments getPath() {
		return path;
	}

	public PortalModel getPortalModel() {
		return portalModel;
	}

	public LocalParams getParameters() {
		return parameters;
	}

	public Portal getPortal() {
		return portalModel.getPortal();
	}
}
