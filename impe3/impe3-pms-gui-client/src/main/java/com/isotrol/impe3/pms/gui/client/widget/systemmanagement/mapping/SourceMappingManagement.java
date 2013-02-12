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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
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
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.SourceMappingsController;
import com.isotrol.impe3.pms.gui.client.data.impl.SourceMappingSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Master view for Source Mappings.
 * 
 * @author Manuel Ruiz
 * 
 */
public class SourceMappingManagement extends PmsContentPanel {

	/**
	 * Width in pixels for column: <b>Name</b><br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 100;
	/**
	 * Width in pixels for column: <b>Width</b><br/>
	 */
	private static final int COLUMN_DESCRPTION_WIDTH = 300;

	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<SourceMappingSelModelData> store = null;
	/**
	 * Source mappings grid.<br/>
	 */
	private Grid<SourceMappingSelModelData> grid = null;

	/**
	 * "Edit" menu item.<br/>
	 */
	private Button tiEdit = null;
	/**
	 * "Delete mapping" menu item.<br/>
	 */
	private Button tiDelete = null;

	/**
	 * Filter for {@link #grid}.<br/>
	 */
	private CustomizableStoreFilter<SourceMappingSelModelData> filter = null;

	/*
	 * Injected deps
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Cell Renderer for ID cell.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * Source mappings controller.<br/>
	 */
	private ISourceMappingsServiceAsync sourceMappingsService = null;

	/**
	 * Error Message Resolver for Source Mappings service.<br/>
	 */
	private IErrorMessageResolver emrSourceMappings = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS specific css styles.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * the grid store sorter.<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;
	
	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Constructor. Creates the sources mapping tab panel, inits the asynchronous service and adds the listeners.
	 */
	public SourceMappingManagement() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependencies are injected.
	 */
	public void init() {
		initThis();

		addGrid();
		addToolBars();

		initController();

		tryGetSourceMappings();
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the source mappings controller. All change events fired by
	 * the controller will refresh .<br/>
	 */
	private void initController() {
		ChangeListener controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				switch (event.getType()) {
					case ChangeEventSource.Add:
					case ChangeEventSource.Update:
						tryGetSourceMappings();
						break;
					case PmsChangeEvent.IMPORT:
						tryGetSourceMappings();
						store.clearFilters();
						filter.setValue(null);
						break;
					case ChangeEventSource.Remove: // remove from view:
						if (event instanceof PmsChangeEvent) {
							PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
							String id = pmsEvent.getEventInfo();
							SourceMappingSelModelData model = store
								.findModel(SourceMappingSelModelData.PROPERTY_ID, id);
							if (model != null) {
								store.remove(model);
							}
						}
						break;
					default: // shouldn't happen..
						// Logger.getInstance().log(
						// "Unexpected event descriptor for a ChangeEventSource instance :" + event.getType());
				}
			}
		};
		SourceMappingsController serviceController = (SourceMappingsController) sourceMappingsService;
		serviceController.addChangeListener(controllerListener);
	}

	/**
	 * Calls <b>get</b> remote procedure on the {@link #sourceMappingsService source mappings service}.
	 */
	private void tryGetSourceMappings() {
		util.mask(pmsMessages.mskMappings());

		AsyncCallback<List<SourceMappingSelDTO>> callback = new AsyncCallback<List<SourceMappingSelDTO>>() {
			public void onSuccess(List<SourceMappingSelDTO> mappings) {
				List<SourceMappingSelModelData> maps = dtoToModel(mappings);
				store.removeAll();
				store.add(maps);

				util.unmask();
			}

			private List<SourceMappingSelModelData> dtoToModel(List<SourceMappingSelDTO> mappings) {
				List<SourceMappingSelModelData> maps = new LinkedList<SourceMappingSelModelData>();
				for (SourceMappingSelDTO m : mappings) {
					SourceMappingSelModelData mapping = new SourceMappingSelModelData(m);
					maps.add(mapping);
				}

				return maps;
			}

			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrSourceMappings, pmsMessages.msgErrorRetrieveMappings());
			}
		};

		sourceMappingsService.getSourceMappings(callback);
	}

	/**
	 * Calls <b>delete</b> remote procedure on the {@link #sourceMappingsService service}.
	 * @param smsDto
	 */
	private void tryDeleteMapping(SourceMappingSelDTO smsDto) {
		util.mask(pmsMessages.mskDeleteMapping());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrSourceMappings, pmsMessages.msgErrorDeleteMapping());
			}

			public void onSuccess(Void arg0) {
				util.unmask();
				util.info(pmsMessages.msgSuccessDeleteMapping());
			}
		};
		sourceMappingsService.delete(smsDto.getId(), callback);
	}

	/**
	 * Inits this component properties and inner components.
	 */
	private void initThis() {
		setLayoutOnChange(true);
		setLayout(new FitLayout());
	}

	/**
	 * Creates, configs and adds the {@link #grid}.
	 */
	private void addGrid() {
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig(SourceMappingSelModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		column.setSortable(false);
		column.setRenderer(idCellRenderer);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(SourceMappingSelModelData.PROPERTY_NAME);
		column.setHeader(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(SourceMappingSelModelData.PROPERTY_DESCRIPTION);
		column.setHeader(pmsMessages.columnHeaderDescription());
		column.setWidth(COLUMN_DESCRPTION_WIDTH);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<SourceMappingSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(SourceMappingSelModelData.PROPERTY_NAME);

		grid = new Grid<SourceMappingSelModelData>((ListStore<SourceMappingSelModelData>) store, cm);

		grid.setSelectionModel(new GridSelectionModel<SourceMappingSelModelData>());
		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<SourceMappingSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SourceMappingSelModelData> se) {
				boolean enabled;
				// enable Edit & Delete:
				if (grid.getSelectionModel().getSelectedItem() != null) {
					enabled = true;
				} else {
					enabled = false;
				}
				tiEdit.setEnabled(enabled);
				tiDelete.setEnabled(enabled);
			}
		});

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<SourceMappingSelModelData>>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			/**
			 * <br/>
			 */
			public void handleEvent(GridEvent<SourceMappingSelModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		grid.setAutoExpandColumn(SourceMappingSelModelData.PROPERTY_DESCRIPTION);
		grid.setLoadMask(true);
		grid.getView().setForceFit(true);

		add(grid);
	}

	/**
	 * Creates, configures and adds the tool bar.
	 */
	private void addToolBars() {
		ToolBar toolBar = new ToolBar();
		ToolBar bottomToolBar = new ToolBar();

		SelectionListener<ButtonEvent> newElementListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				showMappingDetail(new SourceMappingTemplateDTO());
			}
		};
		buttonsSupport.addAddButton(toolBar, newElementListener, null);
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> deleteElementListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final SourceMappingSelModelData selModelData = grid.getSelectionModel().getSelectedItem();
				if (selModelData != null) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent we) {
							Button clicked = we.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryDeleteMapping(selModelData.getDTO());
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteMapping(), lConfirm)
						.setModal(true);
				}
			}
		};
		tiDelete = buttonsSupport.addDeleteButton(toolBar, deleteElementListener, null);
		tiDelete.disable();
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> editionListener = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails();
			}

		};
		tiEdit = buttonsSupport.addEditButton(toolBar, editionListener);
		tiEdit.disable();

		toolBar.add(new FillToolItem());

		// Filter:
		filter = new CustomizableStoreFilter<SourceMappingSelModelData>(Arrays.asList(new String[] {
			SourceMappingSelModelData.PROPERTY_NAME, SourceMappingSelModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(store);
		toolBar.add(filter);

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetSourceMappings();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);
		
		// export mappings button
		SelectionListener<ButtonEvent> exportListener = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				MappingExportWindow exportWindow = PmsFactory.getInstance().getMappingExportWindow();
				exportWindow.show();
			}

		};
		buttonsSupport.addGenericButton(pmsMessages.labelExport(), pmsStyles.exportIcon(), bottomToolBar, exportListener);
		
		buttonsSupport.addSeparator(bottomToolBar);

		// import mappings button
		SelectionListener<ButtonEvent> importListener = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				MappingImportWindow importWindow = PmsFactory.getInstance().getMappingImportWindow();
				importWindow.show();
			}

		};
		buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), bottomToolBar, importListener);
		bottomToolBar.add(new FillToolItem());
		
		// help button
		buttonsSupport.addHelpButton(bottomToolBar, settings.pmsMappingsAdminPortalManualUrl());
		
		setTopComponent(toolBar);
		setBottomComponent(bottomToolBar);

	}

	/**
	 * Retrieves selected Source Mapping data from service, and displays it in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		SourceMappingSelModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			tryRetrieveAndDisplayDetails(selected);
		}
	}

	/**
	 * Destroys current details window, and creates & shows a new one with the passed data.<br/>
	 * @param mappingModel
	 */
	private void showMappingDetail(SourceMappingTemplateDTO mappingModel) {
		MappingDetailPanel mappingDetailPanel = PmsFactory.getInstance().getMappingDetailPanel();
		mappingDetailPanel.setSourceMappingTemplateDto(mappingModel);
		mappingDetailPanel.show();
	}

	/**
	 * retrieves the passed mapping detail data via RPC and displays it on the creation panel.<br/>
	 * 
	 * @param ctSel
	 */
	private void tryRetrieveAndDisplayDetails(final SourceMappingSelModelData smSel) {
		util.mask(pmsMessages.mskMapping());

		AsyncCallback<SourceMappingTemplateDTO> callback = new AsyncCallback<SourceMappingTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrSourceMappings, pmsMessages.msgErrorRetrieveMapping());
			}

			public void onSuccess(SourceMappingTemplateDTO ct) {
				showMappingDetail(ct);
				util.unmask();
			}
		};

		sourceMappingsService.get(smSel.getDTO().getId(), callback);
	}

	/**
	 * Injects the Source Mappings async service proxy.
	 * @param sourceMappingsService
	 */
	@Inject
	public void setSourceMappingsService(ISourceMappingsServiceAsync sourceMappingsService) {
		this.sourceMappingsService = sourceMappingsService;
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
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the shared objects container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the store sorter<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * Injects the Error Message Resolver for Source Mappings.
	 * @param emrSourceMappings the emrSourceMappings to set
	 */
	@Inject
	public void setEmrSourceMappings(IErrorMessageResolver emrSourceMappings) {
		this.emrSourceMappings = emrSourceMappings;
	}

	/**
	 * Injects the cell renderer for ID column.
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
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
	
	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
