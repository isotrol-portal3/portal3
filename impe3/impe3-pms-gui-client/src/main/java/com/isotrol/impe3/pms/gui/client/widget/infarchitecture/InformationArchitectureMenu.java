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

package com.isotrol.impe3.pms.gui.client.widget.infarchitecture;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.widget.AManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoryManagement;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypeManagement;


/**
 * Shows the information architecture menu
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class InformationArchitectureMenu extends AManagementMenu {

	/*
	 * HTML IDs for menu items.
	 */
	/**
	 * "Content Types" menu item ID.<br/>
	 */
	private static final String ID_MI_CONTENT_TYPES = "mi2_content_types";
	/**
	 * "Categories" menu item ID.<br/>
	 */
	private static final String ID_MI_CATEGORIES = "mi2_categories";
	
	private ListView<MenuItemModelData> lvAdminMenu = null;

	/**
	 * Constructor
	 */
	public InformationArchitectureMenu() {}
	
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
		setIconStyle(getPmsStyles().menuIconInformationArchitecture());
		setBodyBorder(false);
		
		setHeading(getPmsMessages().menuItem1InformationArchitecture());
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
					String widgetClass = null;
					String title = null;
					BaseModel data = null;
					PmsMessages pmsMessages = getPmsMessages();
					String id = selectedItem.get(MenuItemModelData.PROPERTY_ID);
					if (id.equals(ID_MI_CONTENT_TYPES)) {
						// open content types tab panel
						widgetClass = ContentTypeManagement.class.toString();
						title = pmsMessages.menuItem2ContentTypes();
	
					} else if (id.equals(ID_MI_CATEGORIES)) {
						// open tab for categories
						widgetClass = CategoryManagement.class.toString();
						title = pmsMessages.menuItem2Categories();
					}
					getTabItemManager().closeAllAndAddWidget(widgetClass, data, title);
				}
			}
		};

		PmsMessages pmsMessages = getPmsMessages();
		PmsStyles pmsStyles = getPmsStyles();
		
		// content types
		MenuItemModelData item = new MenuItemModelData(
				pmsMessages.menuItem2ContentTypes(),
				pmsStyles.menuIconContentTypes(),
				ID_MI_CONTENT_TYPES);
		lvAdminMenu.getStore().add(item);

		// categories
		item = new MenuItemModelData(
				pmsMessages.menuItem2Categories(),
				pmsStyles.menuIconCategories(),
				ID_MI_CATEGORIES);
		lvAdminMenu.getStore().add(item);

		lvAdminMenu.addListener(Events.OnClick, listener);
		
		add(lvAdminMenu);
	}

	@Override
	public ListView<MenuItemModelData> getMenuListView() {
		return lvAdminMenu;
	}

}
