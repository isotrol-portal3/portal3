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

package com.isotrol.impe3.palette.redirect;


import java.util.UUID;

import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;


/**
 * Redirection to content type component.
 * @author Andres Rodriguez
 */
public class ToContentTypeComponent extends RedirectComponent {
	/** Destination content type. */
	private ContentType destination = null;
	/** Redirection type. */
	private Boolean type = false;
	/** Whether we keep the navigation key. */
	private boolean keepNavigationKey = true;
	/** Query string. */
	private String query = null;
	/** Navigation key. */
	private NavigationKey navigationKey;
	/** Request parameters. */
	private RequestParams requestParams;
	/** Request parameter. */
	private String requestParam = null;

	/**
	 * Constructor.
	 */
	public ToContentTypeComponent() {
	}

	@Inject
	public void setConfig(ToContentTypeConfig config) {
		if (config != null) {
			this.destination = config.destination();
			this.type = config.type();
			this.requestParam = config.requestParam();
			this.query = config.query();
			if (config.keepNavigationKey() != null) {
				this.keepNavigationKey = config.keepNavigationKey().booleanValue();
			}
		}
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	@Inject
	public void setRequestParams(RequestParams requestParams) {
		this.requestParams = requestParams;
	}

	/** Component execution. */
	public ComponentResponse execute() {
		ContentType c = getDestination();
		if (c == null) {
			// Nothing to do
			return ComponentResponse.OK;
		}
		NavigationKey nk = navigationKey;
		if (nk == null || !keepNavigationKey) {
			nk = NavigationKey.contentType(c);
		} else {
			nk = nk.withContentType(c);
		}
		final PageKey pk = PageKey.navigation(nk);
		final Route r = getRoute().toPage(pk);
		// External
		if (type != null && type.booleanValue()) {
			return ComponentResponse.seeOther(replaceQuery(getUriGenerator().getAbsoluteURI(r), query));
		} else {
			return ComponentResponse.internal(r);
		}
	}

	private ContentType getDestination() {
		// Priority 1: Parameter
		ContentType c = getParameterContentType();
		if (c != null) {
			return c;
		}
		return destination;
	}

	private ContentType getParameterContentType() {
		if (requestParam != null && requestParams != null && requestParams.contains(requestParam)) {
			String value = requestParams.getFirst(requestParam);
			if (value == null) {
				return null;
			}
			final UUID id;
			try {
				id = UUID.fromString(value);
			} catch (IllegalArgumentException e) {
				return null;
			}
			return getContentTypes().get(id);
		}
		return null;
	}
}
