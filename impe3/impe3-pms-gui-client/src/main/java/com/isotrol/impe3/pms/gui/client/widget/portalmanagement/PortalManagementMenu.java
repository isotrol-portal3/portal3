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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.widget.AManagementMenu;


/**
 * Portal management tree in left menu
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class PortalManagementMenu extends AManagementMenu {

	private static final String ID_PORTALS_TREE = "portal-tree-item";

	private ListView<MenuItemModelData> menuList = null;

	/* ********
	 * Injected deps:*******
	 */

	/**
	 * Constructor
	 */
	public PortalManagementMenu() {
	}

	/**
	 * Inits the widget. Must be called after the dependencies are injected.
	 */
	public void init() {
		initThis();
		addComponents();
	}

	private void addComponents() {
		menuList = new ListView<MenuItemModelData>();
		menuList.setStore(new ListStore<MenuItemModelData>());
		menuList.setSimpleTemplate(getSettings().tplListView());
		menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		Listener<ComponentEvent> listener = new Listener<ComponentEvent>() {

			public void handleEvent(ComponentEvent ce) {
				ModelData selectedItem = menuList.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					showWidget((String) selectedItem.get(MenuItemModelData.PROPERTY_ID));
				}
			}
		};

		menuList.setBorders(false);

		PmsMessages pmsMessages = getPmsMessages();

		// portals tree link
		MenuItemModelData itemUser = new MenuItemModelData(pmsMessages.menuItem2PortalsTree(), getPmsStyles()
			.iconPortalManagement(), ID_PORTALS_TREE);
		menuList.getStore().add(itemUser);

		menuList.addListener(Events.OnClick, listener);

		add(menuList);
	}

	private void showWidget(String type) {
		String clazz = null;
		Object data = null;
		String title = null;
		PmsMessages pmsMessages = getPmsMessages();
		if (type.equals(ID_PORTALS_TREE)) {
			// open tab with portals tree
			clazz = PortalsManagement.class.toString();
			title = pmsMessages.menuItem2PortalsTree();
		}
		getTabItemManager().closeAllAndAddWidget(clazz, data, title);
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setIconStyle(getPmsStyles().iconPortalManager());
		setBodyBorder(false);
		setAutoHeight(true);
		setLayoutOnChange(true);

		setHeading(getPmsMessages().menuItem1PortalManagement());
		getHeader().addStyleName(getGuiCommonStyles().noSideBorders());
	}

	@Override
	public ListView<MenuItemModelData> getMenuListView() {
		return menuList;
	}
}
