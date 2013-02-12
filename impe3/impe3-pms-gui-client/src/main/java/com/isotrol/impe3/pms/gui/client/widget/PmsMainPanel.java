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

package com.isotrol.impe3.pms.gui.client.widget;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;

/**
 * Pms main panel with {@link CardLayout} where the viewports (Main, Node Repositories an Users) are added
 * @author Manuel Ruiz
 *
 */
public class PmsMainPanel extends ContentPanel {
	
	/**
	 * The PMSMainPanel layout
	 */
	private CardLayout layout = null;

	@Override
	protected void beforeRender() {
		initThis();
	}

	/**
	 * Configures this
	 */
	private void initThis() {
		setHeaderVisible(false);
		layout = new CardLayout();
		setLayout(layout);
	}
	
	/**
	 * Sets the active viewport in the main panel. In CardLayout only one component is active
	 * @param viewport
	 */
	public void activateViewport(Viewport viewport) {
		layout.setActiveItem(viewport);
	}
}
