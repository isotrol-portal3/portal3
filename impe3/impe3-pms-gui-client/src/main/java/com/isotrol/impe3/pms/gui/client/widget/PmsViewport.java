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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget;


import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Util;


/**
 * Handles graphic elements shared between all PMS apps: PMS itself, PortalUsers, NodesRepository etc
 * 
 * @author Andrei Cojocaru
 * 
 */
public class PmsViewport extends Viewport implements IInitializableWidget {

	/**
	 * supports {@link IInitializableWidget#isInitialized()}<br/>
	 */
	private boolean initialized = false;

	/**
	 * Width in pixels for the left panel width<br/>
	 */
	private static final int LEFT_PANEL_WIDTH = 250;

	/**
	 * Margin between panels.<br/>
	 */
	protected static final int PANEL_MARGIN = 5;

	/**
	 * Height of the top panel.<br/>
	 */
	private static final int TOP_PANEL_HEIGHT = 20;

	/*
	 * Injected deps
	 */
	/**
	 * Utilities<br/>
	 */
	private Util util = null;
	/**
	 * Left menu widget.<br/>
	 */
	private LeftMenu leftPanel = null;

	/**
	 * Center widget.<br/>
	 */
	private PmsTabItemManager centerWidget = null;

	/** Top Widget */
	private PmsTopPanel topPanel = null;

	/**
	 * Default constructor.
	 */
	public PmsViewport() {
		setLayout(new BorderLayout());
		setLayoutOnChange(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.ISimpleInitializableWidget#init()
	 */
	/**
	 * <br/>
	 */
	public PmsViewport init() {

		util.mask();

		addTopPanel();
		addLeftMenu();
		addCenterWidget();
		initialized = true;

		util.unmask();

		return this;
	}

	/**
	 * Adds the top panel. The single navigation panel and the current logged user info
	 */
	private void addTopPanel() {

		if (isNorthVisible()) {
			final BorderLayoutData topLayoutData = new BorderLayoutData(LayoutRegion.NORTH);
			topLayoutData.setMargins(new Margins(PANEL_MARGIN, PANEL_MARGIN, 0, PANEL_MARGIN));
			topLayoutData.setSize(TOP_PANEL_HEIGHT);
			add(topPanel, topLayoutData);
		}
	}

	/**
	 * Adds the passed widget to the left.<br/>
	 */
	protected void addLeftMenu() {

		if (!isInitialized()) {
			ALeftPanel lp = getLeftPanel();
			Widget widget = lp.init();

			final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST);
			leftLayoutData.setSplit(true);
			leftLayoutData.setCollapsible(true);
			leftLayoutData.setMargins(new Margins(PANEL_MARGIN));
			leftLayoutData.setSize(LEFT_PANEL_WIDTH);

			add(widget, leftLayoutData);
		}
	}

	protected ALeftPanel getLeftPanel() {
		return leftPanel;
	}

	protected ICenterWidget getCenterWidget() {
		return centerWidget;
	}

	/**
	 * Adds the central widget.<br/>
	 */
	protected void addCenterWidget() {

		if (!isInitialized()) {
			ICenterWidget cw = getCenterWidget();
			Widget widget = cw.init();

			final BorderLayoutData centerLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
			centerLayoutData.setMargins(new Margins(PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN));

			LayoutContainer lc = new LayoutContainer(new FitLayout());
			lc.setBorders(true);
			lc.add(widget);

			add(lc, centerLayoutData);
		}
	}

	protected boolean isNorthVisible() {
		return true;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param leftPanel the leftPanel to set
	 */
	@Inject
	public void setLeftPanel(LeftMenu leftPanel) {
		this.leftPanel = leftPanel;
	}

	/**
	 * @param centerWidget the centerWidget to set
	 */
	@Inject
	public void setCenterWidget(PmsTabItemManager centerWidget) {
		this.centerWidget = centerWidget;
	}

	/**
	 * @param topPanel the topPanel to set
	 */
	@Inject
	public void setTopPanel(PmsTopPanel topPanel) {
		this.topPanel = topPanel;
	}
}
