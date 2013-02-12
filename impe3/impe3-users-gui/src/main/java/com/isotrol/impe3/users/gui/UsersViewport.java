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

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * Main container used in users app.
 * 
 * @author Manuel Ruiz
 *
 */
@Singleton
public class UsersViewport extends Viewport {

	private static final int PANEL_MARGIN = 5;
	private static final int BOTTOM_PANEL_HEIGHT = 40;
	private static final int LEFT_PANEL_WIDTH = 250;
	
	private ContentPanel bottomPanel = null;
	
	/*
	 * Injected deps.
	 */
	private LeftMenu leftMenu = null;
	
	/**
	 * Tab items manager.<br/>
	 */
	private UsersTabItemManager tabPanel = null;
	
	/**
	 * Constructor
	 */
	public UsersViewport() {}
	
	/**
	 * Inits the widget. Must be called after the properties injection.
	 */
	public void init() {
		configThis();
		initThis();
	}

	private void initThis() {
		// Left Panel
		leftMenu.init();

		final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST);
		// leftLayoutData.setSplit(true);
		// leftLayoutData.setCollapsible(true);
		leftLayoutData.setMargins(new Margins(PANEL_MARGIN));
		leftLayoutData.setSize(LEFT_PANEL_WIDTH);

		add(leftMenu,leftLayoutData);

		// Bottom Panel
		bottomPanel = new ContentPanel();
		bottomPanel.setHeaderVisible(false);

		final BorderLayoutData bottomLayoutData = new BorderLayoutData(LayoutRegion.SOUTH);
		bottomLayoutData.setMargins(new Margins(0, PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN));
		bottomLayoutData.setSize(BOTTOM_PANEL_HEIGHT);

		add(bottomPanel, bottomLayoutData);

		// Center Panel: LayoutContainer (for borders) + TabPanel inside
		LayoutContainer lc = new LayoutContainer(new FitLayout());
		lc.setBorders(true);

		lc.add(tabPanel);
		
		final BorderLayoutData centerLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
		centerLayoutData.setMargins(new Margins(PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN, 0));

		add(lc, centerLayoutData);
	}

	private void configThis() {
		setLayout(new BorderLayout());	
	}
	
	/**
	 * Injects the left menu
	 * @param leftMenu
	 */
	@Inject
	public void setLeftMenu(LeftMenu leftMenu) {
		this.leftMenu = leftMenu;
	}

	/**
	 * Injects the tab item manager.
	 * @param tabPanel
	 */
	@Inject
	public void setTabPanel(UsersTabItemManager tabPanel) {
		this.tabPanel = tabPanel;
	}
}
