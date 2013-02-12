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

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.common.util.Messages;
import com.isotrol.impe3.users.gui.users.UserManagementMenu;


/**
 * Componente que implemente el menú izquierdo.
 * 
 * @author Manuel Ruiz
 * 
 */
public class LeftMenu extends ContentPanel {

	/**
	 * Logo container<br/>
	 */
	private LayoutContainer pLogo = null;
	
	/**
	 * Menu container.<br/>
	 */
	private LayoutContainer pMenu = null;
	
	/**
	 * Messages service.<br/>
	 */
	private Messages messages = null;

	/**
	 * The users management menu<br/>
	 */
	private UserManagementMenu userManagementMenu = null;
	
	/**
	 * Constructor
	 */
	public LeftMenu() {}
	
	/**
	 * Inits the widget. Must be called after the dependences injection.
	 */
	public void init() {
		configComponent();
		initComponent();
	}

	/**
	 * Configs component properties.<br/>
	 */
	private void configComponent() {
		setLayout(new RowLayout());
		setHeaderVisible(false);
	}

	/**
	 * Configs this container inner components.<br/>
	 */
	private void initComponent() {

		pLogo = new LayoutContainer();
		pLogo.setLayout(new CenterLayout());
		pLogo.addStyleName("menu-logo-icon");
		Text title = new Text(messages.frameworkDisplayName());
		title.addStyleName("menu-logo-title");
		pLogo.add(title);
		add(pLogo);
		
		pMenu = new LayoutContainer(new AccordionLayout());
		add(pMenu);
		
		// System management
		createUserManagementMenu();
	}

	/**
	 * Creates the user menu.<br/>
	 */
	private void createUserManagementMenu() {

		ContentPanel cpUsersManagementMenu = new ContentPanel();
		cpUsersManagementMenu.setHeading(messages.menuItem1SystemAdministration());
		//cpUsersManagementMenu.setIconStyle(PmsStyles.ADMINICON);
		cpUsersManagementMenu.setAutoHeight(true);
		cpUsersManagementMenu.setBodyBorder(false);
		cpUsersManagementMenu.getHeader().addStyleName("level1-left-panel");
		pMenu.add(cpUsersManagementMenu);

		ContentPanel userContentList = new ContentPanel();
		userContentList.setHeaderVisible(false);
		userContentList.setBodyBorder(false);

		userManagementMenu.init();
		userContentList.setLayout(new FitLayout());
		userContentList.add(userManagementMenu);
		cpUsersManagementMenu.add(userContentList);
	}
	
	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(Messages messages) {
		this.messages = messages;
	}
	
	/**
	 * Injects the users management menu.
	 * @param userManagementMenu
	 */
	@Inject
	public void setUserManagementMenu(UserManagementMenu userManagementMenu) {
		this.userManagementMenu = userManagementMenu;
	}
}
