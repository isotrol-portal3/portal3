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

package com.isotrol.impe3.pms.gui.client.widget.design;

import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsDependencesTree;

/**
 * Right Panel with Components Dependences Tree
 * 
 * @author Manuel Ruiz
 */
public class ComponentsTreePanel extends ContentPanel {

	/** the page to design */
	private PageTemplateDTO page;

	/**
	 * Components dependences tree.<br/>
	 */
	private ComponentsDependencesTree componentsDependencesTree = null;

	/**
	 * Constructor
	 */
	public ComponentsTreePanel() {}

	/**
	 * Inits the widget. Must be explicitly called after dependences are injected.
	 * @param pageDto
	 */
	public void init(PageTemplateDTO pageDto) {
		this.page = pageDto;
		initComponent();
	}

	/**
	 * Inits the GUI layer of the widget.
	 */
	private void initComponent() {

		setScrollMode(Scroll.AUTO);

		List<ComponentInPageTemplateDTO> components = page.getComponents();
		componentsDependencesTree.init(components);

		add(componentsDependencesTree);
	}
	
	/**
	 * Injects the components dependences tree.
	 * @param componentsDependencesTree
	 */
	@Inject
	public void setComponentsDependencesTree(
			ComponentsDependencesTree componentsDependencesTree) {
		this.componentsDependencesTree = componentsDependencesTree;
	}
}
