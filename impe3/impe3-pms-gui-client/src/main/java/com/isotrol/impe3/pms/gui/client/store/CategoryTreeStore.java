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

package com.isotrol.impe3.pms.gui.client.store;


import java.util.List;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.CategorySelModelData;


/**
 * Tree store for a category tree.
 * @author Andres Rodriguez
 */
public class CategoryTreeStore extends TreeStore<CategorySelModelData> {

	/**
	 * Default constructor.<br/>
	 */
	public CategoryTreeStore() {
	}

	/**
	 * Constructor
	 * @param tree the DTO to store
	 */
	public CategoryTreeStore(CategoryTreeDTO tree) {
		add(tree, true);
	}

	/**
	 * Adds the passed node and all its descendants to the store.<br/>
	 * @param tree
	 */
	public final void add(CategoryTreeDTO tree, boolean addChildren) {
		if (tree != null) {
			CategorySelModelData root = new CategorySelModelData(tree.getNode());
			add(root, false);
			if (addChildren) {
				load(root, tree);
			}			
		}
	}

	/**
	 * Loads the children of the passed tree node.<br/>
	 * @param parent parent node for the passed subtree.
	 * @param tree subtree for the passed parent.
	 */
	private void load(CategorySelModelData parent, CategoryTreeDTO tree) {
		final List<CategoryTreeDTO> children = tree.getChildren();
		if (children == null || children.isEmpty()) {
			return;
		}
		for (final CategoryTreeDTO child : children) {
			final CategorySelModelData model = new CategorySelModelData(child.getNode());
			add(parent, model, false);
			load(model, child);
		}
	}
}
