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


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.databinding.IDataBound;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalSelModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.ioc.IPmsFactory;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.EventCallback;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.CommentsExternalServiceManagement;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.IndexersManagement;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.NodesRepositoryManagement;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.PortalUsersManagement;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoryManagement;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypeManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalDefaultPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalPreviewPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ValidConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.DevicesManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.edition.EditionManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping.SourceMappingManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ComponentsPackagesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ConnectorsView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.InvalidModulesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.NotFoundModulesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.NotModulesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains.RoutingDomainsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.UsersManagement;


/**
 * Gestiona los TabItems del tabPanel central de la aplicación:
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class PmsTabItemManager extends TabPanel implements ICenterWidget {

	/**
	 * support for {@link IInitializableWidget}.<br/>
	 */
	private boolean initialized = false;

	/**
	 * The PMS injector, used here as the Spring "bean factory"<br/> Yes, we do miss Spring's <i>BeanFactoryAware</i>
	 * and <i>InitializingBean</i> interfaces :(
	 */
	private IPmsFactory injector = null;

	private static final String UNDERSCORE = "_";

	private static final List<String> PORTAL_MANAGEMENT_WIDGETS = Arrays.asList(new String[] {PortalsManagement.class
		.toString()});

	private static final List<String> INFORMATION_ARCHITECTURE_WIDGETS = Arrays.asList(new String[] {
		ContentTypeManagement.class.toString(), CategoryManagement.class.toString()});

	private static final List<String> SYSTEM_MANAGEMENT_WIDGETS = Arrays.asList(new String[] {
		UsersManagement.class.toString(), EditionManagement.class.toString(), ComponentsPackagesView.class.toString(),
		ConnectorsView.class.toString(), InvalidModulesView.class.toString(), NotFoundModulesView.class.toString(),
		NotModulesView.class.toString(), SourceMappingManagement.class.toString(),
		ValidConnectorsManagement.class.toString(), RoutingDomainsManagement.class.toString(),
		DevicesManagement.class.toString()});

	private static final List<String> SERVICES_MANAGEMENT_WIDGETS = Arrays.asList(new String[] {
		PortalUsersManagement.class.toString(), NodesRepositoryManagement.class.toString(),
		CommentsExternalServiceManagement.class.toString(), IndexersManagement.class.toString()});

	private static final List<String> MODULES_REGISTRY_WIDGETS = Arrays.asList(new String[] {
		ComponentsPackagesView.class.toString(), ConnectorsView.class.toString(), InvalidModulesView.class.toString(),
		NotFoundModulesView.class.toString(), NotModulesView.class.toString(),});

	/*
	 * Injected services
	 */
	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;

	/**
	 * The tree selection model<br/>
	 */
	private TreePanelSelectionModel<PortalSelModelData> treeSelectionModel = null;

	/**
	 * Proxy to portals RPC service.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/** Portal default panel */
	private PortalDefaultPanel portalDefaultPanel = null;

	/**
	 * Default constructor. Dependency Injection is done through setters.
	 */
	public PmsTabItemManager() {
		this.injector = PmsFactory.getInstance();

		setTabScroll(true);
		setBorderStyle(false);
		setBodyBorder(false);
		setId(Constants.CENTER_PANEL_ID);
		setAutoSelect(true);
	}

	/**
	 * Acually, does nothing. In this particular case, it may not be called.<br/>
	 * @see com.isotrol.impe3.pms.gui.common.IInitializable#init()
	 */
	public Widget init() {
		initialized = true;

		return this;
	}

	/**
	 * GXT bug: layout is correct only if resizing after first tabitem insert.<br/> FIXME this piece of code should be
	 * checked w/ every new GXT version. Current version: 1.2.4
	 */
	private void maybeResize() {
		if (getItemCount() == 1) {
			setSize(Constants.HUNDRED_PERCENT, Constants.HUNDRED_PERCENT);
		}
	}

	/**
	 * @param widget Widget por añadir
	 * @param title Titulo tab
	 * 
	 */
	private TabItem addTabItem(Widget widget, String title) {
		String tabItemId = null;

		// creacion ID que deberia tener:
		if (widget instanceof IDataBound<?>) {
			IDataBound<?> dataBoundWidget = (IDataBound<?>) widget;
			Object model = dataBoundWidget.getBoundData();
			tabItemId = computeTabItemId(widget.getClass().toString(), model);
		} else {
			// antes: tabItemId = w.getClass().toString();
			tabItemId = title;
		}

		TabItem tabItem = find(tabItemId);

		// inserción solo si no encontrado:
		if (tabItem == null) {
			PmsTabItem newTabItem = new PmsTabItem(title, widget, tabItemId);
			// newTabItem.setClosable(true);
			add(newTabItem);
			tabItem = newTabItem;
		}
		maybeResize();
		return tabItem;
	}

	/**
	 * @param widget Widget por añadir
	 * @param title Titulo tab
	 * 
	 */
	public void addTabItemAndCloseAll(Widget widget, String title) {
		removeAll();
		PmsTabItem newTabItem = new PmsTabItem(title, widget);
		add(newTabItem);
		setSelection(newTabItem);
	}

	/**
	 * Adds the modules registry tab items.<br/>
	 */
	public void addModulesRegistryTabItems() {
		closeAllWithoutConfirm(new EventCallback() {
			public void onEvent() {
				TabItem item = addWidget(ConnectorsView.class.toString(), null, pmsMessages.titleConnectors());
				addWidget(ComponentsPackagesView.class.toString(), null, pmsMessages.titleComponentsPackages());
				addWidget(InvalidModulesView.class.toString(), null, pmsMessages.titleInvalidModules());
				addWidget(NotFoundModulesView.class.toString(), null, pmsMessages.titleNotFoundModules());
				addWidget(NotModulesView.class.toString(), null, pmsMessages.titleIncorrectModules());
				setSelection(item);
			}
		});
	}

	private void closeAllWithoutConfirm(EventCallback callback) {
		closeAll(false, callback);
	}

	/**
	 * Adds the Connectors tab items.<br/>
	 */
	public void addConnectorsTabItems() {
		closeAllWithoutConfirm(new EventCallback() {
			public void onEvent() {
				// AConnectorsManagement cm = injector.getValidConnectorsManagement().init();
				AConnectorsManagement cm = injector.getAllConnectorsManagement().init();
				TabItem item = addTabItem(cm, pmsMessages.titleAllConnectors());

				// erroneous connectors:
				AConnectorsManagement ecm = injector.getErroneousConnectorsManagement().init();
				addTabItem(ecm, pmsMessages.titleErroneousConnectors());

				// warning connectors:
				AConnectorsManagement wcm = injector.getWarningConnectorsManagement().init();
				addTabItem(wcm, pmsMessages.titleWarningConnectors());

				// ok connectors:
				AConnectorsManagement ocm = injector.getValidConnectorsManagement().init();
				addTabItem(ocm, pmsMessages.titleValidConnectors());

				setSelection(item);
			}
		});
	}

	/**
	 * Closes all tab items related to portal with passed ID, if there are such open tabs.<br/>
	 * 
	 * @param portalId portal identifier
	 */
	public void removePortalTabItems(String portalId) {
		List<TabItem> tabItems = getItems();
		List<TabItem> toRemove = new LinkedList<TabItem>();
		for (TabItem tabItem : tabItems) {
			if (tabItem instanceof PmsTabItem) {
				PmsTabItem pmsTabItem = (PmsTabItem) tabItem;
				Widget widget = pmsTabItem.getContent();
				if (isBoundToPortal(widget, portalId)) {
					toRemove.add(tabItem);
				}
			}
		}
		for (TabItem tabItem : toRemove) {
			remove(tabItem);
		}
	}

	/**
	 * Workaround for Checkstyle rule about max nested if-else depth :[<br/> Returns <code>true</code> if the passed
	 * widget is a portal management widget bound to a PortalTemplateDTO whose id is equal to the passed id.
	 * @param widget
	 * @param portalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isBoundToPortal(Widget widget, String portalId) {
		boolean is = false;
		if (PORTAL_MANAGEMENT_WIDGETS.contains(widget.getClass().toString())) {
			// all portal related widgets are IDataBound<PortalTemplateDTO>
			IDataBound<PortalTemplateDTO> dataBoundPanel = (IDataBound<PortalTemplateDTO>) widget;
			PortalTemplateDTO portalTemplate = dataBoundPanel.getBoundData();
			String templateId = portalTemplate.getId();
			if (templateId != null && templateId.equals(portalId)) {
				is = true;
			}
		}
		return is;
	}

	/**
	 * Calcula el tabItemId para una clase dada y un modelo asociado.
	 * 
	 * @param widgetClass
	 * @param model
	 * @return
	 */
	private String computeTabItemId(String widgetClass, Object model) {
		StringBuilder tabItemId = new StringBuilder(widgetClass);
		if (model != null) {
			tabItemId.append(UNDERSCORE).append(model.toString());
		}
		return tabItemId.toString();
	}

	/**
	 * Añade un TabItem dada la clase del Widget contenido y los datos asociados.
	 * 
	 * @param widgetClass el String de la clase, tal que 'instancia'.getClass().toString()
	 * @param model los datos asociados al widget, si existentes
	 * @param title
	 */
	private TabItem addWidget(String widgetClass, Object model, String title) {
		// comprobacion existencia
		String tabItemId = computeTabItemId(widgetClass, model);
		TabItem existing = find(tabItemId);
		if (existing == null) {
			// creacion
			Widget widget = null;

			if (PORTAL_MANAGEMENT_WIDGETS.contains(widgetClass)) {
				widget = createPortalManagementWidget(widgetClass);
			} else if (INFORMATION_ARCHITECTURE_WIDGETS.contains(widgetClass)) {
				widget = createInformationArchitectureWidget(widgetClass);
			} else if (SYSTEM_MANAGEMENT_WIDGETS.contains(widgetClass)) {
				widget = createSystemManagementWidget(widgetClass);
			} else if (SERVICES_MANAGEMENT_WIDGETS.contains(widgetClass)) {
				widget = createServicesManagementWidget(widgetClass);
			}
			// insercion
			TabItem tabItem = new PmsTabItem(title, widget, tabItemId);
			add(tabItem);
			existing = tabItem;
		}

		if (!PORTAL_MANAGEMENT_WIDGETS.contains(widgetClass)) {
			treeSelectionModel.deselectAll();
		}

		maybeResize();
		return existing;
	}

	/**
	 * <br/>
	 * @param widgetClass
	 * @param model
	 * @param title
	 */
	public void closeAllAndAddWidget(final String widgetClass, final Object model, final String title) {
		closeAllWithoutConfirm(new EventCallback() {
			public void onEvent() {
				TabItem item = addWidget(widgetClass, model, title);
				setSelection(item);
			}
		});
	}

	/**
	 * <br/>
	 * @param widgetClass
	 * @return
	 */
	private Widget createSystemManagementWidget(String widgetClass) {
		Widget widget = null;
		if (widgetClass.equals(UsersManagement.class.toString())) {
			widget = injector.getUserManagement().init();
		} else if (widgetClass.equals(EditionManagement.class.toString())) {
			EditionManagement editionManagement = injector.getEditionManagement();
			editionManagement.init();
			widget = editionManagement;
		} else if (widgetClass.equals(SourceMappingManagement.class.toString())) {
			SourceMappingManagement sourceMappingManagement = injector.getSourceMappingManagement();
			sourceMappingManagement.init();
			widget = sourceMappingManagement;
		} else if (widgetClass.equals(ValidConnectorsManagement.class.toString())) {
			ValidConnectorsManagement validConnectorsManagement = injector.getValidConnectorsManagement();
			validConnectorsManagement.lazyInit();
			widget = validConnectorsManagement;
		} else if (widgetClass.equals(RoutingDomainsManagement.class.toString())) {
			RoutingDomainsManagement routingDomainsManagement = injector.getRoutingDomainsManagement();
			routingDomainsManagement.init();
			widget = routingDomainsManagement;
		} else if (MODULES_REGISTRY_WIDGETS.contains(widgetClass)) {
			widget = createModulesRegistryWidget(widgetClass);
		} else if (widgetClass.equals(DevicesManagement.class.toString())) {
			DevicesManagement devicesManagement = injector.getDeviceManagement();
			devicesManagement.init();
			widget = devicesManagement;
		}
		return widget;
	}

	/**
	 * <br/>
	 * @param widgetClass
	 */
	private Widget createModulesRegistryWidget(String widgetClass) {
		Widget widget = null;
		if (widgetClass.equals(ConnectorsView.class.toString())) {
			ConnectorsView connectorsView = injector.getConnectorsView();
			widget = connectorsView;
		} else if (widgetClass.equals(ComponentsPackagesView.class.toString())) {
			ComponentsPackagesView componentsManagement = injector.getComponentsPackagesManagement();
			widget = componentsManagement;
		} else if (widgetClass.equals(InvalidModulesView.class.toString())) {
			InvalidModulesView invalidManagement = injector.getInvalidModulesView();
			widget = invalidManagement;
		} else if (widgetClass.equals(NotFoundModulesView.class.toString())) {
			NotFoundModulesView notFoundView = injector.getNotFoundModulesView();
			widget = notFoundView;
		} else if (widgetClass.equals(NotModulesView.class.toString())) {
			NotModulesView incorrectView = injector.getIncorrectModulesView();
			widget = incorrectView;
		}

		return widget;
	}

	/**
	 * <br/>
	 * @param widgetClass
	 * @return
	 */
	private Widget createInformationArchitectureWidget(String widgetClass) {
		Widget widget = null;
		if (widgetClass.equals(ContentTypeManagement.class.toString())) {
			ContentTypeManagement contentTypeManagement = injector.getContentTypeManagement();
			contentTypeManagement.init();
			widget = contentTypeManagement;
		} else if (widgetClass.equals(CategoryManagement.class.toString())) {
			CategoryManagement categoryManagement = injector.getCategoryManagement();
			categoryManagement.init();
			widget = categoryManagement;
		}
		return widget;
	}

	private Widget createServicesManagementWidget(String widgetClass) {
		Widget widget = null;
		if (widgetClass.equals(PortalUsersManagement.class.toString())) {
			PortalUsersManagement usersManagement = injector.getPortalUsersManagement();
			usersManagement.init();
			widget = usersManagement;
		} else if (widgetClass.equals(NodesRepositoryManagement.class.toString())) {
			NodesRepositoryManagement nrManagement = injector.getNodesRepositoryManagement();
			nrManagement.init();
			widget = nrManagement;
			
		} else if (widgetClass.equals(IndexersManagement.class.toString())) {
			IndexersManagement nrManagement = injector.getIndexersManagement();
			nrManagement.init();
			widget = nrManagement;
		} else if (widgetClass.equals(CommentsExternalServiceManagement.class.toString())) {
			CommentsExternalServiceManagement commentsManagement = injector.getCommentsExternalServiceManagement();
			commentsManagement.init();
			widget = commentsManagement;
		}

		return widget;
	}

	private Widget createPortalManagementWidget(String widgetClass) {
		Widget widget = null;
		if (widgetClass.equals(PortalsManagement.class.toString())) {
			PortalsManagement portalsManagement = injector.getPortalsManagement();
			widget = portalsManagement;
		}
		return widget;
	}

	/**
	 * Closes all the tab items.<br/>
	 * @param showConfirm if true, will show a confirm dialog before closing.
	 * @param callback code to fire once the tabs are closed. May be <code>null</code>.
	 */
	private void closeAll(boolean showConfirm, final EventCallback callback) {
		if (showConfirm && getItemCount() > 0) {
			MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmCloseTabs(),
				new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button b = we.getButtonClicked();
						if (b.getItemId().equals(Dialog.YES)) {
							removeAll();
							doCallback(callback);
						}
					}
				});
		} else {
			removeAll();
			doCallback(callback);
		}
	}

	/**
	 * Calls the callback method, if non <code>null</code>.<br/>
	 * @param callback
	 */
	private void doCallback(EventCallback callback) {
		if (callback != null) {
			callback.onEvent();
		}
	}

	/**
	 * Busca un TabItem dado por su tabItemId
	 * 
	 * @param tabItemId
	 * @return <code>null</code>, si no encontrado; el PmsTabItem correspondiente, si existe
	 */
	private PmsTabItem find(String tabItemId) {
		Iterator<TabItem> it = getItems().iterator();

		PmsTabItem tabItem = null;
		boolean found = false;

		while (it.hasNext() && !found) {
			tabItem = (PmsTabItem) it.next();
			found = tabItem.getTabItemId().equals(tabItemId);
		}

		if (found) {
			return tabItem;
		}
		return null;
	}

	/**
	 * Closes all tab items, and adds the portal default item
	 * 
	 * @param ptDto
	 */
	public void tryAddPortalTabItems(final PortalNameDTO portalName) {

		closeAllWithoutConfirm(null);

		util.mask(pmsMessages.mskOfflinePreview());

		AsyncCallback<PortalURLsDTO> callback = new AsyncCallback<PortalURLsDTO>() {
			public void onSuccess(PortalURLsDTO result) {
				PortalPreviewPanel portalPreview = PmsFactory.getInstance().getPortalPreviewWidget();
				portalPreview.setPortalName(portalName);
				portalPreview.setPortalURLs(result);
				setSelection(addTabItem(portalPreview, portalName.getName().getDisplayName()));
				util.unmask();
			}

			public void onFailure(Throwable caught) {
				addTabItem(portalDefaultPanel, portalName.getName().getDisplayName());
				util.unmask();
			}
		};
		portalsService.getURLs(portalName.getId(), callback);
	}

	/**
	 * sets the tree selection model
	 * @param selectionModel the treeSelectionModel to set
	 */
	@Inject
	public void setTreeSelectionModel(TreePanelSelectionModel<PortalSelModelData> selectionModel) {
		this.treeSelectionModel = selectionModel;
	}

	/**
	 * sets the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * sets the PMS message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Sets the portals async service.
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * Injects the utilities object.
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param portalDefaultPanel the portalDefaultPanel to set
	 */
	@Inject
	public void setPortalDefaultPanel(PortalDefaultPanel portalDefaultPanel) {
		this.portalDefaultPanel = portalDefaultPanel;
	}

}
