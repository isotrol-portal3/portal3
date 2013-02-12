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

package com.isotrol.impe3.pms.gui.client.widget.help;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.user.client.Window;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.gui.common.widget.IframePanel;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.widget.AManagementMenu;


/**
 * Shows the pms help menu menu
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class HelpMenu extends AManagementMenu {

	/*
	 * HTML IDs for menu items.
	 */
	/**
	 * "Watch handbook" menu item ID.<br/>
	 */
	private static final String ID_MI_WATCH_MANUAL = "mi2_watch_manual";
	/**
	 * "Open handbook" menu item ID.<br/>
	 */
	private static final String ID_MI_OPEN_MANUAL = "mi2_open_manual";

	private ListView<MenuItemModelData> lvAdminMenu = null;

	/**
	 * Constructor
	 */
	public HelpMenu() {
	}

	/**
	 * Inits the widget. Must be called after the dependencies injection.
	 */
	public void init() {
		initThis();
		initComponents();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setAutoHeight(true);
		setIconStyle(getPmsStyles().menuIconHelp());
		setBodyBorder(false);

		setHeading(getPmsMessages().menuItem1Help());
		getHeader().addStyleName(getGuiCommonStyles().noSideBorders());
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		lvAdminMenu = new ListView<MenuItemModelData>();
		lvAdminMenu.setSimpleTemplate(getSettings().tplListView());
		lvAdminMenu.setStore(new ListStore<MenuItemModelData>());
		lvAdminMenu.setBorders(false);
		lvAdminMenu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		Listener<ComponentEvent> listener = new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent ce) {
				MenuItemModelData selectedItem = lvAdminMenu.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					PmsMessages pmsMessages = getPmsMessages();
					String id = selectedItem.get(MenuItemModelData.PROPERTY_ID);
					if (id.equals(ID_MI_WATCH_MANUAL)) {
						// open tab panel with the Pms handbook in a iframe
						IframePanel iframe = PmsFactory.getInstance().getIframePanel();
						iframe.setIframeUrl(getSettings().pmsAdminManualUrl());
						getTabItemManager().addTabItemAndCloseAll(iframe, pmsMessages.headerPmsHandbook());

					} else if (id.equals(ID_MI_OPEN_MANUAL)) {
						// open Pms handbook in a new window
						Window.open(getSettings().pmsAdminManualUrl(), pmsMessages.headerPmsHandbook(),
							PmsConstants.NEW_WINDOW_FEATURES);
					}
				}
			}
		};

		PmsMessages pmsMessages = getPmsMessages();
		PmsStyles pmsStyles = getPmsStyles();

		// watch handbook
		MenuItemModelData item = new MenuItemModelData(pmsMessages.menuItem2WatchHandbook(), pmsStyles.menuIconHelp(),
			ID_MI_WATCH_MANUAL);
		lvAdminMenu.getStore().add(item);

		// open handbook
		item = new MenuItemModelData(pmsMessages.menuItem2OpenHandbook(), pmsStyles.menuIconHelp(), ID_MI_OPEN_MANUAL);
		lvAdminMenu.getStore().add(item);

		lvAdminMenu.addListener(Events.OnClick, listener);

		add(lvAdminMenu);
	}

	@Override
	public ListView<MenuItemModelData> getMenuListView() {
		return lvAdminMenu;
	}

}
