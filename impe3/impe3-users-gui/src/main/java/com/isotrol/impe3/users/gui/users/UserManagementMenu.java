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

package com.isotrol.impe3.users.gui.users;


import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.users.gui.UsersTabItemManager;
import com.isotrol.impe3.users.gui.ioc.UsersAppFactory;
import com.isotrol.impe3.users.gui.util.UsersMessages;

/**
 * Shows the user admin menu
 * 
 * @author Manuel Ruiz
 * 
 */
public class UserManagementMenu extends LayoutContainer {

	/*
	 * HTML IDs for the menu items
	 */
	private static final String ID_MI_USERS_MANAGEMENT = "mi2_users_management";

	/*
	 * Injected deps
	 */
	/**
	 * Reference to users messages service.<br/>
	 */
	private UsersMessages usersMessages = null;
	
	/**
	 * The tab items manager.<br/>
	 */
	private UsersTabItemManager tabItemManager = null;
	
	/**
	 * Default constructor
	 */
	public UserManagementMenu() {}
	
	/**
	 * Inits the widget. Must be called after the dependences injection.
	 * 
	 * @return the same instance initialized
	 */
	public UserManagementMenu init() {
		configComponent();
		
		initComponent();
		
		return this;
	}

	/**
	 * Inits this container properties and inner components.
	 */
	private void initComponent() {
		final DataList adminMenuList = new DataList();

		Listener<ComponentEvent> listener = new Listener<ComponentEvent>() {

			public void handleEvent(ComponentEvent ce) {
				DataListItem selectedItem = adminMenuList.getSelectedItem();
				if (selectedItem != null && selectedItem.getId().equals(ID_MI_USERS_MANAGEMENT)) {
					// abrimos pestaña para gestion de usuarios
					UsersManagement widget = UsersAppFactory.getInstance().getUsersManagement().init();
					tabItemManager.addTabItem(widget, usersMessages.headerUsersManagementMenu());
				} 
			}
		};

		adminMenuList.setBorders(false);
		adminMenuList.setFlatStyle(true);
		adminMenuList.setSelectionMode(SelectionMode.SINGLE);
		
		// user management
		DataListItem itemUser = new DataListItem(usersMessages.headerUsersManagementMenu());
		itemUser.setId(ID_MI_USERS_MANAGEMENT);
		itemUser.setIconStyle("user-icon");
		itemUser.setBorders(false);
		adminMenuList.add(itemUser);

		adminMenuList.addListener(Events.OnClick, listener);
		add(adminMenuList);

	}

	private void configComponent() {
		setLayout(new FlowLayout());
		setAutoHeight(true);
	}
	
	/**
	 * Injects the users app specific messages
	 * @param usersMessages
	 */
	@Inject
	public void setUsersMessages(UsersMessages usersMessages) {
		this.usersMessages = usersMessages;
	}
	
	/**
	 * Injects the tab items manager.
	 * @param tabItemManager
	 */
	@Inject
	public void setTabItemManager(UsersTabItemManager tabItemManager) {
		this.tabItemManager = tabItemManager;
	}
}
