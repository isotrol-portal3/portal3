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

package com.isotrol.impe3.api.support;


import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.ForwardingObject;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;


/**
 * Forwarding URI generator.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingURIGenerator extends ForwardingObject implements URIGenerator {
	/** Default constructor. */
	public ForwardingURIGenerator() {
	}

	protected abstract URIGenerator delegate();

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getPortal()
	 */
	public Portal getPortal() {
		return delegate().getPortal();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getBase()
	 */
	public UriBuilder getBase() {
		return delegate().getBase();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteBase()
	 */
	public UriBuilder getAbsoluteBase() {
		return delegate().getAbsoluteBase();
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.FileId)
	 */
	public URI getURI(FileId file) {
		return delegate().getURI(file);
	}
	
	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.FileId)
	 */
	public URI getAbsoluteURI(FileId file) {
		return delegate().getAbsoluteURI(file);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.Route)
	 */
	public URI getURI(Route route) {
		return delegate().getURI(route);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.Route, com.google.common.collect.Multimap)
	 */
	public URI getURI(Route route, Multimap<String, ?> parameters) {
		return delegate().getURI(route, parameters);
	}
	
	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.Route)
	 */
	public URI getAbsoluteURI(Route route) {
		return delegate().getAbsoluteURI(route);
	}
	
	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.Route, com.google.common.collect.Multimap)
	 */
	public URI getAbsoluteURI(Route route, Multimap<String, ?> parameters) {
		return delegate().getAbsoluteURI(route, parameters);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURI(com.isotrol.impe3.api.FileId, java.lang.String)
	 */
	public URI getURI(FileId file, String name) {
		return delegate().getURI(file, name);
	}
	
	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteURI(com.isotrol.impe3.api.FileId, java.lang.String)
	 */
	public URI getAbsoluteURI(FileId file, String name) {
		return delegate().getAbsoluteURI(file, name);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURIByBase(java.lang.String, java.lang.String)
	 */
	public URI getURIByBase(String base, String path) {
		return delegate().getURIByBase(base, path);
	}

	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getURIByMDBase(java.lang.String, java.lang.String)
	 */
	public URI getURIByMDBase(String base, String path) {
		return delegate().getURIByMDBase(base, path);
	}
	
	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getActionURI(com.isotrol.impe3.api.Route, java.util.UUID, java.lang.String, com.google.common.collect.Multimap)
	 */
	public URI getActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return delegate().getActionURI(from, cipId, name, parameters);
	}
	
	/**
	 * @see com.isotrol.impe3.api.URIGenerator#getAbsoluteActionURI(com.isotrol.impe3.api.Route, java.util.UUID, java.lang.String, com.google.common.collect.Multimap)
	 */
	public URI getAbsoluteActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return delegate().getAbsoluteActionURI(from, cipId, name, parameters);
	}
}
