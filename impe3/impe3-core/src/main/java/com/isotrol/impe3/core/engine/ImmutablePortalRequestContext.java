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
import static com.isotrol.impe3.api.support.URIs.queryParameters;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Device;
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
import com.isotrol.impe3.core.support.RouteParams;


/**
 * Immutable implementation of PortalRequestContext. This is a non-final class. Subclasses must guarantee immutability.
 * @author Andres Rodriguez
 */
class ImmutablePortalRequestContext extends ImmutableRequestContext implements PortalRequestContext {
	/** Action path segment. */
	private static final String ACTION = "action";
	
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

	@Override
	public final UUID getPortalId() {
		return getPortal().getId();
	}

	@Override
	public final ModelInfo getPortalModelInfo() {
		return modelInfo;
	}

	@Override
	public final Portal getPortal() {
		return uriGenerator.getPortal();
	}

	@Override
	public final UriBuilder getBase() {
		return uriGenerator.getBase();
	}

	@Override
	public final UriBuilder getAbsoluteBase() {
		return uriGenerator.getAbsoluteBase();
	}

	@Override
	public final URI getURI(Route route) {
		return uriGenerator.getURI(route);
	}

	@Override
	public final URI getURI(Route route, Multimap<String, ?> parameters) {
		return uriGenerator.getURI(route, parameters);
	}

	@Override
	public final URI getAbsoluteURI(Route route) {
		return uriGenerator.getAbsoluteURI(route);
	}

	@Override
	public final URI getAbsoluteURI(Route route, Multimap<String, ?> parameters) {
		return uriGenerator.getAbsoluteURI(route, parameters);
	}

	@Override
	public final URI getURI(FileId file) {
		return uriGenerator.getURI(file);
	}

	@Override
	public final URI getURI(FileId file, String name) {
		return uriGenerator.getURI(file, name);
	}

	@Override
	public final URI getAbsoluteURI(FileId file) {
		return uriGenerator.getAbsoluteURI(file);
	}

	@Override
	public final URI getAbsoluteURI(FileId file, String name) {
		return uriGenerator.getAbsoluteURI(file, name);
	}

	@Override
	public final URI getURIByBase(String base, String path) {
		return uriGenerator.getURIByBase(base, path);
	}

	@Override
	public final URI getURIByMDBase(String base, String path) {
		return uriGenerator.getURIByMDBase(base, path);
	}
	
	private URI getActionURI(UriBuilder b, Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		checkNotNull(from, "The calling route must be provided");
		checkNotNull(cipId, "The CIP Id must be provided");
		checkNotNull(name, "The action name must be provided");
		final String portalId = getPortalId().toString();
		final Device d = from.getDevice();
		final String deviceId = d != null ? d.getStringId() : new UUID(0L, 0L).toString();
		final Locale l = from.getLocale();
		final String locale = l != null ? l.toString() : "en";
		b.segment("-", ACTION, portalId, deviceId, locale, cipId.toString(), name);
		queryParameters(b, parameters);
		queryParameters(b, RouteParams.toParams(getCSRFToken(), from));
		return b.build();
	}

	/*
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.api.PortalRequestContext#getActionURI(com.isotrol.impe3.api.Route, java.util.UUID, java.lang.String, com.google.common.collect.Multimap)
	 */
	@Override
	public final URI getActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return getActionURI(getBase(), from, cipId, name, parameters);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.api.PortalRequestContext#getAbsoluteActionURI(com.isotrol.impe3.api.Route, java.util.UUID, java.lang.String, com.google.common.collect.Multimap)
	 */
	@Override
	public URI getAbsoluteActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return getActionURI(getAbsoluteBase(), from, cipId, name, parameters);
	}

	@Override
	public final PrincipalContext getPrincipalContext() {
		return principalContext;
	}

	@Override
	public ContentLoader getContentLoader() {
		return contentLoader;
	}

	private Route getRoute(PageKey pageKey) {
		return Route.of(isSecure(), pageKey, getDevice(), getLocale());
	}

	@Override
	public URI getURI(PageKey page) {
		return getURI(getRoute(page));
	}

	@Override
	public URI getURI(PageKey page, Multimap<String, ?> parameters) {
		return getURI(getRoute(page), parameters);
	}

	@Override
	public URI getAbsoluteURI(PageKey page) {
		return getAbsoluteURI(getRoute(page));
	}

	@Override
	public URI getAbsoluteURI(PageKey page, Multimap<String, ?> parameters) {
		return getAbsoluteURI(getRoute(page), parameters);
	}

	@Override
	public URI getPortalRelativeURI(String path, Multimap<String, ?> parameters) {
		final UriBuilder b = UriBuilder.fromUri(getURI(PageKey.main()));
		b.path(path);
		URIs.queryParameters(b, parameters);
		return b.build();
	}

}
