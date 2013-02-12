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

package com.isotrol.impe3.gui.common.widget;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tab items with only one child are very frequent in our apps.<br/>
 * On the other way, assigning them a {@link FitLayout} is a must and is frequently omitted.<br/>
 * This widget represents such tabs: once created, they may be directly inserted into a tab panel
 * without any layout problems.
 * 
 * @author Andrei Cojocaru
 *
 */
public class TabItemWithUniqueChild extends TabItem {
	/**
	 * Constructor provided with a title for the tab and its content widget.
	 */
	public TabItemWithUniqueChild(String title, Widget widget) {
		super(title);
		setLayout(new FitLayout());
		add(widget);
	}
}
