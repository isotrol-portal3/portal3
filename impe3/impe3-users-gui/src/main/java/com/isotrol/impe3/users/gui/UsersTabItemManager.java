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

package com.isotrol.impe3.users.gui;


import com.extjs.gxt.ui.client.widget.TabPanel;

import com.google.gwt.user.client.ui.Widget;


/**
 * Manege the central tabPanel's items
 * 
 * @author Manuel Ruiz
 *
 */
public final class UsersTabItemManager extends TabPanel {
	
	public UsersTabItemManager() {
		setTabScroll(true);
		setBorderStyle(false);
		setBodyBorder(false);
	}

	/**
	 * @param widget	Widget por añadir
	 * @param title	Titulo tab
	 * 
	 */
	public void addTabItem(Widget widget, String title) {

		removeAll();
		UsersTabItem newTabItem = new UsersTabItem(title, widget);
		add(newTabItem);
	}
	
}
