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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement;


import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.session.EnvironmentConfigDTO;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.AManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.PmsTabItemManager;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.config.EnvironmentConfigWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.DevicesManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.edition.EditionManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping.SourceMappingManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains.RoutingDomainsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.UsersManagement;


/**
 * Shows the system admin menu
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class SystemManagementMenu extends AManagementMenu {

	/*
	 * HTML IDs for the menu items
	 */
	/**
	 * <b>Users Management</b> menu item ID<br/>
	 */
	private static final String ID_MI_USERS_MANAGEMENT = "mi2_users_management";
	/**
	 * <b>Editions Management</b> menu item ID<br/>
	 */
	private static final String ID_MI_EDITIONS_MANAGEMENT = "mi2_editions_management";
	/**
	 * <b>Modules Registry</b> menu item ID<br/>
	 */
	private static final String ID_MI_MODULES_REGISTRY = "mi2_modules_registry";
	/**
	 * <b>Source Mappings</b> menu item ID<br/>
	 */
	private static final String ID_MI_MAPPINGS = "mi2_mappings";
	/**
	 * <b>Connectors</b> menu item ID<br/>
	 */
	private static final String ID_MI_CONNECTORS = "mi2_connectors";
	/**
	 * <b>Devices</b> menu item ID<br/>
	 */
	private static final String ID_MI_DEVICES = "mi2_devices";
	/**
	 * <b>Routing Domains</b> menu item ID<br/>
	 */
	private static final String ID_MI_ROUTING_DOMAINS = "mi2_routing_domains";
	/**
	 * <b>Configuration</b> menu item ID<br/>
	 */
	private static final String ID_MI_CONFIGURATION = "mi2_configuration";

	/**
	 * the menu list.<br/>
	 */
	private ListView<MenuItemModelData> adminMenuList = null;
	
	/**
	 * Utilities bundle
	 */
	private Util util = null;
	
	/**
	 * Asynchronous Sessions service
	 */
	private ISessionsServiceAsync sessionsServcie = null;
	
	/**
	 * Error Message Resolver service.<br/>
	 */
	private SimpleErrorMessageResolver emrSimple = null;

	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Constructor
	 */
	public SystemManagementMenu() {
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
		setIconStyle(getPmsStyles().menuIconSystemManagement());
		setBodyBorder(false);

		setHeading(getPmsMessages().menuItem1SystemManagement());
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

		PmsMessages pmsMessages = getPmsMessages();
		PmsStyles pmsStyles = getPmsStyles();

		adminMenuList.setBorders(false);

		// editions management
		MenuItemModelData dliEditions = new MenuItemModelData(pmsMessages.menuItem2EditionsManagement(), pmsStyles
			.menuIconEditionsManagement(), ID_MI_EDITIONS_MANAGEMENT);
		adminMenuList.getStore().add(dliEditions);

		// user management
		MenuItemModelData itemUser = new MenuItemModelData(pmsMessages.menuItem2UsersManagement(), getGuiCommonStyles()
			.iUser(), ID_MI_USERS_MANAGEMENT);
		adminMenuList.getStore().add(itemUser);

		// routing domains
		MenuItemModelData iRoutingDomains = new MenuItemModelData(pmsMessages.menuItem2RoutingDomains(), pmsStyles
			.menuIconRoutingDomains(), ID_MI_ROUTING_DOMAINS);
		adminMenuList.getStore().add(iRoutingDomains);

		// modules registry
		MenuItemModelData item = new MenuItemModelData(pmsMessages.menuItem2ModulesRegistry(), pmsStyles
			.menuIconModulesRegistry(), ID_MI_MODULES_REGISTRY);
		adminMenuList.getStore().add(item);

		// mapping
		item = new MenuItemModelData(pmsMessages.menuItem2Mappings(), pmsStyles.menuIconSourceMappings(),
			ID_MI_MAPPINGS);
		adminMenuList.getStore().add(item);

		// connectors
		item = new MenuItemModelData(pmsMessages.menuItem2Connectors(), pmsStyles.menuIconConnectors(),
			ID_MI_CONNECTORS);
		adminMenuList.getStore().add(item);

		// devices
		item = new MenuItemModelData(pmsMessages.menuItem2Devices(), pmsStyles.menuIconDevices(), ID_MI_DEVICES);
		adminMenuList.getStore().add(item);

		// configuration
		item = new MenuItemModelData(pmsMessages.menuItem2Configuration(), getGuiCommonStyles().iEdit(),
			ID_MI_CONFIGURATION);
		adminMenuList.getStore().add(item);

		adminMenuList.addListener(Events.OnClick, new MenuListener());

		add(adminMenuList);
	}

	private class MenuListener implements Listener<ComponentEvent> {

		public void handleEvent(ComponentEvent e) {
			PmsMessages pmsMessages = getPmsMessages();
			PmsTabItemManager tabItemManager = getTabItemManager();

			MenuItemModelData selected = adminMenuList.getSelectionModel().getSelectedItem();

			if (selected == null) {
				return;
			}

			String widgetClass = null;
			String title = null;
			BaseModel data = null;

			String id = selected.get(MenuItemModelData.PROPERTY_ID);
			if (id.equals(ID_MI_USERS_MANAGEMENT)) {
				// abrimos pestaña para gestion de usuarios
				widgetClass = UsersManagement.class.toString();
				title = pmsMessages.menuItem2UsersManagement();
			} else if (id.equals(ID_MI_EDITIONS_MANAGEMENT)) {
				widgetClass = EditionManagement.class.toString();
				title = pmsMessages.menuItem2EditionsManagement();
			} else if (id.equals(ID_MI_MODULES_REGISTRY)) {
				tabItemManager.addModulesRegistryTabItems();
			} else if (id.equals(ID_MI_MAPPINGS)) {
				widgetClass = SourceMappingManagement.class.toString();
				title = pmsMessages.menuItem2Mappings();
			} else if (id.equals(ID_MI_CONNECTORS)) {
				tabItemManager.addConnectorsTabItems();
			} else if (id.equals(ID_MI_ROUTING_DOMAINS)) {
				widgetClass = RoutingDomainsManagement.class.toString();
				title = pmsMessages.menuItem2RoutingDomains();
			} else if (id.equals(ID_MI_DEVICES)) {
				widgetClass = DevicesManagement.class.toString();
				title = pmsMessages.menuItem2Devices();
			} else if (id.equals(ID_MI_CONFIGURATION)) {
				tryGetEnvironmentConfig();
			}
			if (widgetClass != null) {
				tabItemManager.closeAllAndAddWidget(widgetClass, data, title);
			}
		}
	}
	
	private void tryGetEnvironmentConfig() {

		util.mask(getPmsMessages().mskEnvironmentConfig());

		AsyncCallback<EnvironmentConfigDTO> callback = new AsyncCallback<EnvironmentConfigDTO>() {

			public void onSuccess(EnvironmentConfigDTO result) {
				util.unmask();
				EnvironmentConfigWindow widget = PmsFactory.getInstance().getEnvironmentConfigWindow();
				widget.initWidget(result);
				widget.show();
			}

			public void onFailure(Throwable caught) {
				util.unmask();
				errorProcessor.processError(caught, emrSimple, getPmsMessages().msgErrorGetEnvironmentConfig());
			}
		};
		sessionsServcie.getEnvironmentConfig(callback);
	}

	@Override
	public ListView<MenuItemModelData> getMenuListView() {
		return adminMenuList;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @param sessionsServcie the sessionsServcie to set
	 */
	@Inject
	public void setSessionsServcie(ISessionsServiceAsync sessionsServcie) {
		this.sessionsServcie = sessionsServcie;
	}

	/**
	 * @param emrSimple the emrSimple to set
	 */
	@Inject
	public void setEmrSimple(SimpleErrorMessageResolver emrSimple) {
		this.emrSimple = emrSimple;
	}

	/**
	 * @param errorProcessor the errorProcessor to set
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	};

}
