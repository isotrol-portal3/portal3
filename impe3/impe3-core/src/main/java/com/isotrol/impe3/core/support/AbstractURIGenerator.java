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

package com.isotrol.impe3.core.support;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.PageURI;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.support.URIs;

/**
 * Abstract URI generator implementation.
 * @author Andres Rodriguez.
 */
public abstract class AbstractURIGenerator implements URIGenerator {
	/** Portal. */
	private final Portal portal;
	/** Portal base. */
	private final URI base;
	/** Portal absolute base. */
	private final URI absBase;

	/**
	 * Default constructor.
	 * @param portal Currently running portal.
	 * @param base Base URI.
	 */
	public AbstractURIGenerator(final Portal portal, final URI base, final URI absBase) {
		this.portal = checkNotNull(portal);
		this.base = checkNotNull(base).normalize();
		if (base.isAbsolute() || absBase == null) {
			this.absBase = base;
		} else {
			this.absBase = checkNotNull(absBase).normalize();
		}
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getPortal()
	 */
	@Override
	public Portal getPortal() {
		return portal;
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getBase()
	 */
	@Override
	public final UriBuilder getBase() {
		return UriBuilder.fromUri(base);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteBase()
	 */
	@Override
	public UriBuilder getAbsoluteBase() {
		return UriBuilder.fromUri(absBase);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.Route)
	 */
	@Override
	public URI getURI(Route route) {
		final PageURI page = getRouteSegments(route);
		if (page == null) {
			return base;
		}
		final UriBuilder b = getBase();
		for (String s : page.getPath()) {
			b.path(s);
		}
		URIs.queryParameters(b, page.getParameters());
		return b.build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.Route,
	 *      com.google.common.collect.Multimap)
	 */
	@Override
	public URI getURI(Route route, Multimap<String, ?> parameters) {
		final URI uri = getURI(route);
		if (uri == null) {
			return null;
		}
		final UriBuilder b = UriBuilder.fromUri(uri);
		URIs.queryParameters(b, parameters);
		return b.build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.Route)
	 */
	@Override
	public URI getAbsoluteURI(Route route) {
		final PageURI page = getRouteSegments(route);
		if (page == null) {
			return base;
		}
		final UriBuilder b = getAbsoluteBase();
		for (String s : page.getPath()) {
			b.path(s);
		}
		URIs.queryParameters(b, page.getParameters());
		return b.build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.Route,
	 *      com.google.common.collect.Multimap)
	 */
	@Override
	public URI getAbsoluteURI(Route route, Multimap<String, ?> parameters) {
		final URI uri = getAbsoluteURI(route);
		if (uri == null) {
			return null;
		}
		final UriBuilder b = UriBuilder.fromUri(uri);
		URIs.queryParameters(b, parameters);
		return b.build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.FileId)
	 */
	@Override
	public URI getURI(FileId file) {
		return getBase().path("-/file/" + file.getId().toString().toLowerCase() + "/" + file.getName()).build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.FileId, java.lang.String)
	 */
	@Override
	public URI getURI(FileId file, String name) {
		return getBase().path("-/fitem/" + file.getId().toString().toLowerCase() + "/" + name).build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.FileId)
	 */
	@Override
	public URI getAbsoluteURI(FileId file) {
		return getAbsoluteBase().path("-/file/" + file.getId().toString().toLowerCase() + "/" + file.getName()).build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.FileId,
	 *      java.lang.String)
	 */
	@Override
	public URI getAbsoluteURI(FileId file, String name) {
		return getAbsoluteBase().path("-/fitem/" + file.getId().toString().toLowerCase() + "/" + name).build();
	}

	private URI getURIByBase(URI baseUri, String path) {
		if (baseUri == null) {
			return null;
		}
		return UriBuilder.fromUri(baseUri).path(path).build();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURIByBase(java.lang.String, java.lang.String)
	 */
	@Override
	public URI getURIByBase(String base, String path) {
		return getURIByBase(portal.getBase(base), path);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURIByMDBase(java.lang.String, java.lang.String)
	 */
	@Override
	public URI getURIByMDBase(String base, String path) {
		return getURIByBase(portal.getMDBase(base), path);
	}

	protected PageURI getRouteSegments(Route route) {
		return new PageURI(PathSegments.of(), null);
	}
}
