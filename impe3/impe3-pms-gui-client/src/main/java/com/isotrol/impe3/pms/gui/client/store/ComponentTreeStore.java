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
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ComponentInPageTemplateModelData;


/**
 * Tree store for a component in page
 * @author Manuel Ruiz
 */
public class ComponentTreeStore extends TreeStore<ComponentInPageTemplateModelData> {

	/**
	 * Default constructor.<br/>
	 */
	public ComponentTreeStore() {
	}

	/**
	 * Constructor
	 * @param tree the DTO to store
	 */
	public ComponentTreeStore(ComponentInPageTemplateDTO tree) {
		doAdd(tree, true);
	}

	/**
	 * Adds the passed node and all its descendants to the store.<br/>
	 * @param tree
	 * @param addChildren
	 */
	public final void add(ComponentInPageTemplateDTO tree, boolean addChildren) {
		doAdd(tree, true);
	}

	/**
	 * Adds the passed node and all its descendants to the store.<br/>
	 * @param tree
	 * @param addChildren
	 */
	private final void doAdd(ComponentInPageTemplateDTO tree, boolean addChildren) {
		ComponentInPageTemplateModelData root = new ComponentInPageTemplateModelData(tree);
		add(root, false);
		if (addChildren) {
			load(root, tree.getChildren());
		}
	}

	/**
	 * Loads the children of the passed tree node.<br/>
	 * @param parent parent node for the passed subtree.
	 * @param children list of children for the passed parent.
	 */
	private final void load(ComponentInPageTemplateModelData parent, List<ComponentInPageTemplateDTO> children) {
		if (children == null || children.isEmpty()) {
			return;
		}
		for (final ComponentInPageTemplateDTO child : children) {
			final ComponentInPageTemplateModelData model = new ComponentInPageTemplateModelData(child);
			add(parent, model, false);
			model.setParent(parent);
			load(model, child.getChildren());
		}
	}

	/**
	 * If addChildren, add the complete subtree by hand
	 * @see com.extjs.gxt.ui.client.store.TreeStore#add(com.extjs.gxt.ui.client.data.ModelData,
	 * com.extjs.gxt.ui.client.data.ModelData, boolean)
	 */
	@Override
	public final void add(ComponentInPageTemplateModelData parent, ComponentInPageTemplateModelData item, 
		boolean addChildren) {

		super.add(parent, item, false);

		if (addChildren) {
			List<ComponentInPageTemplateDTO> children = item.getDTO().getChildren();
			if (children != null) {
				for (ComponentInPageTemplateDTO child : children) {
					add(item, new ComponentInPageTemplateModelData(child), true);
				}
			}
		}
	}
}
