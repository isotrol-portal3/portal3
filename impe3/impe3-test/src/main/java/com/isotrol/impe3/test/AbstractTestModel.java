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

package com.isotrol.impe3.test;


import static com.isotrol.impe3.core.engine.RequestContexts.client;
import static com.isotrol.impe3.core.engine.RequestContexts.http;
import static com.isotrol.impe3.core.engine.RequestContexts.request;

import java.util.Locale;
import java.util.UUID;

import net.sf.derquinsej.Proxies;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.ModelInfo;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.support.DefaultDeviceCapabilities;
import com.isotrol.impe3.core.content.NRContentLoader;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.core.engine.SimplePrincipalContext;
import com.isotrol.impe3.core.impl.LocalParamsFactory;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * Abstract IMPE3 Test Model.
 * @author Andres Rodriguez
 */
abstract class AbstractTestModel implements TestModel {
	private final ComponentRequestContext context;

	AbstractTestModel(Portal portal, Device device, Locale locale, Function<UUID, NodeRepository> nodeRepositories) {
		final URIGenerator urig = new TestURIGenerator(portal);
		final ClientRequestContext client = client(device, DefaultDeviceCapabilities.get(device), locale);
		final RequestContext request = request(client, http());
		final ContentLoader contentLoader;
		if (nodeRepositories == null) {
			contentLoader = Proxies.unsupported(ContentLoader.class);
		} else {
			contentLoader = new NRContentLoader(portal, nodeRepositories, locale);
		}
		final PortalRequestContext portalContext = RequestContexts.portal(ModelInfo.create(UUID.randomUUID()), request,
			urig, new SimplePrincipalContext(), contentLoader);
		final PageRequestContext pageContext = RequestContexts.firstRequestedPage(portalContext, PathSegments.of(),
			PageKey.main(), LocalParamsFactory.of());
		this.context = RequestContexts.component(pageContext, TestSupport.uuid());
	}

	/** Returns the engine mode. */
	public final EngineMode getMode() {
		return getPortal().getMode();
	}

	/** Returns the default portal. */
	public final Portal getPortal() {
		return context.getPortal();
	}

	public ComponentRequestContext getContext() {
		return context;
	}

	public RenderContext getRenderContext() {
		return RequestContexts.render(context, null, ImmutableListMultimap.<String, String> of());
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getContentTypes()
	 */
	public final ContentTypes getContentTypes() {
		return getPortal().getContentTypes();
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getCategories()
	 */
	public final Categories getCategories() {
		return getPortal().getCategories();
	}

	public final ContentLoader getContentLoader() {
		return context.getContentLoader();
	}

}
