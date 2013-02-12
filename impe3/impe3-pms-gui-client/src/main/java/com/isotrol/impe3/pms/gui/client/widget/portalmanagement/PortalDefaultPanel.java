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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.user.client.ui.Image;

/**
 * This panel will be load in portal management by default
 * @author Manuel Ruiz
 *
 */
public class PortalDefaultPanel extends LayoutContainer {
	
	@Override
	protected void beforeRender() {
		addComponents();
	}
	
	/**
	 * Add a "engine" image to this panel
	 */
	private void addComponents() {
		add(new Image("img/engineBig.png"));
		// if sets the layout before add the image, the image is not centered in the first load
		setLayout(new CenterLayout());
	}
}
