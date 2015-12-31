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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.device;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.api.DeviceNameUse;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.BooleanCellRenderer;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeTreeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.DeviceInPortalModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.PortalDeviceTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;


/**
 * Gui for portals devices management
 * @author Manuel Ruiz
 * 
 */
public class PortalDevicesManagement extends PmsContentPanel {

	/**
	 * Width for <b>name</b> column<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 200;

	/**
	 * Store for {@link #grid} and {@link #filter}.<br/>
	 */
	private PortalDeviceTreeStore store = null;

	/**
	 * Master view Content Types grid.<br/>
	 */
	private TreeGrid<DeviceInPortalModelData> treeGrid = null;

	/**
	 * use parent configuration button
	 */
	private Button ttiInheritConfiguration = null;

	/** current portal id */
	private String portalId = null;

	private PortalDevicesTemplateDTO currentPortalDevicesTemplate = null;

	protected static final Map<DeviceNameUse, String> useNameMap = new HashMap<DeviceNameUse, String>();
	static {
		useNameMap.put(DeviceNameUse.NONE, "No usado");
		useNameMap.put(DeviceNameUse.FIRST_SEGMENT, "Primer segmento");
		useNameMap.put(DeviceNameUse.LAST_SEGMENT, "Último segmento");
		useNameMap.put(DeviceNameUse.EXTENSION, "Extensión");
	}

	/* ********
	 * Injected deps:*******
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Portals async service.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * Error Message Resolver for Portals.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

	/**
	 * PMS specific message bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common message bundle.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * PMS specific styles service.
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Common specific styles service.
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;

	/**
	 * Renderer for boolean properties.<br/>
	 */
	private BooleanCellRenderer booleanCellRenderer = null;

	@Override
	protected void beforeRender() {
		initComponent();

		// asynchronous access:
		tryGetPortalDevices();
		configController();
	}

	private void initComponent() {
		setLayoutOnChange(true);
		setLayout(new FitLayout());

		addTreeGrid();
		addTopToolBar();
	}

	private void configController() {
		final PortalsController portalsController = (PortalsController) portalsService;

		final ChangeListener changeListener = new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
				switch (pmsEvent.getType()) {
					case PmsChangeEvent.UPDATE_PORTAL_DEVICES: // portal deleted
						tryGetPortalDevices();
						break;
					default:
						// nothing to do here.
				}
			}
		};
		portalsController.addChangeListener(changeListener);

		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				portalsController.removeChangeListener(changeListener);
			}
		});
	}

	/**
	 * Retrieves the current portal devices and populates the tree grid.<br/>
	 */
	private void tryGetPortalDevices() {
		util.mask(pmsMessages.mskDevices());

		AsyncCallback<PortalDevicesTemplateDTO> callback = new AsyncCallback<PortalDevicesTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrPortals, pmsMessages.msgErrorRetrieveDevices());
			}

			public void onSuccess(PortalDevicesTemplateDTO arg0) {
				populateTreeGrid(arg0);
				util.unmask();
			}
		};

		portalsService.getPortalDevices(portalId, callback);
	}

	/**
	 * @param arg0 the devices template for current portal
	 */
	private void populateTreeGrid(PortalDevicesTemplateDTO arg0) {

		currentPortalDevicesTemplate = arg0;
		enableDisableButtons(!isInherited() && currentPortalDevicesTemplate.isChild());

		store.removeAll();

		for (DeviceInPortalTreeDTO child : arg0.getDevices()) {
			store.add(child, true);
		}
	}

	/**
	 * Creates, configures and adds the grid.<br/>
	 */
	private void addTreeGrid() {
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_ACTIVE);
		config.setWidth(50);
		config.setHeaderText(pmsMessages.columnHeaderActive());
		config.setRenderer(booleanCellRenderer);
		config.setAlignment(HorizontalAlignment.CENTER);
		configs.add(config);

		config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_DEFAULT);
		config.setWidth(50);
		config.setHeaderText(pmsMessages.columnHeaderDefault());
		config.setRenderer(booleanCellRenderer);
		config.setAlignment(HorizontalAlignment.CENTER);
		configs.add(config);

		config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_NAME);
		config.setWidth(COLUMN_NAME_WIDTH);
		config.setRenderer(new HtmlEncodeTreeGridCellRenderer());
		config.setHeaderText(pmsMessages.columnHeaderDevice());
		configs.add(config);

		config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_PATH);
		config.setWidth(200);
		config.setHeaderText(pmsMessages.columnHeaderName());
		config.setRenderer(new HtmlEncodeGridCellRenderer());
		configs.add(config);

		config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_USE_NAME);
		config.setWidth(200);
		config.setHeaderText(pmsMessages.columnHeaderNameUse());
		config.setRenderer(new GridCellRenderer<DeviceInPortalModelData>() {

			public Object render(DeviceInPortalModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<DeviceInPortalModelData> store, Grid<DeviceInPortalModelData> grid) {
				if (model.getDTO().getUse() != null) {
					return useNameMap.get(model.getDTO().getUse());
				} else {
					return null;
				}
			}
		});
		configs.add(config);

		ColumnModel cm = new ColumnModel(configs);

		store = new PortalDeviceTreeStore();

		treeGrid = new TreeGrid<DeviceInPortalModelData>((PortalDeviceTreeStore) store, cm);
		treeGrid.setSelectionModel(new GridSelectionModel<DeviceInPortalModelData>());
		treeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		treeGrid.setAutoExpandColumn(DeviceInPortalModelData.PROPERTY_PATH);
		treeGrid.getStyle().setLeafIcon(IconHelper.createStyle(pmsStyles.iconTreeFolder()));
		treeGrid.setLoadMask(true);
		treeGrid.getView().setForceFit(true);

		add(treeGrid);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addTopToolBar() {
		ToolBar toolBar = new ToolBar();

		SelectionListener<ButtonEvent> lEdit = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				if (currentPortalDevicesTemplate.isChild() && !isInherited()) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {

							// TODO if save, setInherited(true)???;
							showDeviceEditionPanel(true);
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmOverrideParentDevices(),
						lConfirm).setModal(true);
				} else {
					showDeviceEditionPanel(false);
				}
			}
		};
		buttonsSupport.addEditButton(toolBar, lEdit, null);
		buttonsSupport.addSeparator(toolBar);

		ttiInheritConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelInheritConfiguration(),
			guiCommonStyles.iDelete(), toolBar, new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					final DeviceInPortalModelData selected = treeGrid.getSelectionModel().getSelectedItem();
					if (selected != null) {
						Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent be) {
								Button clicked = be.getButtonClicked();
								if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
									setInherited(true);
									PortalDevicesDTO devices = currentPortalDevicesTemplate.toPortalDevicesDTO();
									AsyncCallback<Void> callback = new AsyncCallback<Void>() {
										public void onSuccess(Void result) {
											util.info(pmsMessages.msgSuccessSetPortalDevices());
										}

										public void onFailure(Throwable caught) {
											util.error(pmsMessages.msgErrorSetPortalDevices());
										}
									};
									portalsService.setPortalDevices(devices, callback);
								}
							}
						};
						MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmUseParentDevices(),
							lConfirm).setModal(true);
					}
				}
			});
		toolBar.add(new FillToolItem());

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetPortalDevices();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

		setTopComponent(toolBar);
	}

	private void enableDisableButtons(boolean enabled) {
		ttiInheritConfiguration.setEnabled(enabled);
	}

	/**
	 * Open a device edition panel
	 * @param inherited
	 */
	private void showDeviceEditionPanel(boolean inherited) {
		PortalDeviceEditionPanel deviceEdition = PmsFactory.getInstance().getPortalDeviceEditionPanel();
		deviceEdition.init(portalId, inherited);
		deviceEdition.show();
	}

	private boolean isInherited() {
		return currentPortalDevicesTemplate != null ? currentPortalDevicesTemplate.isInherited() : false;
	}

	private void setInherited(boolean inherited) {
		currentPortalDevicesTemplate.setInherited(inherited);
	}

	/**
	 * Injects the portals async service proxy.
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
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
	 * Injects the PMS specific message bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the utilities object
	 * @param u the util to set
	 */
	@Inject
	public void setUtil(Util u) {
		this.util = u;
	}

	/**
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the PMS specific style bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param portalId the portalId to set
	 */
	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	/**
	 * @param booleanCellRenderer the booleanCellRenderer to set
	 */
	@Inject
	public void setBooleanCellRenderer(BooleanCellRenderer booleanCellRenderer) {
		this.booleanCellRenderer = booleanCellRenderer;
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}
}
