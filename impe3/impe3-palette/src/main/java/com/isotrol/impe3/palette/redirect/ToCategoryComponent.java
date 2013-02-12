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

import com.google.common.base.Objects;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;


/**
 * Redirection to category component.
 * @author Andres Rodriguez
 */
public class ToCategoryComponent extends RedirectComponent {
	/** Destination category. */
	private Category destination = null;
	/** Whether we prefer injected navigation. */
	private boolean preferNavigationKey = false;
	/** Whether we keep the content key. */
	private boolean keepContentKey = false;
	/** Redirection type. */
	private Boolean type = false;
	/** Query string. */
	private String query = null;
	/** Navigation key. */
	private NavigationKey navigationKey;
	/** Content key. */
	private ContentKey contentKey;
	/** Request parameters. */
	private RequestParams requestParams;
	/** Request parameter. */
	private String requestParam = null;

	/**
	 * Constructor.
	 */
	public ToCategoryComponent() {
	}

	private boolean cb(Boolean b) {
		return b != null ? b.booleanValue() : false;
	}

	@Inject
	public void setConfig(ToCategoryConfig config) {
		if (config != null) {
			this.destination = config.destination();
			this.preferNavigationKey = cb(config.preferNavigationKey());
			this.keepContentKey = cb(config.keepContentKey());
			this.type = config.type();
			this.requestParam = config.requestParam();
			this.query = config.query();
		}
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	@Inject
	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}

	@Inject
	public void setRequestParams(RequestParams requestParams) {
		this.requestParams = requestParams;
	}

	/** Component execution. */
	public ComponentResponse execute() {
		Category c = getDestination();
		if (c == null) {
			// Nothing to do
			return ComponentResponse.OK;
		}
		if (navigationKey != null && navigationKey.isCategory() && Objects.equal(c, navigationKey.getCategory())) {
			if (!keepContentKey) {
				contentKey = null;
			}
			// Nothing more to do
			return ComponentResponse.OK;
		}
		if (preferNavigationKey && navigationKey != null) {
			c = navigationKey.getCategory();
		}
		// Set the category
		navigationKey = NavigationKey.category(c);
		// If no redirection, we are done
		if (type == null) {
			return ComponentResponse.OK;
		}
		// Destination route
		final PageKey pk;
		if (keepContentKey && contentKey != null) {
			pk = PageKey.content(NavigationKey.category(c), contentKey);
		} else {
			pk = PageKey.navigation(c);
			contentKey = null;
		}
		final Route r = getRoute().toPage(pk);
		// External
		if (type.booleanValue()) {
			return ComponentResponse.seeOther(replaceQuery(getUriGenerator().getAbsoluteURI(r), query));
		} else {
			return ComponentResponse.internal(r);
		}
	}

	private Category getDestination() {
		// Priority 1: Parameter
		Category c = getParameterCategory();
		if (c != null) {
			return c;
		}
		if (preferNavigationKey && navigationKey != null && navigationKey.isCategory()) {
			return navigationKey.getCategory();
		}
		return destination;
	}

	private Category getParameterCategory() {
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
			return getCategories().get(id);
		}
		return null;
	}

	@Extract
	public NavigationKey getNavigationKey() {
		return navigationKey;
	}

	@Extract
	public ContentKey getContentKey() {
		return contentKey;
	}
}
