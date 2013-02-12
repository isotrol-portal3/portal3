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

package com.isotrol.impe3.palette.transform.inpage;


import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;


/**
 * Change category navigation component.
 * @author Emilio Escobar Reyero
 */
public class CategoryNavigationComponent implements Component {
	/** Navigation key. */
	private NavigationKey navigationKey;
	/** Component configuration. */
	private CategoryNavigationConfig config;

	/**
	 * Constructor.
	 */
	public CategoryNavigationComponent() {
	}

	@Inject
	public void setConfig(CategoryNavigationConfig config) {
		this.config = config;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	/** Component execution. */
	public ComponentResponse execute() {
		if (config == null) {
			return ComponentResponse.OK;
		}
		final Category category = config.category();
		final boolean keepContentType = config.keepContentType().booleanValue();
		
		if (navigationKey == null) {
			if (category != null) {
				navigationKey = NavigationKey.category(category);
			}
		} else {
			if (keepContentType && navigationKey.isContentType()) {
				navigationKey = NavigationKey.category(category, navigationKey.getContentType());
			} else if (category != null) {
				navigationKey = NavigationKey.category(category);
			} else {
				navigationKey = null;
			}
		}
		return ComponentResponse.OK;
	}

	@Extract
	public NavigationKey getNavigationKey() {
		return navigationKey;
	}
}
