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


import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;

import com.google.common.base.Function;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.PortalModel;


/**
 * Default offline engine implementation. This engine provides a per-request implementation so IT IS NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
public final class DefaultOnlineEngine extends DefaultEngine {
	public DefaultOnlineEngine(final EngineModel model) {
		super(model);
	}

	public DefaultOnlineEngine(final EngineModel model, Function<UUID, PrincipalContext> principalContextBuilder) {
		super(model, principalContextBuilder);
	}

	@Override
	protected ResolvedPortal resolvePortal(PathSegments path, HttpHeaders headers, HttpRequestContext request) {
		int score = -1;
		PortalModel portalModel = null;
		for (PortalModel pm : getModel().getPortals()) {
			int s = getPortalScore(path, pm.getPath());
			if (s > score) {
				score = s;
				portalModel = pm;
			}
		}
		if (score < 0) {
			return null;
		}
		return new ResolvedPortal(path.consume(score), portalModel, null);
	}

	private int getPortalScore(PathSegments request, PathSegments portal) {
		final int np = portal.size();
		final int nr = request.size();
		if (np > nr) {
			return -1;
		}
		if (np == 0) {
			return 0;
		}
		for (int i = 0; i < np; i++) {
			if (!portal.get(i).equalsIgnoreCase(request.get(i))) {
				return -1;
			}
		}
		return np;
	}

}
