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


import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.util.Logger;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.ServicesManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.help.HelpMenu;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.InformationArchitectureMenu;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.SystemManagementMenu;


/**
 * Componente que implementa el menú izquierdo.
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class LeftMenu extends ALeftPanel {

	private List<ListView<MenuItemModelData>> menusList = null;
	/*
	 * Injected objects:
	 */
	/**
	 * Logger object.<br/>
	 */
	private Logger logger = null;
	/**
	 * PMS specific pmsSettings bundle.<br/>
	 */
	private PmsSettings pmsSettings = null;
	/**
	 * System Management menu item<br/>
	 */
	private SystemManagementMenu systemManagementMenu = null;

	/**
	 * Information Architecture menu item<br/>
	 */
	private InformationArchitectureMenu informationArchitectureMenu = null;

	/**
	 * Portal Management menu item.<br/>
	 */
	private PortalManagementMenu portalManagementMenu = null;

	/**
	 * External Services Management menu item<br/>
	 */
	private ServicesManagementMenu servicesManagementMenu = null;
	
	/**
	 * Pms Help menu item
	 */
	private HelpMenu helpMenu = null;

	/**
	 * @see com.isotrol.impe3.pms.gui.common.widget.ALeftPanel#init(java.lang.Object)
	 */
	@Override
	public Widget init() {
		// assert sessionDto != null : "A SessionDTO object must be provided before initializing the left menu";

		super.init();

		return this;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.widget.ALeftPanel
	 * #configMenuPanel(com.extjs.gxt.ui.client.widget.LayoutContainer)
	 */
	protected void configMenuPanel(LayoutContainer pMenu) {

		menusList = new ArrayList<ListView<MenuItemModelData>>();

		// System management
		systemManagementMenu.init();
		pMenu.add(systemManagementMenu);
		congifMenuListener(systemManagementMenu.getMenuListView());

		// information architecture
		informationArchitectureMenu.init();
		pMenu.add(informationArchitectureMenu);
		congifMenuListener(informationArchitectureMenu.getMenuListView());

		// Create the portal management menu:
		portalManagementMenu.init();
		pMenu.add(portalManagementMenu);
		congifMenuListener(portalManagementMenu.getMenuListView());

		// Create the services management menu:
		servicesManagementMenu.init();
		pMenu.add(servicesManagementMenu);
		congifMenuListener(servicesManagementMenu.getMenuListView());
		
		// Create the help menu:
		helpMenu.init();
		pMenu.add(helpMenu);
		congifMenuListener(helpMenu.getMenuListView());

		// Log Panel como un Item de menú más
		// solo si debugEnabled = true
		if (Boolean.valueOf(pmsSettings.isDebugMode())) {
			addLogPanel(pMenu);
		}

	}

	/**
	 * To deselect the others menus
	 * @param menuListView the menu that stay open and with the selected item
	 */
	private void congifMenuListener(final ListView<MenuItemModelData> menuListView) {
		if (menuListView != null) {
			menusList.add(menuListView);
			menuListView.addListener(Events.OnClick, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					for (ListView<MenuItemModelData> l : menusList) {
						if (!l.equals(menuListView)) {
							l.getSelectionModel().deselectAll();
						}
					}
				}
			});
		}
	}

	/**
	 * <br/>
	 * @param pMenu
	 */
	private void addLogPanel(LayoutContainer pMenu) {
		ContentPanel logMenu = new ContentPanel();
		logMenu.setHeading("Log");
		logMenu.setAutoHeight(true);
		logMenu.setBodyBorder(false);
		logMenu.getHeader().addStyleName(getStyles().menuLevel1());
		pMenu.add(logMenu);

		VerticalPanel logPanel = new VerticalPanel();
		logPanel.setSpacing(5);
		logPanel.setScrollMode(Scroll.ALWAYS);
		logMenu.add(logPanel);

		logger.setLogPanel(logPanel);

	}

	/**
	 * Injects the System Management menu item
	 * @param systemManagementMenu
	 */
	@Inject
	public void setSystemManagementMenu(SystemManagementMenu systemManagementMenu) {
		this.systemManagementMenu = systemManagementMenu;
	}

	/**
	 * Injects the Information Architecture menu item.
	 * @param menu
	 */
	@Inject
	public void setInformatinArchitectureMenu(InformationArchitectureMenu menu) {
		this.informationArchitectureMenu = menu;
	}

	/**
	 * Injects the Portal Management menu item.
	 * @param portalManagementMenu
	 */
	@Inject
	public void setPortalManagementMenu(PortalManagementMenu portalManagementMenu) {
		this.portalManagementMenu = portalManagementMenu;
	}

	/**
	 * Injects the External Services Management menu item
	 * @param servicesManagementMenu
	 */
	@Inject
	public void setServicesManagementMenu(ServicesManagementMenu servicesManagementMenu) {
		this.servicesManagementMenu = servicesManagementMenu;
	}

	/**
	 * Injects the PMS specific pmsSettings bundle.
	 * @param settings
	 */
	@Inject
	public void setSettings(PmsSettings settings) {
		this.pmsSettings = settings;
	}

	/**
	 * @param logger
	 */
	@Inject
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	public boolean isInitialized() {
		return false;
	}

	/**
	 * Injects the PMS specific settings bundle.<br/>
	 * @param pmsSettings
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
	}

	/**
	 * @param helpMenu the helpMenu to set
	 */
	@Inject
	public void setHelpMenu(HelpMenu helpMenu) {
		this.helpMenu = helpMenu;
	}
}
