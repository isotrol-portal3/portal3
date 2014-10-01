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

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.ModelInfo;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.content.ContentLoader;


/**
 * Forwarding portal request context.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingPortalRequestContext extends ForwardingRequestContext implements PortalRequestContext {
	/** Default constructor. */
	public ForwardingPortalRequestContext() {
	}

	protected abstract PortalRequestContext delegate();

	@Override
	public UUID getPortalId() {
		return delegate().getPortalId();
	}

	@Override
	public ModelInfo getPortalModelInfo() {
		return delegate().getPortalModelInfo();
	}

	@Override
	public PrincipalContext getPrincipalContext() {
		return delegate().getPrincipalContext();
	}

	@Override
	public ContentLoader getContentLoader() {
		return delegate().getContentLoader();
	}

	@Override
	public Portal getPortal() {
		return delegate().getPortal();
	}

	@Override
	public UriBuilder getBase() {
		return delegate().getBase();
	}

	@Override
	public UriBuilder getAbsoluteBase() {
		return delegate().getAbsoluteBase();
	}

	@Override
	public URI getURI(Route route) {
		return delegate().getURI(route);
	}

	@Override
	public URI getURI(Route route, Multimap<String, ?> parameters) {
		return delegate().getURI(route, parameters);
	}
	
	@Override
	public URI getAbsoluteURI(Route route) {
		return delegate().getAbsoluteURI(route);
	}

	@Override
	public URI getAbsoluteURI(Route route, Multimap<String, ?> parameters) {
		return delegate().getAbsoluteURI(route, parameters);
	}

	@Override
	public URI getURI(FileId file) {
		return delegate().getURI(file);
	}

	@Override
	public URI getURI(FileId file, String name) {
		return delegate().getURI(file, name);
	}

	@Override
	public URI getAbsoluteURI(FileId file) {
		return delegate().getAbsoluteURI(file);
	}

	@Override
	public URI getAbsoluteURI(FileId file, String name) {
		return delegate().getAbsoluteURI(file, name);
	}

	@Override
	public URI getURIByBase(String base, String path) {
		return delegate().getURIByBase(base, path);
	}

	@Override
	public URI getURIByMDBase(String base, String path) {
		return delegate().getURIByMDBase(base, path);
	}

	@Override
	public URI getActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return delegate().getActionURI(from, cipId, name, parameters);
	}

	@Override
	public URI getAbsoluteActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters) {
		return delegate().getAbsoluteActionURI(from, cipId, name, parameters);
	}

	@Override
	public URI getURI(PageKey page) {
		return delegate().getURI(page);
	}
	
	@Override
	public URI getURI(PageKey page, Multimap<String, ?> parameters) {
		return delegate().getURI(page, parameters);
	}
	
	@Override
	public URI getAbsoluteURI(PageKey page) {
		return delegate().getAbsoluteURI(page);
	}
	
	@Override
	public URI getAbsoluteURI(PageKey page, Multimap<String, ?> parameters) {
		return delegate().getAbsoluteURI(page, parameters);
	}

	@Override
	public URI getPortalRelativeURI(String path, Multimap<String, ?> parameters) {
		return delegate().getPortalRelativeURI(path, parameters);
	}

}
