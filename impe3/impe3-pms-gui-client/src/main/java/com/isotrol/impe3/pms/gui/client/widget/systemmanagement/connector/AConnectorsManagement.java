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
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.GearCellRenderer;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.ILazyComponent;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.ConnectorsController;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ConnectorsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.renderer.ModuleNameCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.StateCellRenderer;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Base class for Connectors management.<br/>
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public abstract class AConnectorsManagement extends PmsContentPanel implements IInitializableWidget, ILazyComponent {

	/**
	 * Support for {@link IInitializableWidget#isInitialized()}.<br/>
	 */
	private boolean initialized = false;

	/**
	 * <b>Module</b> property descriptor.<br/>
	 */
	private static final String PROPERTY_MODULE = Constants.PROPERTY_MODULE;

	/**
	 * Width in px for <b>name</b> column.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 150;
	/**
	 * Width in px for <b>state</b> column.<br/>
	 */
	private static final int COLUMN_STATE_WIDTH = Constants.COLUMN_ICON_WIDTH;
	/**
	 * Width in px for <b>key</b> column.<br/>
	 */
	private static final int COLUMN_KEY_WIDTH = Constants.COLUMN_ICON_WIDTH;
	/**
	 * Width in px for <b>module</b> column.<br/>
	 */
	private static final int COLUMN_MODULE_WIDTH = 200;

	/*
	 * Tool items buttons
	 */
	/**
	 * "Edit" menu item<br/>
	 */
	private Button editToolItem = null;
	/**
	 * "Delete" menu item<br/>
	 */
	private Button deleteToolItem = null;

	/**
	 * Modules store for {@link #grid}<br/>
	 */
	private ListStore<ModuleInstanceSelModelData> store = null;
	/**
	 * Modules grid.<br/>
	 */
	private Grid<ModuleInstanceSelModelData> grid = null;

	/**
	 * filter for {@link #grid}.<br/> When configured, bound to {@link #store}.
	 */
	private CustomizableStoreFilter<ModuleInstanceSelModelData> filter = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Renderer for "module name" grid cell.<br/>
	 */
	private ModuleNameCellRenderer moduleNameCellRenderer = null;

	/**
	 * Renderer for "state" grid cell.<br/>
	 */
	private StateCellRenderer stateCellRenderer = null;

	/**
	 * The gear cell renderer.<br/>
	 */
	private GearCellRenderer gearCellRenderer = null;

	/**
	 * Cell renderer for ID column.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;

	/** connector service proxy */
	private IConnectorsServiceAsync connectorsService = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Error message resolver for the operation: {@link ConnectorsService#delete(String)}.<br/>
	 */
	private ConnectorsErrorMessageResolver emrConnectors = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Gui Common Styles bundle.<br/>
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * The {@link #modulesStore grids store} sorter.<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * Settings bundle
	 */
	private Settings settings = null;
	
	/**
	 * Pms styles bundle
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Default constructor. Configures the widget as a lazy initialized component.
	 */
	public AConnectorsManagement() {
		Util.configLazyComponent(this);
	}

	/**
	 * Contains the <i>pre-render</i> code initialization.<br/>
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init()
	 */
	public AConnectorsManagement init() {
		initialized = true;
		addToolBar();
		addBottomToolBar();
		return this;
	}

	/**
	 * Inits the widget. Must be called after the dependencies are set (after instantiation).
	 */
	public void lazyInit() {
		initComponent();
		initController();

		tryGetConnectorInstances();
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the connectors controller. All change events fired by the
	 * controller will refresh .<br/>
	 */
	private void initController() {
		ConnectorsController connController = (ConnectorsController) this.connectorsService;

		connController.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				if (event instanceof PmsChangeEvent) {
					PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
					switch (pmsEvent.getType()) {
						case PmsChangeEvent.DELETE:
							String id = pmsEvent.getEventInfo();
							ModuleInstanceSelModelData model = store.findModel(ModuleInstanceSelModelData.PROPERTY_ID,
								id);
							if (model != null) {
								store.remove(model);
							}
							break;
						case PmsChangeEvent.ADD:
						case PmsChangeEvent.UPDATE:
						case PmsChangeEvent.IMPORT:
							tryGetConnectorInstances();
							break;
						default: // shouldn't happen..
							// Logger.getInstance().log(
							// "Unexpected event descriptor for a ChangeEventSource instance :" + event.type);
					}
				}
			}
		});
	}

	/**
	 * Inits this container properties and inner components.<br/>
	 */
	private void initComponent() {

		setLayout(new FitLayout());

		addGrid();
		// filter binding after store creation:

		filter.bind(store);

		initSpecific();
	}

	/**
	 * Contains the implementation-specific initialization code.<br/> Default implementation does nothing.
	 */
	protected void initSpecific() {
	}

	/**
	 * Creates and adds a grid to the container<br/>
	 */
	private void addGrid() {
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		// ID
		ColumnConfig column = new ColumnConfig(ModuleInstanceSelModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		column.setRenderer(idCellRenderer);
		configs.add(column);

		// state icon
		column = new ColumnConfig();
		column.setRenderer(stateCellRenderer);
		column.setId(ModuleInstanceSelModelData.PROPERTY_STATE);
		column.setHeaderText(pmsMessages.columnHeaderState());
		column.setWidth(COLUMN_STATE_WIDTH);
		configs.add(column);

		// key
		column = new ColumnConfig();
		column.setId(ModuleInstanceSelModelData.PROPERTY_KEY);
		column.setHeaderText("");
		column.setWidth(COLUMN_KEY_WIDTH);
		column.setRenderer(gearCellRenderer);
		column.setSortable(false);
		configs.add(column);

		// name
		column = new ColumnConfig();
		column.setId(ModuleInstanceSelModelData.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PROPERTY_MODULE);
		column.setWidth(COLUMN_MODULE_WIDTH);
		column.setHeaderText(pmsMessages.columnHeaderModule());
		column.setRenderer(moduleNameCellRenderer);
		column.setSortable(false);
		configs.add(column);

		addSpecificColumns(configs);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<ModuleInstanceSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(ModuleInstanceSelModelData.PROPERTY_NAME);

		grid = new Grid<ModuleInstanceSelModelData>((ListStore<ModuleInstanceSelModelData>) store, cm);

		grid.setSelectionModel(new GridSelectionModel<ModuleInstanceSelModelData>());
		grid.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<ModuleInstanceSelModelData>() {

				@Override
				public void selectionChanged(SelectionChangedEvent<ModuleInstanceSelModelData> se) {

					if (se.getSelection() != null && !se.getSelection().isEmpty()) {
						enableButtons();
					} else {
						disableButtons();
					}
				}

			});

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ModuleInstanceSelModelData>>() {
			public void handleEvent(GridEvent<ModuleInstanceSelModelData> be) {
				getSelectedAndDisplayDetails();
			};
		});

		grid.setAutoExpandColumn(ModuleInstanceSelModelData.PROPERTY_NAME);
		grid.setLoadMask(true);
		grid.getView().setForceFit(true);

		add(grid);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		deleteToolItem = buttonsSupport.addDeleteButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				final ModuleInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) { // confirmed
								tryDelete(selected);
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteConnector(),
						listener).setModal(true);
				}
			}
		}, null);

		editToolItem = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails();
			}
		});

		// disable edit and delete buttons (enable when a grid's row is selected)
		disableButtons();

		toolBar.add(deleteToolItem);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(editToolItem);

		toolBar.add(new FillToolItem());

		filter = new CustomizableStoreFilter<ModuleInstanceSelModelData>(Arrays.asList(new String[] {
			ModuleInstanceSelModelData.PROPERTY_NAME, ModuleInstanceSelModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		toolBar.add(filter);

		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetConnectorInstances();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

		addSpecificToolItems(toolBar);

		setTopComponent(toolBar);
	}
	
	private void addBottomToolBar() {
		// Bottom toolbar
		ToolBar bottomToolBar = new ToolBar();

		// Export button
		Button ttiExport = getButtonsSupport().createGenericButton(getPmsMessages().labelExport(),
			pmsStyles.exportIcon(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ConnectorsExportWindow connWindow = PmsFactory.getInstance().getConnectorsExportWindow();
					connWindow.show();
				}
			});
		bottomToolBar.add(ttiExport);
		bottomToolBar.add(new SeparatorToolItem());

		// Import button
		Button ttiImport = getButtonsSupport().createGenericButton(getPmsMessages().labelImport(),
			pmsStyles.importIcon(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ConnectorsImportWindow connWindow = PmsFactory.getInstance().getConnectorsImportWindow();
					connWindow.show();
				}
			});
		bottomToolBar.add(ttiImport);
		
		bottomToolBar.add(new FillToolItem());

		// "Help" button:
		getButtonsSupport().addHelpButton(bottomToolBar, getSettings().pmsConnectorsAdminPortalManualUrl());

		setBottomComponent(bottomToolBar);
	}

	/**
	 * Retrieves the selected Connector data from remote service, and displays it on a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		ModuleInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			tryRetrieveAndDisplayDetails(selected);
		}
	}

	/**
	 * disables grid buttons
	 */
	private void disableButtons() {
		deleteToolItem.disable();
		editToolItem.disable();
	}

	/**
	 * enables grid buttons
	 */
	private void enableButtons() {
		deleteToolItem.enable();
		editToolItem.enable();
	}

	/**
	 * Calls connector service to show the connector instances in the grid
	 */
	private void tryGetConnectorInstances() {
		util.mask(pmsMessages.mskConnectors());

		AsyncCallback<List<ModuleInstanceSelDTO>> callback = new AsyncCallback<List<ModuleInstanceSelDTO>>() {
			public void onSuccess(List<ModuleInstanceSelDTO> instances) {
				List<ModuleInstanceSelModelData> connectors = dtoToModel(instances);
				store.removeAll();
//				store.clearFilters();
//				filter.setValue(null);
				store.add(connectors);

				util.unmask();
			}

			private List<ModuleInstanceSelModelData> dtoToModel(List<ModuleInstanceSelDTO> instances) {
				List<ModuleInstanceSelModelData> maps = new LinkedList<ModuleInstanceSelModelData>();
				for (ModuleInstanceSelDTO m : instances) {
					ModuleInstanceSelModelData connector = new ModuleInstanceSelModelData(m);
					maps.add(connector);
				}

				return maps;
			}

			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrConnectors, pmsMessages.msgErrorRetrieveConnectors());
			}
		};

		callGetConnectors(callback);
	}

	/**
	 * Retrieves the passed Connector details, and shows them on a details panel.<br/>
	 * @param selected
	 */
	protected final void tryRetrieveAndDisplayDetails(final ModuleInstanceSelModelData selected) {
		util.mask(pmsMessages.mskComponent());

		AsyncCallback<ModuleInstanceTemplateDTO> callback = new AsyncCallback<ModuleInstanceTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrConnectors, pmsMessages.msgErrorRetrieveConnector());
			}

			public void onSuccess(ModuleInstanceTemplateDTO template) {
				showConnectorDetail(template);
				util.unmask();
			}
		};

		connectorsService.get(selected.getDTO().getId(), callback);
	}

	/**
	 * Deletes the passed Connector.<br/>
	 * @param selected
	 */
	private void tryDelete(final ModuleInstanceSelModelData selected) {
		util.mask(pmsMessages.mskDeleteConnector());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrConnectors, pmsMessages.msgErrorDeleteConnector());
			}

			public void onSuccess(Void arg0) {
				util.unmask();
				util.info(pmsMessages.msgSuccessDeleteConnector());
			}
		};

		connectorsService.delete(selected.getDTO().getId(), callback);
	}

	/**
	 * Creates a new details panel, binds the passed DTO to it and displays it.<br/>
	 * @param template
	 */
	protected final void showConnectorDetail(ModuleInstanceTemplateDTO template) {
		ConnectorDetailPanel connectorDetailPanel = PmsFactory.getInstance().getConnectorDetailPanel();
		connectorDetailPanel.init(template);
		connectorDetailPanel.show();
	}

	/**
	 * <br/>
	 * @param callback
	 */
	protected abstract void callGetConnectors(AsyncCallback<List<ModuleInstanceSelDTO>> callback);

	/**
	 * Adds specific column configs. Must be overridden in the concrete subclass.<br/> Default implementation does
	 * nothing.
	 * 
	 * @param configs list of column configs currently added.
	 */
	protected void addSpecificColumns(List<ColumnConfig> configs) {
	}

	/**
	 * Adds specific tool items to the tool bar. Must be overriden in the concrete subclass.<br/> Default implementation
	 * does nothing.
	 * 
	 * @param toolbar
	 */
	protected void addSpecificToolItems(ToolBar toolbar) {
	}

	/**
	 * Returns the connectors service.<br/>
	 * @return the connectors service.
	 */
	protected final IConnectorsServiceAsync getService() {
		return connectorsService;
	}

	/**
	 * @return the buttonsSupport
	 */
	protected final Buttons getButtonsSupport() {
		return buttonsSupport;
	}

	/**
	 * @return the messages
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the pmsMessages
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the emrConnectors
	 */
	protected final ConnectorsErrorMessageResolver getErrorMessageResolver() {
		return emrConnectors;
	}

	/**
	 * @return the util
	 */
	protected final Util getUtil() {
		return util;
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

	/*
	 * Dependencies injectors
	 */
	/**
	 * Injects the connectors service.
	 * @param connectorsService
	 */
	@Inject
	public void setConnectorsService(IConnectorsServiceAsync connectorsService) {
		this.connectorsService = connectorsService;
	}

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
	 * Injects the buttons support
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the shared objects bundle.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the state cell renderer.<br/>
	 * @param stateCellRenderer
	 */
	@Inject
	public void setStateCellRenderer(StateCellRenderer stateCellRenderer) {
		this.stateCellRenderer = stateCellRenderer;
	}

	/**
	 * Injects the grids store sorter.<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * @return the storeSorter
	 */
	public AlphabeticalStoreSorter getStoreSorter() {
		return storeSorter;
	}

	/**
	 * Injects the gear cell rederer.
	 * @param gearCellRenderer the gearCellRenderer to set
	 */
	@Inject
	public void setGearCellRenderer(GearCellRenderer gearCellRenderer) {
		this.gearCellRenderer = gearCellRenderer;
	}

	/**
	 * Injects the module name cell renderer.
	 * @param moduleNameCellRenderer the moduleNameCellRenderer to set
	 */
	@Inject
	public void setModuleNameCellRenderer(ModuleNameCellRenderer moduleNameCellRenderer) {
		this.moduleNameCellRenderer = moduleNameCellRenderer;
	}

	/**
	 * Injects the Error Message resolver.<br/>
	 * @param emrConnectors
	 */
	@Inject
	protected final void setEmrConnectors(ConnectorsErrorMessageResolver emrConnectors) {
		this.emrConnectors = emrConnectors;
	}

	/**
	 * @param idCellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setIdCellRenderer(InformationCellRenderer idCellRenderer) {
		this.idCellRenderer = idCellRenderer;
	}

	/**
	 * @return the idCellRenderer
	 */
	public InformationCellRenderer getIdCellRenderer() {
		return idCellRenderer;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error processor.
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @return the guiCommonStyles
	 */
	public GuiCommonStyles getGuiCommonStyles() {
		return guiCommonStyles;
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
}
