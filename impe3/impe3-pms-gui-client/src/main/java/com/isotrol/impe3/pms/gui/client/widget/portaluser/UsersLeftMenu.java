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

package com.isotrol.impe3.pms.gui.client.widget.portaluser;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.pms.gui.client.i18n.UsersMessages;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.GeneralTabItemManager;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;


/**
 * Portal users service left panel
 * 
 * @author Manuel Ruiz
 * 
 */
public class UsersLeftMenu extends ALeftPanel {

	/*
	 * HTML IDs for the menu items
	 */
	private static final String ID_MI_USERS_MANAGEMENT = "mi2_users_management";

	/**
	 * supports {@link IInitializableWidget#isInitialized()}<br/>
	 */
	private boolean initialized = false;

	/**
	 * Messages service.<br/>
	 */
	private UsersMessages userMessages = null;

	/**
	 * The tab items manager.<br/>
	 */
	private GeneralTabItemManager tabItemManager = null;

	/**
	 * Styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;

	@Override
	protected void configMenuPanel(LayoutContainer container) {

		ContentPanel cpUsersManagementMenu = new ContentPanel();
		cpUsersManagementMenu.setHeading(userMessages.menuItem1SystemManagement());
		// cpUsersManagementMenu.setIconStyle(PmsStyles.ADMINICON);
		cpUsersManagementMenu.setAutoHeight(true);
		cpUsersManagementMenu.setBodyBorder(false);
		cpUsersManagementMenu.getHeader().addStyleName(styles.menuLevel1());
		container.add(cpUsersManagementMenu);

		final ListView<MenuItemModelData> adminMenuList = new ListView<MenuItemModelData>();
		adminMenuList.setSimpleTemplate(getSettings().tplListView());
		adminMenuList.setBorders(false);
		adminMenuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		adminMenuList.setStore(new ListStore<MenuItemModelData>());

		// user management
		MenuItemModelData itemUser = new MenuItemModelData(userMessages.headerUsersManagementMenu(), styles.iUser(),
			ID_MI_USERS_MANAGEMENT);
		adminMenuList.getStore().add(itemUser);

		adminMenuList.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<MenuItemModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<MenuItemModelData> se) {
				MenuItemModelData selectedItem = se.getSelectedItem();
				if (selectedItem != null
					&& ((String) selectedItem.get(MenuItemModelData.PROPERTY_ID)).equals(ID_MI_USERS_MANAGEMENT)) {
					// open users management tab item
					PortalUsersList widget = PmsFactory.getInstance().getPortalUsersList().init();
					tabItemManager.addTabItem(widget, userMessages.headerUsersManagementMenu());
				}
				
			}
		});

		cpUsersManagementMenu.add(adminMenuList);

		if(!adminMenuList.getSelectionModel().getSelectedItems().isEmpty()) {
			adminMenuList.getSelectionModel().deselectAll();
		}
		adminMenuList.getSelectionModel().select(itemUser, false);
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setUserMessages(UsersMessages messages) {
		this.userMessages = messages;
	}

	/**
	 * Injects the styles bundle.
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param tabItemManager the tabItemManager to set
	 */
	@Inject
	public void setTabItemManager(GeneralTabItemManager tabItemManager) {
		this.tabItemManager = tabItemManager;
	}
}
