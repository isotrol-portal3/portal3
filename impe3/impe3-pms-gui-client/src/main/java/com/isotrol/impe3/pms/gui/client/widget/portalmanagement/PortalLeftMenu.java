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


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.gui.common.widget.IframePanel;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.portal.PortalCacheDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PageDeviceModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.ioc.IPmsFactory;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.PortalEditionPropertiesPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.cache.PortalCachePanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.collection.CollectionsManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.AOwnComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.InheritedComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.device.PortalDevicesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names.PortalNameEditionPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.CategoryPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentTypePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.DefaultPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ErrorPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.SpecialPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.TemplatePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.parent.ParentPortalManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.AvailableBasesWidget;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.AvailablePropertiesWidget;


/**
 * Portals left panel
 * 
 * @author Manuel Ruiz
 * 
 */
public class PortalLeftMenu extends ALeftPanel {

	/** "Default widget" item ID */
	private static final String ID_START_PAGE = "star-page";

	/** "Names - locales" item ID.<br/> */
	private static final String ID_NAMES = "names-locales";

	/** "General management" item ID.<br/> */
	private static final String ID_GENERAL_MNT = "general-mnt";

	/** "Bases" item ID.<br/> */
	private static final String ID_BASES = "bases";

	/** "Properties" item ID.<br/> */
	private static final String ID_PROPERTIES = "properties";

	/** "Parent portal" item ID.<br/> */
	private static final String ID_PARENT_PORTAL = "parent-portal";

	/** "Collections" item ID.<br/> */
	private static final String ID_COLLECTIONS = "collections";

	/** "Portal devices" item ID.<br/> */
	private static final String ID_PORTAL_DEVICES = "portal-devices";

	/** "Portal devices" item ID.<br/> */
	private static final String ID_PORTAL_CACHE = "portal-cache";

	/** "Display offline" item ID.<br/> */
	private static final String ID_OFFLINE = "offline";

	/** "Display online" item ID.<br/> */
	private static final String ID_ONLINE = "online";

	/** "Own components" item ID.<br/> */
	private static final String ID_OWN_COMP = "own-comp";

	/** "Inherited components" item ID.<br/> */
	private static final String ID_INH_COMP = "inh-comp";

	/** "Default pages" item ID.<br/> */
	private static final String ID_DEFAULT_PAGES = "default-pages";

	/** "Templates" item ID.<br/> */
	private static final String ID_TEMPLATES = "templates";

	/** "Special pages" item ID.<br/> */
	private static final String ID_SPECIAL_PAGES = "special-pages";

	/** "Category pages" item ID.<br/> */
	private static final String ID_CATEGORY_PAGES = "category-pages";

	/** "Content pages" item ID.<br/> */
	private static final String ID_CONTENT_PAGES = "content-pages";

	/** "Content list pages" item ID.<br/> */
	private static final String ID_CONTENT_LIST_PAGES = "content-list-pages";

	/** "Error pages" item ID.<br/> */
	private static final String ID_ERROR_PAGES = "error-pages";

	/** "Watch handbook" item ID.<br/> */
	private static final String ID_WATCH_HELP = "watch-help";

	/** "Open handbook" item ID.<br/> */
	private static final String ID_OPEN_HELP = "open-help";

	/** portal name dto */
	private PortalNameDTO portalName = null;

	private PortalTabItemManager tabItemManager = null;

	private ListView<MenuItemModelData> adminMenulist = null;

	private MenuItemModelData iAdmin = null;

	private MenuItemModelData iDefault = null;

	private MenuItemLevelOnePanel adminPanel = null;

	/**
	 * ComboBox with the devices for the current portal
	 */
	private ComboBox<PageDeviceModelData> cbDevices = null;

	/**
	 * Page device selected in combo
	 */
	private PageDeviceModelData currentDevice = null;

	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * The Portals async service proxy.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * The Pages async service proxy.<br/>
	 */
	private IPagesServiceAsync pagesService = null;

	/**
	 * Portals service error message resolver.<br/>
	 */
	private PortalsServiceErrorMessageResolver errorMessageResolver = null;

	/** Portal default panel */
	private PortalDefaultPanel portalDefaultPanel = null;

	private List<ListView<MenuItemModelData>> menusList = null;

	/**
	 * Error Message Resolver for Portals.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

	@Override
	protected void configMenuPanel(LayoutContainer container) {

		portalName = PmsFactory.getInstance().getPortalViewport().getPortalName();
		menusList = new ArrayList<ListView<MenuItemModelData>>();

		addGeneralAdminItemMenu(container);
		// addDisplayItemMenu(container);
		addComponentsItemMenu(container);
		addPagesItemMenu(container);
		addHelpItemMenu(container);

		// show init widget for portals:
		showInitWidget();
	}

	private void addGeneralAdminItemMenu(LayoutContainer container) {
		adminPanel = new MenuItemLevelOnePanel();
		adminPanel.setHeading(getPmsMessages().menuItem1GeneralManagement());
		adminPanel.setIconStyle(getPmsStyles().iconPortalManagement());
		container.add(adminPanel);

		adminMenulist = new ListView<MenuItemModelData>();
		adminMenulist.setBorders(false);
		adminMenulist.setStore(new ListStore<MenuItemModelData>());
		adminMenulist.setSimpleTemplate(getSettings().tplListView());
		adminMenulist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();

		// item: Initial widget
		iDefault = new MenuItemModelData(getPmsMessages().menuItem2StartPage(), getPmsStyles().iconPortalManagement(),
			ID_START_PAGE);
		lModels.add(iDefault);

		// item: name and locales
		MenuItemModelData iNames = new MenuItemModelData(getPmsMessages().menuItem2Names(),
			getPmsStyles().iconLocale(), ID_NAMES);
		lModels.add(iNames);

		// item: Portal bases
		MenuItemModelData iBases = new MenuItemModelData(getPmsMessages().labelEditBases(), getPmsStyles().iconBases(),
			ID_BASES);
		lModels.add(iBases);

		// item: Portal properties
		MenuItemModelData iProperties = new MenuItemModelData(getPmsMessages().labelEditProperties(), getPmsStyles()
			.iconProperties(), ID_PROPERTIES);
		lModels.add(iProperties);

		// item: Portal management
		iAdmin = new MenuItemModelData(getPmsMessages().menuItem2Configuration(), getStyles().iEdit(), ID_GENERAL_MNT);
		lModels.add(iAdmin);

		// item: Parent Portal
		MenuItemModelData iParent = new MenuItemModelData(getPmsMessages().menuItem2ParentPortal(), getPmsStyles()
			.iconPortalParent(), ID_PARENT_PORTAL);
		lModels.add(iParent);

		// item: Collections
		MenuItemModelData iCollection = new MenuItemModelData(getPmsMessages().menuItem2Collections(), getPmsStyles()
			.iconCollections(), ID_COLLECTIONS);
		lModels.add(iCollection);

		// item: Portal Cache
		MenuItemModelData iCache = new MenuItemModelData(getPmsMessages().menuItem2Cache(), getPmsStyles().iconCache(),
			ID_PORTAL_CACHE);
		lModels.add(iCache);

		// item: Portal Devices
		MenuItemModelData iDevices = new MenuItemModelData(getPmsMessages().menuItem2Devices(), getPmsStyles()
			.menuIconDevices(), ID_PORTAL_DEVICES);
		lModels.add(iDevices);

		adminMenulist.getStore().add(lModels);

		adminMenulist.addListener(Events.OnClick, new Listener<ListViewEvent<MenuItemModelData>>() {
			/**
			 * <br/>
			 * @param be
			 */
			public void handleEvent(ListViewEvent<MenuItemModelData> be) {
				if (!be.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
					String itemId = be.getListView().getSelectionModel().getSelectedItem().get(
						MenuItemModelData.PROPERTY_ID);
					IPmsFactory factory = PmsFactory.getInstance();
					if (itemId.equals(ID_GENERAL_MNT)) {
						tryGetPortalTemplate(portalName.getId());
					} else if (itemId.equals(ID_BASES)) {
						AvailableBasesWidget widget = factory.getAvailableBasesWidget();
						widget.init(portalName);
						tabItemManager.addTabItem(widget, getPmsMessages().headerBasesManagement());
					} else if (itemId.equals(ID_PROPERTIES)) {
						AvailablePropertiesWidget widget = factory.getAvailablePropertiesWidget();
						widget.init(portalName);
						tabItemManager.addTabItem(widget, getPmsMessages().headerPropertiesManagement());
					} else if (itemId.equals(ID_START_PAGE)) {
						showInitWidget();
					} else if (itemId.equals(ID_NAMES)) {
						tryGetPortalNames();
					} else if (itemId.equals(ID_PARENT_PORTAL)) {
						ParentPortalManagement widget = factory.getParentPortalManagement();
						widget.init(portalName.getId());
						tabItemManager.addTabItem(widget, getPmsMessages().menuItem2ParentPortal());
					} else if (itemId.equals(ID_COLLECTIONS)) {
						CollectionsManagement widget = factory.getCollectionsManagement();
						widget.setPortalId(portalName.getId());
						tabItemManager.addTabItem(widget, getPmsMessages().menuItem2Collections());
					} else if (itemId.equals(ID_PORTAL_DEVICES)) {
						PortalDevicesManagement widget = factory.getPortalDevicesManagement();
						widget.setPortalId(portalName.getId());
						tabItemManager.addTabItem(widget, getPmsMessages().menuItem2Devices());
					} else if (itemId.equals(ID_PORTAL_CACHE)) {
						tryGetPortalCacheInfo();
					}
					deselectOthersMenus(adminMenulist);
				}
			}
		});

		adminPanel.add(adminMenulist);
		menusList.add(adminMenulist);
	}

	private void tryGetPortalCacheInfo() {

		getUtil().mask(getPmsMessages().mskPortalCache());

		AsyncCallback<PortalCacheDTO> callback = new AsyncCallback<PortalCacheDTO>() {

			public void onSuccess(PortalCacheDTO result) {
				getUtil().unmask();
				PortalCachePanel widget = PmsFactory.getInstance().getPortalCachePanel();
				widget.initWidget(result);
				widget.show();
			}

			public void onFailure(Throwable caught) {
				getUtil().unmask();
				getUtil().error(getPmsMessages().msgErrorGetPortalCache());
			}
		};
		portalsService.getPortalCache(portalName.getId(), callback);
	}

	/**
	 * Shows the portal's init widget. This is the offline preview (if ready) or the default portal widget
	 */
	protected void showInitWidget() {

		getUtil().mask(getPmsMessages().mskOfflinePreview());

		AsyncCallback<PortalURLsDTO> callback = new AsyncCallback<PortalURLsDTO>() {
			public void onSuccess(PortalURLsDTO result) {
				PortalPreviewPanel portalPreview = PmsFactory.getInstance().getPortalPreviewWidget();
				portalPreview.setPortalName(portalName);
				portalPreview.setPortalURLs(result);
				tabItemManager.addTabItem(portalPreview, portalName.getName().getDisplayName());
				getUtil().unmask();
			}

			public void onFailure(Throwable caught) {
				tabItemManager.addTabItem(portalDefaultPanel, portalName.getName().getDisplayName());
				getUtil().unmask();
			}
		};
		portalsService.getURLs(portalName.getId(), callback);

		adminMenulist.getSelectionModel().select(iDefault, false);
		deselectOthersMenus(adminMenulist);
		adminPanel.expand();
	}

	/**
	 * Calls getNames method from portal service and show names / locales window if success
	 */
	private void tryGetPortalNames() {
		getUtil().mask(getPmsMessages().mskPortalNames());
		AsyncCallback<PortalNameDTO> callback = new AsyncCallback<PortalNameDTO>() {

			public void onSuccess(PortalNameDTO result) {
				getUtil().unmask();
				portalName = result;
				PortalNameEditionPanel portalNamePanel = PmsFactory.getInstance().getPortalNameEditionPanel();
				portalNamePanel.initWidget(result);
				portalNamePanel.show();
			}

			public void onFailure(Throwable caught) {
				getUtil().unmask();
				getUtil().error(getPmsMessages().msgErrorRetrievePortalNames());
			}
		};
		portalsService.getName(portalName.getId(), callback);
	}

	@SuppressWarnings("unused")
	private void addDisplayItemMenu(LayoutContainer container) {
		MenuItemLevelOnePanel panel = new MenuItemLevelOnePanel();
		panel.setHeading(getPmsMessages().menuItem1Display());
		panel.setIconStyle(getStyles().iPreview());
		container.add(panel);

		final ListView<MenuItemModelData> list = new ListView<MenuItemModelData>();
		list.setBorders(false);
		list.setStore(new ListStore<MenuItemModelData>());
		list.setSimpleTemplate(getSettings().tplListView());
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
		// item: display offline
		MenuItemModelData iOffline = new MenuItemModelData(getPmsMessages().menuItem2Offline(), getPmsStyles()
			.viewOffline(), ID_OFFLINE);
		lModels.add(iOffline);

		// item: display online
		MenuItemModelData iOnline = new MenuItemModelData(getPmsMessages().menuItem2Online(), getPmsStyles()
			.viewOnline(), ID_ONLINE);
		lModels.add(iOnline);

		list.getStore().add(lModels);

		list.addListener(Events.OnClick, new Listener<ListViewEvent<MenuItemModelData>>() {
			/**
			 * <br/>
			 * @param be
			 */
			public void handleEvent(ListViewEvent<MenuItemModelData> be) {
				if (!be.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
					String itemId = be.getListView().getSelectionModel().getSelectedItem().get(
						MenuItemModelData.PROPERTY_ID);
					if (itemId.equals(ID_OFFLINE)) {
						tryGetOfflineUrl(portalName.getId(), portalName.getName().getDisplayName());
					} else if (itemId.equals(ID_ONLINE)) {
						tryGetOnlineUrl(portalName.getId(), portalName.getName().getDisplayName());
					}
					deselectOthersMenus(list);
				}
			}
		});

		panel.add(list);
		menusList.add(list);
	}

	private void addComponentsItemMenu(LayoutContainer container) {

		MenuItemLevelOnePanel panel = new MenuItemLevelOnePanel();
		panel.setHeading(getPmsMessages().headerComponentsManagement());
		panel.setIconStyle(getPmsStyles().menuIconComponentsPackages());
		container.add(panel);

		final ListView<MenuItemModelData> list = new ListView<MenuItemModelData>();
		list.setBorders(false);
		list.setStore(new ListStore<MenuItemModelData>());
		list.setSimpleTemplate(getSettings().tplListView());
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
		// item: own components
		MenuItemModelData iOwn = new MenuItemModelData(getPmsMessages().menuItem2OwnComponents(), getPmsStyles()
			.menuIconGenericPackage(), ID_OWN_COMP);
		lModels.add(iOwn);

		// item: inherited components
		MenuItemModelData iInherited = new MenuItemModelData(getPmsMessages().menuItem2InheritedComponentes(),
			getPmsStyles().menuIconGenericPackage(), ID_INH_COMP);
		lModels.add(iInherited);

		list.getStore().add(lModels);

		list.addListener(Events.OnClick, new Listener<ListViewEvent<MenuItemModelData>>() {
			/**
			 * <br/>
			 * @param be
			 */
			public void handleEvent(ListViewEvent<MenuItemModelData> be) {
				if (!be.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
					String itemId = be.getListView().getSelectionModel().getSelectedItem().get(
						MenuItemModelData.PROPERTY_ID);
					IPmsFactory factory = PmsFactory.getInstance();
					if (itemId.equals(ID_OWN_COMP)) {
						AOwnComponentManagement aoc = factory.getAllOwnComponentManagement();
						aoc.setPortalNameDTO(portalName);
						TabItem item = tabItemManager.addTabItem(aoc, getPmsMessages().headerOwnComponentsTab());
						
						AOwnComponentManagement eoc = factory.getErrorOwnComponentManagement();
						eoc.setPortalNameDTO(portalName);
						tabItemManager.addTabItem(eoc, getPmsMessages().headerErrorComponentsTab(), false);
						
						AOwnComponentManagement woc = factory.getWarningOwnComponentManagement();
						woc.setPortalNameDTO(portalName);
						tabItemManager.addTabItem(woc, getPmsMessages().headerWarnComponentsTab(), false);
						
						AOwnComponentManagement voc = factory.getValidOwnComponentManagement();
						voc.setPortalNameDTO(portalName);
						tabItemManager.addTabItem(voc, getPmsMessages().headerValidComponentsTab(), false);
						
						tabItemManager.setSelection(item);
						
					} else if (itemId.equals(ID_INH_COMP)) {
						InheritedComponentManagement widget = factory.getInheritedComponentManagement();
						widget.init(portalName);
						tabItemManager.addTabItem(widget, getPmsMessages().headerInheritedComponentsTab());
					}

					deselectOthersMenus(list);
				}
			}
		});

		panel.add(list);
		menusList.add(list);
	}

	private void addPagesItemMenu(LayoutContainer container) {

		MenuItemLevelOnePanel panel = new MenuItemLevelOnePanel();
		panel.setHeading(getPmsMessages().menuItem1Pages());
		panel.setIconStyle(getPmsStyles().iconPage());
		container.add(panel);

		ToolBar pagesToolBar = new ToolBar();
		pagesToolBar.setAlignment(HorizontalAlignment.CENTER);
		panel.setTopComponent(pagesToolBar);
		// create the combo with the page devices
		createPagesDeviceCombo(pagesToolBar);

		// retrieve pages devices when menu is expanded
		panel.addListener(Events.BeforeExpand, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				tryGetPageDevices();
			}
		});

		final ListView<MenuItemModelData> list = new ListView<MenuItemModelData>();
		list.setBorders(false);
		list.setStore(new ListStore<MenuItemModelData>());
		list.setSimpleTemplate(getSettings().tplListView());
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
		// item: default pages
		MenuItemModelData iDefaultPages = new MenuItemModelData(getPmsMessages().headerDefaultPagesManagement(),
			getPmsStyles().iconGenericPage(), ID_DEFAULT_PAGES);
		lModels.add(iDefaultPages);

		// item: templates
		MenuItemModelData iTemplates = new MenuItemModelData(getPmsMessages().headerTemplatePagesManagement(),
			getPmsStyles().iconGenericPage(), ID_TEMPLATES);
		lModels.add(iTemplates);

		// item: special pages
		MenuItemModelData iSpecialPages = new MenuItemModelData(getPmsMessages().headerSpecialPagesManagement(),
			getPmsStyles().iconGenericPage(), ID_SPECIAL_PAGES);
		lModels.add(iSpecialPages);

		// item: category pages
		MenuItemModelData iCategoryPages = new MenuItemModelData(getPmsMessages().headerCategoryPagesManagement(),
			getPmsStyles().iconGenericPage(), ID_CATEGORY_PAGES);
		lModels.add(iCategoryPages);

		// item: content pages
		MenuItemModelData iContentPages = new MenuItemModelData(getPmsMessages().headerContentPagesManagement(),
			getPmsStyles().iconGenericPage(), ID_CONTENT_PAGES);
		lModels.add(iContentPages);

		// item: content list pages
		MenuItemModelData iContentListPages = new MenuItemModelData(
			getPmsMessages().headerContentTypePagesManagement(), getPmsStyles().iconGenericPage(),
			ID_CONTENT_LIST_PAGES);
		lModels.add(iContentListPages);

		// item: error pages
		MenuItemModelData iErrorPages = new MenuItemModelData(getPmsMessages().headerErrorPagesManagement(),
			getPmsStyles().iconGenericPage(), ID_ERROR_PAGES);
		lModels.add(iErrorPages);

		list.getStore().add(lModels);

		list.addListener(Events.OnClick, new Listener<ListViewEvent<MenuItemModelData>>() {
			/**
			 * <br/>
			 * @param be
			 */
			public void handleEvent(ListViewEvent<MenuItemModelData> be) {
				if (!be.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
					String itemId = be.getListView().getSelectionModel().getSelectedItem().get(
						MenuItemModelData.PROPERTY_ID);
					IPmsFactory factory = PmsFactory.getInstance();
					if (itemId.equals(ID_DEFAULT_PAGES)) {
						DefaultPagesManagement widget = factory.getDefaultPagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerDefaultPagesManagement());

					} else if (itemId.equals(ID_TEMPLATES)) {
						TemplatePagesManagement widget = factory.getTemplatePagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerTemplatePagesManagement());

					} else if (itemId.equals(ID_SPECIAL_PAGES)) {
						SpecialPagesManagement widget = factory.getSpecialPagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerSpecialPagesManagement());

					} else if (itemId.equals(ID_CATEGORY_PAGES)) {
						CategoryPagesManagement widget = factory.getCategoryPagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerCategoryPagesManagement());

					} else if (itemId.equals(ID_CONTENT_PAGES)) {
						ContentPagesManagement widget = factory.getContentPagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerContentPagesManagement());

					} else if (itemId.equals(ID_CONTENT_LIST_PAGES)) {
						ContentTypePagesManagement widget = factory.getContentTypePagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerContentTypePagesManagement());

					} else if (itemId.equals(ID_ERROR_PAGES)) {
						ErrorPagesManagement widget = factory.getErrorPagesManagement();
						widget.init(portalName, currentDevice);
						tabItemManager.addTabItem(widget, getPmsMessages().headerErrorPagesManagement());
					}
					deselectOthersMenus(list);
				}
			}
		});

		panel.add(list);
		menusList.add(list);
	}

	private void addHelpItemMenu(LayoutContainer container) {

		MenuItemLevelOnePanel panel = new MenuItemLevelOnePanel();
		panel.setHeading(getPmsMessages().menuItem1Help());
		panel.setIconStyle(getPmsStyles().menuIconHelp());
		container.add(panel);

		final ListView<MenuItemModelData> list = new ListView<MenuItemModelData>();
		list.setBorders(false);
		list.setStore(new ListStore<MenuItemModelData>());
		list.setSimpleTemplate(getSettings().tplListView());
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
		// item: watch handbook in an iframe
		MenuItemModelData iWatchHelp = new MenuItemModelData(getPmsMessages().menuItem2WatchHandbook(), getPmsStyles()
			.menuIconHelp(), ID_WATCH_HELP);
		lModels.add(iWatchHelp);

		// item: open handbook in a new window
		MenuItemModelData iOpenHelp = new MenuItemModelData(getPmsMessages().menuItem2OpenHandbook(), getPmsStyles()
			.menuIconHelp(), ID_OPEN_HELP);
		lModels.add(iOpenHelp);

		list.getStore().add(lModels);

		list.addListener(Events.OnClick, new Listener<ListViewEvent<MenuItemModelData>>() {
			/**
			 * <br/>
			 * @param be
			 */
			public void handleEvent(ListViewEvent<MenuItemModelData> be) {
				if (!be.getListView().getSelectionModel().getSelectedItems().isEmpty()) {
					String itemId = be.getListView().getSelectionModel().getSelectedItem().get(
						MenuItemModelData.PROPERTY_ID);
					IPmsFactory factory = PmsFactory.getInstance();
					if (itemId.equals(ID_WATCH_HELP)) {
						// open tab panel with the Pms portals handbook in a iframe
						IframePanel iframe = factory.getIframePanel();
						iframe.setIframeUrl(getSettings().pmsPortalsAdminManualUrl());
						tabItemManager.addTabItem(iframe, getPmsMessages().headerPmsHandbook());
					} else if (itemId.equals(ID_OPEN_HELP)) {
						// open Pms handbook in a new window
						Window.open(getSettings().pmsPortalsAdminManualUrl(), getPmsMessages().headerPmsHandbook(),
							PmsConstants.NEW_WINDOW_FEATURES);
					}

					deselectOthersMenus(list);
				}
			}
		});

		panel.add(list);
		menusList.add(list);
	}

	/**
	 * Creates the combo with the page devices
	 * @param pagesToolBar
	 * @return the ComboBox with the pages devices
	 */
	private void createPagesDeviceCombo(ToolBar pagesToolBar) {
		cbDevices = new ComboBox<PageDeviceModelData>();
		cbDevices.setTriggerAction(TriggerAction.ALL);
		cbDevices.setEditable(false);
		IconButton filterBtn = new IconButton(getPmsStyles().menuIconDevices());
		filterBtn.setWidth(20);
		cbDevices.setDisplayField(PageDeviceModelData.PROPERTY_DISPLAY_NAME);
		ListStore<PageDeviceModelData> stPageDevices = new ListStore<PageDeviceModelData>();
		cbDevices.setStore(stPageDevices);

		cbDevices.addSelectionChangedListener(new SelectionChangedListener<PageDeviceModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<PageDeviceModelData> se) {
				PageDeviceModelData device = se.getSelectedItem();
				if (device != null) {
					currentDevice = device;
				}
			}
		});

		pagesToolBar.add(filterBtn);
		pagesToolBar.add(cbDevices);
	}

	/**
	 * Preview the portal in offline mode
	 * @param portalId
	 * @param name
	 */
	private void tryGetOfflineUrl(String portalId, final String name) {

		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, errorMessageResolver, getPmsMessages().msgErrorGetPortalOfflineUrl(
					name));
			}

			public void onSuccess(String arg0) {
				Window.open(arg0, name, PmsConstants.NEW_WINDOW_FEATURES);
			}
		};

		portalsService.getOfflineURL(portalId, callback);
	}

	/**
	 * Preview the portal in online mode
	 * @param portalId
	 * @param name
	 */
	private void tryGetOnlineUrl(String portalId, final String name) {

		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, errorMessageResolver, getPmsMessages().msgErrorGetPortalOnlineUrl(
					name));
			}

			public void onSuccess(String arg0) {
				Window.open(arg0, name, PmsConstants.NEW_WINDOW_FEATURES);
			}
		};

		portalsService.getOnlineURL(portalId, callback);
	}

	private void deselectOthersMenus(ListView<MenuItemModelData> list) {
		for (ListView<MenuItemModelData> l : menusList) {
			if (!l.equals(list)) {
				l.getSelectionModel().deselectAll();
			}
		}
	}

	private void tryGetPortalTemplate(String portalId) {

		// retrieve the portal template:
		AsyncCallback<PortalTemplateDTO> portalCallback = new AsyncCallback<PortalTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(getMessages().headerErrorWindow(),
					emrPortals.getMessage(arg0, getPmsMessages().msgErrorGetPortal()), null).setModal(true);
			}

			public void onSuccess(PortalTemplateDTO arg0) {
				PortalEditionPropertiesPanel widget = PmsFactory.getInstance().getPortalEditionPropertiesPanel();
				widget.init(arg0, portalName.getName().getDisplayName());
				widget.show();
			}
		};
		portalsService.get(portalId, portalCallback);
	}

	/**
	 * Tries to get the pages device for this portal and stores them in a ComboBox
	 */
	private void tryGetPageDevices() {

		final ListStore<PageDeviceModelData> stPageDevices = cbDevices.getStore();
		stPageDevices.removeAll();

		AsyncCallback<List<PageDeviceDTO>> callback = new AsyncCallback<List<PageDeviceDTO>>() {

			public void onSuccess(List<PageDeviceDTO> result) {
				List<PageDeviceModelData> models = new ArrayList<PageDeviceModelData>();
				for (PageDeviceDTO device : result) {
					models.add(new PageDeviceModelData(device));
				}
				stPageDevices.add(models);
				PageDeviceModelData defaultDevice = stPageDevices.findModel(PageDeviceModelData.PROPERTY_DEFAULT, true);
				cbDevices.setValue(defaultDevice);
				currentDevice = defaultDevice;
			}

			public void onFailure(Throwable caught) {
				getUtil().error(getPmsMessages().msgErrorRetrieveDevices());
			}
		};
		pagesService.getPageDevices(portalName.getId(), callback);
	}

	public boolean isInitialized() {
		return false;
	}

	/**
	 * @param tabItemManager the tabItemManager to set
	 */
	@Inject
	public void setTabItemManager(PortalTabItemManager tabItemManager) {
		this.tabItemManager = tabItemManager;
	}

	/**
	 * @param errorProcessor the errorProcessor to set
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param portalsService the portalsService to set
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * @param errorMessageResolver the errorMessageResolver to set
	 */
	@Inject
	public void setErrorMessageResolver(PortalsServiceErrorMessageResolver errorMessageResolver) {
		this.errorMessageResolver = errorMessageResolver;
	}

	/**
	 * @param portalName the portalName to set
	 */
	public void setPortalName(PortalNameDTO portalName) {
		this.portalName = portalName;
	}

	/**
	 * @param portalDefaultPanel the portalDefaultPanel to set
	 */
	@Inject
	public void setPortalDefaultPanel(PortalDefaultPanel portalDefaultPanel) {
		this.portalDefaultPanel = portalDefaultPanel;
	}

	/**
	 * Injects the Error Message Resolver for Portals service.
	 * @param emrPortals the emrPortals to set
	 */
	@Inject
	public void setEmrPortals(PortalsServiceErrorMessageResolver emrPortals) {
		this.emrPortals = emrPortals;
	}

	/**
	 * @param pagesService the pagesService to set
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
	}
}
