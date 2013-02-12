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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.gui.client.widget.top.UserInfoPanel;

/**
 * The PMS Top Panel with the navigation and the user's info panles
 * @author Manuel Ruiz
 *
 */
public class PmsTopPanel extends HorizontalPanel {
	
	/** Navigation panel */
	private NavigationPanel navigationPanel = null;
	
	/** Current User Information Panel */
	private UserInfoPanel userInfoPanel = null;
	
	@Override
	protected void beforeRender() {
		initThis();
		addComponents();
	}
	
	private void initThis() {
		setTableWidth(Constants.HUNDRED_PERCENT);
	}

	private void addComponents() {
		
		add(navigationPanel);
		TableData td = new TableData("300px", "100%");
		td.setHorizontalAlign(HorizontalAlignment.RIGHT);
		add(userInfoPanel, td);
	}

	/**
	 * @param navigationPanel the navigationPanel to set
	 */
	@Inject
	public void setNavigationPanel(NavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}

	/**
	 * @param userInfoPanel the userInfoPanel to set
	 */
	@Inject
	public void setUserInfoPanel(UserInfoPanel userInfoPanel) {
		this.userInfoPanel = userInfoPanel;
	}

}
