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

package com.isotrol.impe3.palette.breadcrumb;


import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;


/**
 * Abstract class for breadcrumb components with navigation.
 * 
 * @author Andres Rodriguez
 */
abstract class WithNavigationBreadcrumbComponent extends AbstractBreadcrumbComponent {
	/** Navigation key. */
	private NavigationKey navigationKey;

	/**
	 * Constructor.
	 */
	public WithNavigationBreadcrumbComponent() {
	}
	
	final NavigationKey getNavigationKey() {
		return navigationKey;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	/**
	 * Adds items related to navigation.
	 * 
	 * @param navigationKey Navigation to add.
	 * @param category True if the category navigation must be included.
	 * @param contentType True if the content type navigation must be included.
	 */
	final void addNavigation(boolean category, boolean contentType) {
		add(navigationKey, category, contentType);
	}

	public void edit() {
		prepare();
		final BreadcrumbConfig config = getConfig();
		final boolean cg = Boolean.TRUE.equals(config.withCategoryNav());
		final boolean ct = Boolean.TRUE.equals(config.withContentTypeNav());
		add(navigationKey, cg, ct);
	}

	public final ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

}
