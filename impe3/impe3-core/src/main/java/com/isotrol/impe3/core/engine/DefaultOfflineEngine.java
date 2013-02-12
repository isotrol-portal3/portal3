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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.isotrol.impe3.core.engine.RequestContexts.client;
import static com.isotrol.impe3.core.engine.RequestContexts.http;
import static com.isotrol.impe3.core.engine.RequestContexts.portal;
import static com.isotrol.impe3.core.engine.RequestContexts.request;

import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.common.base.Function;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.support.DefaultDeviceCapabilities;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.LayoutResponse;
import com.isotrol.impe3.core.OfflineEngine;
import com.isotrol.impe3.core.Page;
import com.isotrol.impe3.core.PageContext;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.WebExceptions;
import com.isotrol.impe3.core.impl.LocalParamsFactory;


/**
 * Default offline engine implementation. This engine provides a per-request implementation so IT IS NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
public final class DefaultOfflineEngine extends DefaultEngine implements OfflineEngine {
	private PortalModel portalModel = null;

	public DefaultOfflineEngine(final EngineModel model) {
		super(model);
	}

	public DefaultOfflineEngine(final EngineModel model, Function<UUID, PrincipalContext> principalContextBuilder) {
		super(model, principalContextBuilder);
	}

	private void loadPortalModel(UUID portalId) {
		checkArgument(portalModel == null);
		checkNotNull(portalId);
		try {
			portalModel = getModel().getPortal(portalId);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw WebExceptions.notFound(String.format("Portal [%s] not found", portalId));
		}
	}

	private PageInstance getPage(UUID pageId, Locale locale) {
		checkArgument(portalModel != null);
		final Page page = portalModel.getPages().getPage(checkNotNull(pageId));
		if (page == null) {
			throw WebExceptions.notFound(String.format("Page [%s] not found", pageId));
		}
		// TODO
		final ClientRequestContext client = client(page.getDevice(), DefaultDeviceCapabilities.get(page.getDevice()),
			locale);
		final RequestContext request = request(client, http());
		final PortalRequestContext portal = portal(portalModel.getModelInfo(), request, portalModel.getURIGenerator(),
			new SimplePrincipalContext(), portalModel.getContentLoader(client));
		final PageRequestContext prc = RequestContexts.firstRequestedPage(portal, PathSegments.of(), PageKey.main(),
			LocalParamsFactory.of());
		final PageInstance pi = new PageInstance(new PageContext(portalModel, prc, page));
		return pi;
	}

	public LayoutResponse getLayout(UUID portalId, UUID pageId, Locale locale) {
		loadPortalModel(portalId);
		final PageInstance pi = getPage(pageId, locale);
		try {
			final Ok ok = pi.edit();
			return getRenderingEngine(pi).getLayout(pi, ok);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}
	}

	public Response getPreview(UUID portalId, UUID pageId, Locale locale) {
		loadPortalModel(portalId);
		final PageInstance pi = getPage(pageId, locale);
		try {
			final Ok ok = pi.edit();
			return getRenderingEngine(pi).render(pi, ok, Response.ok());
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	protected ResolvedPortal resolvePortal(PathSegments path, HttpHeaders headers, HttpRequestContext request) {
		if (path.isEmpty()) {
			return null;
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(path.get(0));
		} catch (IllegalArgumentException e) {
			return null;
		}
		loadPortalModel(uuid);
		return new ResolvedPortal(path.consume(1), portalModel, null);
	}

}
