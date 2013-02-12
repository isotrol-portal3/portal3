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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.transform.Source;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.api.support.ForwardingPortalRequestContext;
import com.isotrol.impe3.core.impl.LocalParamsFactory;


/**
 * Immutable implementation of ActionContext.
 * @author Andres Rodriguez
 */
final class ImmutableActionContext extends ImmutablePortalRequestContext implements ActionContext {
	private final String name;
	private final UUID id;
	private final Route route;
	private volatile RenderContext renderContext = null;

	/**
	 * Contructor.
	 * @param context Portal contexts.
	 * @param name Action name.
	 * @param id Calling CIP Id.
	 * @param route Calling route (if known).
	 * @return The requested action.
	 */
	ImmutableActionContext(PortalRequestContext context, String name, UUID id, Route route) {
		super(context);
		this.name = checkNotNull(name);
		this.id = checkNotNull(id);
		this.route = checkNotNull(route);
	}

	public String getName() {
		return name;
	}

	public UUID getId() {
		return id;
	}

	public Route getRoute() {
		return route;
	}

	public RenderContext getRenderContext() {
		if (renderContext == null) {
			renderContext = new ActionRenderContext();
		}
		return renderContext;
	}

	private class ActionRenderContext extends ForwardingPortalRequestContext implements RenderContext {
		ActionRenderContext() {
		}

		@Override
		protected PortalRequestContext delegate() {
			return ImmutableActionContext.this;
		}

		public UUID getComponentId() {
			return ImmutableActionContext.this.getId();
		}

		public Map<String, UUID> getRegisteredActions() {
			return ImmutableMap.of();
		}

		public URI getActionURI(String name, Multimap<String, Object> parameters) {
			return ImmutableActionContext.this.getActionURI(getRoute(), getComponentId(), name, parameters);
		}

		public URI getAbsoluteActionURI(String name, Multimap<String, Object> parameters) {
			return ImmutableActionContext.this.getAbsoluteActionURI(getRoute(), getComponentId(), name, parameters);
		}

		public URI getRegisteredActionURI(String name, Multimap<String, Object> parameters) {
			return getActionURI(name, parameters);
		}

		public URI getAbsoluteRegisteredActionURI(String name, Multimap<String, Object> parameters) {
			return getAbsoluteActionURI(name, parameters);
		}

		public ContentKey getContentKey() {
			return null;
		}

		public NavigationKey getNavigationKey() {
			return null;
		}

		public Content getContent() {
			return null;
		}

		public Listing<?> getListing() {
			return null;
		}

		public Pagination getPagination() {
			return null;
		}

		public TemplateKey getTemplateKey() {
			return null;
		}

		public TemplateModel getTemplateModel() {
			return null;
		}

		public PathSegments getPath() {
			return PathSegments.of();
		}

		public Route getRoute() {
			return ImmutableActionContext.this.getRoute();
		}

		public PageKey getPageKey() {
			return getRoute().getPage();
		}

		public LocalParams getLocalParams() {
			return LocalParamsFactory.of();
		}

		public Exception getException() {
			return null;
		}

		public Integer getWidth() {
			return null;
		}

		public URI getSamePageURI(Multimap<String, ?> parameters) {
			return null;
		}

		public URI getSamePageURI(Set<String> remove, Multimap<String, ?> parameters) {
			return null;
		}

		public URI getAbsolutePageURI(Pagination pagination, int page) {
			return null;
		}

		public URI getRelativePageURI(Pagination pagination, int delta) {
			return null;
		}

		public URI getFirstPageURI(Pagination pagination) {
			return null;
		}

		public URI getLastPageURI(Pagination pagination) {
			return null;
		}

		public ETag getETag() {
			return null;
		}

		public Source getSource() {
			return null;
		}
	}
}
