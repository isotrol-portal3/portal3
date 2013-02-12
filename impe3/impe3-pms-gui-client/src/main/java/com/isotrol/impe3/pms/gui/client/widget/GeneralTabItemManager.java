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


import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.gui.common.util.Constants;


/**
 * Manage the central tabPanel's items
 * 
 * @author Manuel Ruiz
 * 
 */
public class GeneralTabItemManager extends TabPanel implements ICenterWidget {

	/**
	 * support for {@link IInitializableWidget}.<br/>
	 */
	private boolean initialized = false;

	/**
	 * Constructor
	 */
	public GeneralTabItemManager() {
		setTabScroll(true);
		setBorderStyle(false);
		setBodyBorder(false);
		setLayoutOnChange(true);
		// this id will be used to show the info panel
		setId(Constants.CENTER_PANEL_ID);
	}

	/**
	 * @param widget Widget to add
	 * @param title Tab title
	 * 
	 */
	public TabItem addTabItem(Widget widget, String title) {

		return addTabItem(widget, title, true);
	}

	/**
	 * @param widget Widget to add
	 * @param title Tab title
	 * @param closeOthers if other tab items will be closed (default false)
	 * 
	 */
	public TabItem addTabItem(Widget widget, String title, boolean closeOthers) {

		if (closeOthers) {
			removeAll();
		}

		GeneralTabItem newTabItem = new GeneralTabItem(title, widget);
		add(newTabItem);
		setSelection(newTabItem);

		return newTabItem;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget#init()
	 */
	public Widget init() {
		initialized = true;

		return this;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
