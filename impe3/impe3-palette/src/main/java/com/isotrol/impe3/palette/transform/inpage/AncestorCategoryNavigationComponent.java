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


import java.util.List;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;


/**
 * Change category navigation to an ancestor component.
 * @author Andres Rodriguez
 */
public class AncestorCategoryNavigationComponent implements Component {
	/** Navigation key. */
	private NavigationKey navigationKey;
	/** Component configuration. */
	private AncestorCategoryNavigationConfig config;
	/** Categories. */
	private Categories categories;

	/**
	 * Constructor.
	 */
	public AncestorCategoryNavigationComponent() {
	}

	@Inject
	public void setConfig(AncestorCategoryNavigationConfig config) {
		this.config = config;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	@Inject
	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	/** Component execution. */
	public ComponentResponse execute() {
		if (config == null || categories == null || navigationKey == null || !navigationKey.isCategory()) {
			return ComponentResponse.OK;
		}
		final int distance = config.distance();
		if (distance < 1) {
			return ComponentResponse.OK;
		}
		final Category current = navigationKey.getCategory();
		final Category category = config.category();
		if (!categories.containsKey(current.getId()) || !categories.containsKey(category.getId())) {
			return ComponentResponse.OK;
		}
		final List<Category> list = Lists.newLinkedList();
		list.add(current);
		Category p = current;
		while ((p = categories.getParent(p.getId())) != null) {
			if (p.equals(category)) {
				if (list.isEmpty()) {
					return ComponentResponse.OK;
				} else {
					goTo(list.get(Math.min(list.size() - 1, distance - 1)));
				}
			} else {
				list.add(0, p);
			}
		}
		return ComponentResponse.OK;
	}

	private void goTo(Category category) {
		final boolean keepContentType = config.keepContentType().booleanValue();
		if (keepContentType && navigationKey.isContentType()) {
			navigationKey = NavigationKey.category(category, navigationKey.getContentType());
		} else if (category != null) {
			navigationKey = NavigationKey.category(category);
		} else {
			navigationKey = null;
		}
	}

	@Extract
	public NavigationKey getNavigationKey() {
		return navigationKey;
	}
}
