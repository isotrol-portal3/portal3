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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.externalservices;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceType;
import com.isotrol.impe3.pms.gui.api.service.IExternalServicesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.ExternalServiceModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.widget.NavigationPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsMainPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;


/**
 * Encloses the graphic interface elements shared by all Services management widgets.
 * 
 * @author Andrei Cojocaru
 * 
 */
public abstract class AServicesManagement extends PmsContentPanel {

	/**
	 * "Name" column width in pixels.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 300;

	/**
	 * "Description" column width in pixels.<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 450;

	/**
	 * External Service grid.<br/>
	 */
	private Grid<ExternalServiceModelData> grid = null;

	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<ExternalServiceModelData> store = null;

	/**
	 * "Manage service" tool item.<br/>
	 */
	private Button ttiManage = null;

	private ExternalServiceDTO selectedServiceDto = null;

	private PmsViewport viewport = null;

	/*
	 * Injected deps
	 */
	/**
	 * The services errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Cell renderer for ID column.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;
	/**
	 * external services service.<br/>
	 */
	private IExternalServicesServiceAsync externalServicesService = null;

	/**
	 * Error message resolver for the external services service.<br/>
	 */
	private IErrorMessageResolver emrServicesService = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * PMS specific settings.<br/>
	 */
	private PmsSettings pmsSettings = null;

	/**
	 * PMS main panel
	 */
	private PmsMainPanel pmsMainPanel = null;

	private NavigationPanel navigationPanel = null;

	public AServicesManagement() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if (getHistoryToken() != null && getHistoryToken().equals(event.getValue()) && viewport != null) {
					navigationPanel.initNavigation();
					addNavigationServiceItems();
					viewport.init();
					pmsMainPanel.activateViewport(viewport);
				}
			}
		});
	}

	/**
	 * Inits the widget. Must be explicitly called once dependencies are injected.
	 */
	public void init() {

		setHeaderVisible(false);
		setLayout(new FitLayout());

		initComponent();

		tryGetServices();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetServices();
			}
		};
		buttonsSupport.addRefreshButton(this, lRefresh);

		addGrid();

		addToolBar();

	}

	/**
	 * Creates, configures and adds the Services grid.<br/>
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig ccId = new ColumnConfig(ExternalServiceModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		ccId.setSortable(false);
		ccId.setRenderer(idCellRenderer);
		configs.add(ccId);

		ColumnConfig ccName = new ColumnConfig();
		ccName.setId(ExternalServiceModelData.PROPERTY_NAME);
		ccName.setWidth(COLUMN_NAME_WIDTH);
		ccName.setHeader(pmsMessages.columnHeaderName());
		configs.add(ccName);

		ColumnConfig ccDescription = new ColumnConfig();
		ccDescription.setId(ExternalServiceModelData.PROPERTY_DESCRIPTION);
		ccDescription.setWidth(COLUMN_DESCRIPTION_WIDTH);
		ccDescription.setHeader(pmsMessages.columnHeaderDescription());
		configs.add(ccDescription);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<ExternalServiceModelData>();

		grid = new Grid<ExternalServiceModelData>(store, cm);
		grid.setAutoExpandColumn(ExternalServiceModelData.PROPERTY_NAME);
		grid.getView().setForceFit(true);

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ExternalServiceModelData>>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			/**
			 * <br/>
			 */
			public void handleEvent(GridEvent<ExternalServiceModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		Listener<SelectionChangedEvent<ExternalServiceModelData>> listener = new Listener<SelectionChangedEvent<ExternalServiceModelData>>() {
			public void handleEvent(SelectionChangedEvent<ExternalServiceModelData> sce) {
				boolean enabled = true;
				if (sce.getSelection().isEmpty()) {
					enabled = false;
				}
				ttiManage.setEnabled(enabled);
			}
		};
		grid.getSelectionModel().addListener(Events.SelectionChange, listener);

		add(grid);
	}

	/**
	 * Retrieves the available services from the server, and populates the grid with the result.<br/>
	 */
	private void tryGetServices() {
		util.mask(pmsMessages.mskServices());

		AsyncCallback<List<ExternalServiceDTO>> callback = new AsyncCallback<List<ExternalServiceDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrServicesService, pmsMessages.msgErrorRetrieveExternalServices());
			}

			public void onSuccess(List<ExternalServiceDTO> arg0) {

				List<ExternalServiceModelData> mdList = new LinkedList<ExternalServiceModelData>();
				for (ExternalServiceDTO dto : arg0) {
					mdList.add(new ExternalServiceModelData(dto));
				}

				store.removeAll();
				store.add(mdList);

				util.unmask();
			}
		};

		externalServicesService.getServices(getManagedServiceType(), callback);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				getSelectedAndDisplayDetails();
			}
		};

		ttiManage = buttonsSupport.addGenericButton(pmsMessages.labelManage(), getManageIconStyle(), toolBar, listener);
		ttiManage.disable();
		ttiManage.addStyleName("service-manage-icon");

		setTopComponent(toolBar);
	}

	/**
	 * Retrieves from service the detailed data of the selected element, and then displays it in an edition panel.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		ExternalServiceModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			selectedServiceDto = selected.getDTO();
			tryDisplayDetails();
		}
	}

	/**
	 * Opens the External Service visualizer entry point in a new window, for the passed DTO.<br/>
	 * @param serviceDto the selected External Service
	 */
	private void tryDisplayDetails() {

		viewport = getExternalServiceViewport(selectedServiceDto);
		if (!viewport.isAttached()) {
			pmsMainPanel.add(viewport);
		}
		// update the navigation
		addNavigationServiceItems();
		if (getHistoryToken() != null) {
			History.newItem(getHistoryToken());
		}
		pmsMainPanel.activateViewport(viewport);
	}

	private void addNavigationServiceItems() {
		navigationPanel.addNewNavigationItem(getServiceManagementName(), false);
		navigationPanel.addNewNavigationItem(selectedServiceDto.getName(), true);
	}

	protected abstract String getServiceManagementName();

	/**
	 * Returns the pms viewport.<br/>
	 * @param serviceDto
	 * @return the pms viewport.<br/>
	 */
	protected abstract PmsViewport getExternalServiceViewport(ExternalServiceDTO serviceDto);

	/**
	 * Returns the type of service managed by this widget.<br/>
	 * @return the type of service managed by this widget.<br/>
	 */
	protected abstract ExternalServiceType getManagedServiceType();

	/**
	 * Return an icon style for "Manage" button.<br/>
	 * @return
	 */
	protected abstract String getManageIconStyle();

	/**
	 * Returns the history token
	 * @return the history token
	 */
	protected abstract String getHistoryToken();

	/**
	 * Injects the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the pmsMessages
	 */
	public PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * Injects the External Services async service.
	 * @param service
	 */
	@Inject
	public void setExternalServicesService(IExternalServicesServiceAsync service) {
		this.externalServicesService = service;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * Injects the buttons helper
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the PMS specific settings.<br/>
	 * @param pmsSettings
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
	}

	/**
	 * Returns the PMS specific settings bundle<br/>
	 * @return the PMS specific settings bundle
	 */
	protected PmsSettings getPmsSettings() {
		return pmsSettings;
	}

	/**
	 * Returns the generic styles bundle.
	 * @return the generic styles bundle.
	 */
	public GuiCommonStyles getStyles() {
		return styles;
	}

	/**
	 * Returns the PMS specific styles bundle.<br/>
	 * @return the PMS specific styles bundle.<br/>
	 */
	protected PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * Injects the PMS specific styles bundle.<br/>
	 * @param pmsStyles the PMS specific styles bundle.<br/>
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the utilities.
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * Injects the Error Message Resolver for the External Services service.
	 * @param emr the emrServicesService to set
	 */
	@Inject
	public void setEmrServicesService(IErrorMessageResolver emr) {
		this.emrServicesService = emr;
	}

	/**
	 * Injects the ID cell renderer.
	 * @param idCellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setIdCellRenderer(InformationCellRenderer idCellRenderer) {
		this.idCellRenderer = idCellRenderer;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service errors processor
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * Injects the pms main panel
	 * @param pmsMainPanel the pmsMainPanel to set
	 */
	@Inject
	public void setPmsMainPanel(PmsMainPanel pmsMainPanel) {
		this.pmsMainPanel = pmsMainPanel;
	}

	/**
	 * @param navigationPanel the navigationPanel to set
	 */
	@Inject
	public void setNavigationPanel(NavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}
}
