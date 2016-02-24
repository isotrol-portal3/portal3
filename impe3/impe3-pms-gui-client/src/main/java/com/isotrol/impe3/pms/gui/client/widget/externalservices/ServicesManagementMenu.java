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

package com.isotrol.impe3.pms.gui.client.widget.externalservices;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.AManagementMenu;


/**
 * Shows the external services admin menu
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class ServicesManagementMenu extends AManagementMenu {

	/*
	 * HTML IDs for the menu items
	 */
	/**
	 * "Users Services" item ID.<br/>
	 */
	private static final String ID_MI_USERS_SERVICES = "mi2_users_services";

	/**
	 * "Nodes Repository" item ID.<br/>
	 */
	private static final String ID_MI_NODES_REPOSITORY_SERVICES = "mi2_nodes_repository";
	
	
	private static final String ID_MI_INDEXADORES_SERVICE = "mi2_servicio_indexadores";

	/**
	 * "Nodes Repository" item ID.<br/>
	 */
	private static final String ID_MI_COMMENT_SERVICES = "mi2_comments";

	private ListView<MenuItemModelData> adminMenuList = null;

	/**
	 * Constructor
	 */
	public ServicesManagementMenu() {
	}

	/**
	 * Inits the widget. Must be called after the properties are injected through IoC.
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
		setIconStyle(getPmsStyles().menuIconServicesManagement());
		setBodyBorder(false);

		setHeadingText(getPmsMessages().menuItem1Services());
		getHeader().addStyleName(getGuiCommonStyles().noSideBorders());
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		adminMenuList = new ListView<MenuItemModelData>();
		adminMenuList.setStore(new ListStore<MenuItemModelData>());
		adminMenuList.setSimpleTemplate(getSettings().tplListView());
		adminMenuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		Listener<ComponentEvent> listener = new Listener<ComponentEvent>() {

			public void handleEvent(ComponentEvent ce) {
				ModelData selectedItem = adminMenuList.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					showWidget((String) selectedItem.get(MenuItemModelData.PROPERTY_ID));
				}
			}
		};

		adminMenuList.setBorders(false);

		PmsMessages pmsMessages = getPmsMessages();

		// user management
		if (getPmsUtil().isPortalUsersServiceVisible()) {
			MenuItemModelData itemUser = new MenuItemModelData(pmsMessages.menuItem2UsersServices(),
				getGuiCommonStyles().iUser(), ID_MI_USERS_SERVICES);
			adminMenuList.getStore().add(itemUser);
		}

		// node repositories
		if (getPmsUtil().isNodeRepositoryVisible()) {
			MenuItemModelData itemNr = new MenuItemModelData(pmsMessages.menuItem2NodesRepository(), getPmsStyles()
				.menuIconNodesRepository(), ID_MI_NODES_REPOSITORY_SERVICES);
			adminMenuList.getStore().add(itemNr);
		}
		//indexers management
		if (getPmsUtil().isIndexersServiceVisible()) {
			MenuItemModelData itemNr = new MenuItemModelData(pmsMessages.menuItem2Indexers(), getPmsStyles().menuIconIndexersRepository(), ID_MI_INDEXADORES_SERVICE);
			adminMenuList.getStore().add(itemNr);
		}

		// comments service
		if (getPmsUtil().isCommentsServiceVisible()) {
			MenuItemModelData itemComments = new MenuItemModelData(pmsMessages.menuItem2Comments(), getPmsStyles()
				.menuIconComments(), ID_MI_COMMENT_SERVICES);
			adminMenuList.getStore().add(itemComments);
		}

		adminMenuList.addListener(Events.OnClick, listener);

		add(adminMenuList);
	}

	private void showWidget(String type) {
		String clazz = null;
		Object data = null;
		String title = null;
		PmsMessages pmsMessages = getPmsMessages();
		if (type.equals(ID_MI_USERS_SERVICES)) {
			// abrimos pestaña para gestion de usuarios
			clazz = PortalUsersManagement.class.toString();
			title = pmsMessages.menuItem2UsersServices();
		} else if (type.equals(ID_MI_NODES_REPOSITORY_SERVICES)) {
			clazz = NodesRepositoryManagement.class.toString();
			title = pmsMessages.menuItem2NodesRepository();
		}else if (type.equals(ID_MI_INDEXADORES_SERVICE)){
			clazz = IndexersManagement.class.toString();
			title = pmsMessages.menuItem2Indexers();
		
		} else if (type.equals(ID_MI_COMMENT_SERVICES)) {
			clazz = CommentsExternalServiceManagement.class.toString();
			title = pmsMessages.menuItem2Comments();
		} 
		getTabItemManager().closeAllAndAddWidget(clazz, data, title);
}

	@Override
	public ListView<MenuItemModelData> getMenuListView() {
		return adminMenuList;
	}
}
