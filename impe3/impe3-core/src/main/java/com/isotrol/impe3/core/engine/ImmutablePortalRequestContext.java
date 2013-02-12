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

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.ModelInfo;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.support.URIs;


/**
 * Immutable implementation of PortalRequestContext. This is a non-final class. Subclasses must guarantee immutability.
 * @author Andres Rodriguez
 */
class ImmutablePortalRequestContext extends ImmutableRequestContext implements PortalRequestContext {
	private final ModelInfo modelInfo;
	private final URIGenerator uriGenerator;
	private final PrincipalContext principalContext;
	private final ContentLoader contentLoader;

	ImmutablePortalRequestContext(ModelInfo modelInfo, RequestContext requestContext, URIGenerator uriGenerator,
		PrincipalContext principalContext, ContentLoader contentLoader) {
		super(requestContext);
		this.modelInfo = checkNotNull(modelInfo);
		this.uriGenerator = checkNotNull(uriGenerator);
		this.principalContext = checkNotNull(principalContext);
		this.contentLoader = contentLoader;
	}

	ImmutablePortalRequestContext(PortalRequestContext context) {
		super(context);
		if (context instanceof ImmutablePortalRequestContext) {
			ImmutablePortalRequestContext prc = (ImmutablePortalRequestContext) context;
			this.modelInfo = prc.modelInfo;
			this.uriGenerator = prc.uriGenerator;
			this.principalContext = prc.principalContext;
			this.contentLoader = prc.contentLoader;
		} else {
			this.modelInfo = checkNotNull(context.getPortalModelInfo());
			this.uriGenerator = checkNotNull(context);
			this.principalContext = checkNotNull(context.getPrincipalContext());
			this.contentLoader = context.getContentLoader();
		}
	}

	public final UUID getPortalId() {
		return getPortal().getId();
	}

	public final ModelInfo getPortalModelInfo() {
		return modelInfo;
	}

	public final Portal getPortal() {
		return uriGenerator.getPortal();
	}

	public final UriBuilder getBase() {
		return uriGenerator.getBase();
	}

	public final UriBuilder getAbsoluteBase() {
		return uriGenerator.getAbsoluteBase();
	}

	public final URI getURI(Route route) {
		return uriGenerator.getURI(route);
	}

	public final URI getURI(Route route, Multimap<String, ?> parameters) {
		return uriGenerator.getURI(route, parameters);
	}

	public final URI getAbsoluteURI(Route route) {
		return uriGenerator.getAbsoluteURI(route);
	}

	public final URI getAbsoluteURI(Route route, Multimap<String, ?> parameters) {
		return uriGenerator.getAbsoluteURI(route, parameters);
	}

	public final URI getURI(FileId file) {
		return uriGenerator.getURI(file);
	}

	public final URI getURI(FileId file, String name) {
		return uriGenerator.getURI(file, name);
	}

	public final URI getAbsoluteURI(FileId file) {
		return uriGenerator.getAbsoluteURI(file);
	}

	public final URI getAbsoluteURI(FileId file, String name) {
		return uriGenerator.getAbsoluteURI(file, name);
	}

	public final URI getURIByBase(String base, String path) {
		return uriGenerator.getURIByBase(base, path);
	}

	public final URI getURIByMDBase(String base, String path) {
		return uriGenerator.getURIByMDBase(base, path);
	}

	public final URI getActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return uriGenerator.getActionURI(from, cipId, name, parameters);
	}

	public final URI getAbsoluteActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return uriGenerator.getAbsoluteActionURI(from, cipId, name, parameters);
	}

	public final PrincipalContext getPrincipalContext() {
		return principalContext;
	}

	public ContentLoader getContentLoader() {
		return contentLoader;
	}

	private Route getRoute(PageKey pageKey) {
		return Route.of(isSecure(), pageKey, getDevice(), getLocale());
	}

	public URI getURI(PageKey page) {
		return getURI(getRoute(page));
	}

	public URI getURI(PageKey page, Multimap<String, ?> parameters) {
		return getURI(getRoute(page), parameters);
	}

	public URI getAbsoluteURI(PageKey page) {
		return getAbsoluteURI(getRoute(page));
	}

	public URI getAbsoluteURI(PageKey page, Multimap<String, ?> parameters) {
		return getAbsoluteURI(getRoute(page), parameters);
	}

	public URI getPortalRelativeURI(String path, Multimap<String, ?> parameters) {
		final UriBuilder b = UriBuilder.fromUri(getURI(PageKey.main()));
		b.path(path);
		URIs.queryParameters(b, parameters);
		return b.build();
	}

}
