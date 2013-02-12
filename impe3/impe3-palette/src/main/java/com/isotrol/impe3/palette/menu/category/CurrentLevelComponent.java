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

package com.isotrol.impe3.palette.menu.category;


import java.util.UUID;

import com.google.common.base.Predicate;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.palette.menu.MenuItem;


/**
 * Current level (siblings) of categories menu.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class CurrentLevelComponent extends CategoryComponent {
	/**
	 * Public constructor.
	 */
	public CurrentLevelComponent() {
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		final Categories categories = getCategories();
		final Category root = categories.getRoot();
		Iterable<Category> level = categories.getChildren(root.getId());
		Category c = getCurrent();
		MenuItem parent = null;
		final Predicate<Category> selected = selected();
		if (c != null) {
			final UUID pk = categories.getParentKey(c.getId());
			if (pk != null) {
				level = categories.getChildren(pk);
				parent = createParentItem(c, selected);
			}
		}
		add(level, selected, parent);
	}
}
