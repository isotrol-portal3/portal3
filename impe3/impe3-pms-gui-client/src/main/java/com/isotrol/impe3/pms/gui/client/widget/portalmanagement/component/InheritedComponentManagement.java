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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
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
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencySetTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.ComponentsController;
import com.isotrol.impe3.pms.gui.client.data.impl.InheritedComponentInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ComponentsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;


/**
 * Widget that manages the portal's inherited components
 * @author Manuel Ruiz
 */
public class InheritedComponentManagement extends PmsContentPanel {

	/**
	 * Width in pixels for column <b>Module</b><br/>
	 */
	private static final int COLUMN_MODULE_WIDTH = 200;

	/**
	 * Width in pixels for column <b>Name</b><br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 100;

	/**
	 * Width in pixels for column <b>Configuration</b><br/>
	 */
	private static final int COLUMN_CONFIGURATION_WIDTH = 50;

	/**
	 * Width in pixels for column <b>Dependencies</b><br/>
	 */
	private static final int COLUMN_DEPENDENCIES_WIDTH = 50;

	/**
	 * Template for grids icon cells.<br/> The pattern <b>${ICON}</b> represents the url of the icon, and must be
	 * relative to <b>img</b> folder.
	 */
	private static final String TEMPLATE_ICON_CELL = "<div style='text-align: center;'><img src='img/${ICON}' title='${TITLE}' /></div>";
	/**
	 * Pattern "icon" to replace in the template.<br/>
	 */
	private static final String PATTERN_ICON = "\\$\\{ICON\\}";
	/**
	 * Pattern "title" to be replaced in the template.<br/>
	 */
	private static final String PATTERN_TITLE = "\\$\\{TITLE\\}";

	private enum DisplayDetailMode {
		CONFIGURATION, DEPENDENCES
	};

	/**
	 * Portal data bound to the widget instance.<br/>
	 */
	private PortalNameDTO portalNameDto = null;

	/** Components store */
	private ListStore<InheritedComponentInstanceSelModelData> store = null;

	/** Components grid */
	private Grid<InheritedComponentInstanceSelModelData> grid = null;

	/**
	 * Filter for grid<br/>
	 */
	private CustomizableStoreFilter<InheritedComponentInstanceSelModelData> filter = null;

	private Button ttiOverrideConfiguration = null;
	private Button ttiInheritConfiguration = null;
	private Button ttiOverrideDependencies = null;
	private Button ttiInheritDependencies = null;

	/*
	 * Injected deps
	 */
	/**
	 * Components service proxy.<br/>
	 */
	private IComponentsServiceAsync componentsService = null;

	/**
	 * Generic service messages.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * <b>ID</b> cell renderer in the {@link #gComponents grid}.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;

	/**
	 * The gear cell renderer.<br/>
	 */
	private GearCellRenderer gearCellRenderer = null;

	/**
	 * PMS specific messages.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * The grid store sorter<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * Buttons helper object<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Error messages resolver for ComponentsService.<br/>
	 */
	private ComponentsErrorMessageResolver errorMessageResolver = null;

	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Pms styles bundle
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Builds the widget for the passed portal
	 * @param model the portal name dto
	 */
	public void init(PortalNameDTO model) {
		this.portalNameDto = model;

		initThis();
		initComponent();
		addListeners();
		tryGetComponents();
	}

	/**
	 * Creates the components that will be added to this widget
	 */
	private void initComponent() {
		addGrid();
		addToolBars();
	}

	/**
	 * Creates and adds the list where adding the components
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig(InheritedComponentInstanceSelModelData.PROPERTY_ID, messages
			.columnHeaderId(), 15);
		column.setSortable(false);
		column.setRenderer(idCellRenderer);
		configs.add(column);

		column = new ColumnConfig(Constants.PROPERTY_KEY, Constants.EMPTY_STRING, 15);
		column.setRenderer(gearCellRenderer);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(Constants.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(InheritedComponentInstanceSelModelData.PROPERTY_MODULE);
		column.setHeaderText(pmsMessages.columnHeaderModule());
		column.setWidth(COLUMN_MODULE_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(InheritedComponentInstanceSelModelData.PROPERTY_CONFIGURATION);
		column.setHeaderText(pmsMessages.columnHeaderConfiguration());
		column.setWidth(COLUMN_CONFIGURATION_WIDTH);
		column.setRenderer(new GridCellRenderer<InheritedComponentInstanceSelModelData>() {

			public Object render(InheritedComponentInstanceSelModelData model, String property, ColumnData config,
				int rowIndex, int colIndex, ListStore<InheritedComponentInstanceSelModelData> store,
				Grid<InheritedComponentInstanceSelModelData> grid) {

				String icon = null;
				String title = null;
				Boolean configuration = model.getDTO().getConfiguration();
				if (configuration == null) {
					return "";
				} else if (configuration) {
					icon = Constants.OK_IMAGE;
					title = pmsMessages.titleComponentOverrideConfiguration();
				} else {
					icon = Constants.ERROR_IMAGE;
					title = pmsMessages.titleComponentInheritedConfiguration();
				}
				return TEMPLATE_ICON_CELL.replaceAll(PATTERN_ICON, icon).replaceAll(PATTERN_TITLE, title);
			}
		});
		configs.add(column);

		column = new ColumnConfig();
		column.setId(InheritedComponentInstanceSelModelData.PROPERTY_DEPENDENCIES);
		column.setHeaderText(pmsMessages.columnHeaderDependences());
		column.setWidth(COLUMN_DEPENDENCIES_WIDTH);
		column.setRenderer(new GridCellRenderer<InheritedComponentInstanceSelModelData>() {

			public Object render(InheritedComponentInstanceSelModelData model, String property, ColumnData config,
				int rowIndex, int colIndex, ListStore<InheritedComponentInstanceSelModelData> store,
				Grid<InheritedComponentInstanceSelModelData> grid) {

				String icon = null;
				String title = null;
				Boolean dependencies = model.getDTO().getDependencies();
				if (dependencies == null) {
					return "";
				} else if (dependencies) {
					icon = Constants.OK_IMAGE;
					title = pmsMessages.titleComponentOverrideDependences();
				} else {
					icon = Constants.ERROR_IMAGE;
					title = pmsMessages.titleComponentInheritedDependences();
				}
				return TEMPLATE_ICON_CELL.replaceAll(PATTERN_ICON, icon).replaceAll(PATTERN_TITLE, title);
			}
		});
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<InheritedComponentInstanceSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(ModuleInstanceSelModelData.PROPERTY_NAME);

		grid = new Grid<InheritedComponentInstanceSelModelData>(
			(ListStore<InheritedComponentInstanceSelModelData>) store, cm);
		grid.setSelectionModel(new GridSelectionModel<InheritedComponentInstanceSelModelData>());
		grid.setAutoExpandColumn(InheritedComponentInstanceSelModelData.PROPERTY_MODULE);
		grid.setLoadMask(true);

		GridView gridView = grid.getView();
		gridView.setForceFit(true);

		add(grid);
	}

	/**
	 * Configures this
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);
	}

	private void addToolBars() {
		ToolBar toolBar = new ToolBar();
		ToolBar bottomToolBar = new ToolBar();

		// override configuration button
		ttiOverrideConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelOverrideConfiguration(),
			pmsMessages.ttOverrideConfiguration(), guiCommonStyles.iEdit(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					getSelectedAndDisplayConfiguration(DisplayDetailMode.CONFIGURATION);
				}
			});
		ttiOverrideConfiguration.disable();
		toolBar.add(new SeparatorToolItem());

		// inherit configuration button
		ttiInheritConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelInheritConfiguration(), pmsMessages
			.ttInheritConfiguration(), guiCommonStyles.iDelete(), toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final InheritedComponentInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							Button clicked = be.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryClearConfiguration(selected.getDTO());
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmClearConfiguration(),
						lConfirm).setModal(true);
				}
			}
		});
		ttiInheritConfiguration.disable();
		toolBar.add(new SeparatorToolItem());

		// override dependencies button
		ttiOverrideDependencies = buttonsSupport.addGenericButton(pmsMessages.labelOverrideDependences(), pmsMessages
			.ttOverrideDependences(), guiCommonStyles.iEdit(), toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayConfiguration(DisplayDetailMode.DEPENDENCES);
			}
		});
		ttiOverrideDependencies.disable();
		toolBar.add(new SeparatorToolItem());

		// inherit dependences button
		ttiInheritDependencies = buttonsSupport.addGenericButton(pmsMessages.labelInheritDependences(), pmsMessages
			.ttInheritDependences(), guiCommonStyles.iDelete(), toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final InheritedComponentInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							Button clicked = be.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryClearDependences(selected.getDTO());
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmClearDependences(),
						lConfirm).setModal(true);
				}
			}
		});
		ttiInheritDependencies.disable();

		toolBar.add(new FillToolItem());

		filter = new CustomizableStoreFilter<InheritedComponentInstanceSelModelData>(Arrays.asList(new String[] {
			ModuleInstanceSelModelData.PROPERTY_NAME, ModuleInstanceSelModelData.PROPERTY_DESCRIPTION,
			ModuleInstanceSelModelData.PROPERTY_MODULE}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(store);
		toolBar.add(filter);

		buttonsSupport.addRefreshButton(toolBar, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetComponents();
			}
		});

		// Export button
		Button ttiExport = buttonsSupport.createGenericButton(pmsMessages.labelExport(), pmsStyles.exportIcon(),
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					InheritedComponentsExportWindow comWindow = PmsFactory.getInstance()
						.getInheritedComponentsExportWindow();
					comWindow.setPortalId(portalNameDto.getId());
					comWindow.show();
				}
			});
		bottomToolBar.add(ttiExport);

		bottomToolBar.add(new SeparatorToolItem());

		// Import button
		Button ttiImport = buttonsSupport.createGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(),
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ComponentsImportWindow comWindow = PmsFactory.getInstance().getComponentsImportWindow();
					comWindow.setPortalId(portalNameDto.getId());
					comWindow.setInherited(true);
					comWindow.show();
				}
			});
		bottomToolBar.add(ttiImport);

		setTopComponent(toolBar);
		setBottomComponent(bottomToolBar);
	}

	/**
	 * Retrieves the components instances registered for the bound portal.<br/>
	 */
	private void tryGetComponents() {
		util.mask(pmsMessages.mskComponents());

		AsyncCallback<List<InheritedComponentInstanceSelDTO>> callback = new AsyncCallback<List<InheritedComponentInstanceSelDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponents());
			}

			public void onSuccess(List<InheritedComponentInstanceSelDTO> arg0) {
				store.removeAll();
				store.clearFilters();
				filter.setValue(null);

				List<InheritedComponentInstanceSelModelData> inheritedModelData = new LinkedList<InheritedComponentInstanceSelModelData>();
				for (InheritedComponentInstanceSelDTO dto : arg0) {
					inheritedModelData.add(new InheritedComponentInstanceSelModelData(dto));
				}
				store.add(inheritedModelData);

				util.unmask();
			}
		};
		componentsService.getInheritedComponents(portalNameDto.getId(), callback);
	}

	/**
	 * Adds listeners to components.<br/>
	 */
	private void addListeners() {

		final ComponentsController serviceController = (ComponentsController) componentsService;
		/*
		 * service controller listener:
		 */
		final ChangeListener changeListener = new ChangeListener() {
			public void modelChanged(ChangeEvent ce) {
				PmsChangeEvent event = (PmsChangeEvent) ce;
				switch (event.getType()) {
					case PmsChangeEvent.UPDATE:
						onComponentUpdate((InheritedComponentInstanceSelDTO) event.getEventInfo());
						break;
					case PmsChangeEvent.IMPORT:
						tryGetComponents();
						break;
					default: // event not expected: nothing to do here.
						break;
				}
			}
		};
		serviceController.addChangeListener(changeListener);
		
		// remove listener from components controller when this panel is detached
		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				serviceController.removeChangeListener(changeListener);
			}
		});

		/*
		 * Grid RowClick listener
		 */
		grid.addListener(Events.RowClick, new Listener<GridEvent<InheritedComponentInstanceSelModelData>>() {
			public void handleEvent(GridEvent<InheritedComponentInstanceSelModelData> ge) {
				InheritedComponentInstanceSelModelData model = grid.getSelectionModel().getSelectedItem();
				if (model != null) {
					enableDisableButtons(model);
				}
			}
		});
	}

	/**
	 * Retrieves the selected Component instance from service, and then displays it in a details window.<br/>
	 */
	private void getSelectedAndDisplayConfiguration(DisplayDetailMode mode) {
		InheritedComponentInstanceSelModelData modelData = grid.getSelectionModel().getSelectedItem();
		if (modelData != null) {
			InheritedComponentInstanceSelDTO dto = modelData.getDTO();
			if (DisplayDetailMode.DEPENDENCES.equals(mode)) {
				tryGetDependences(dto.getComponent().getId());
			} else if (DisplayDetailMode.CONFIGURATION.equals(mode)) {
				tryGetConfiguration(dto.getComponent().getId());
			}
		}
	}

	private void tryGetDependences(final String id) {
		mask(pmsMessages.mskComponent());

		AsyncCallback<DependencySetTemplateDTO> callback = new AsyncCallback<DependencySetTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponent());
			}

			public void onSuccess(DependencySetTemplateDTO arg0) {
				showDependencies(arg0, id);
				unmask();
			}
		};
		componentsService.getDependencies(portalNameDto.getId(), id, callback);
	}

	private void tryGetConfiguration(final String id) {
		mask(pmsMessages.mskComponent());

		AsyncCallback<ConfigurationTemplateDTO> callback = new AsyncCallback<ConfigurationTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponent());
			}

			public void onSuccess(ConfigurationTemplateDTO arg0) {
				showConfiguration(arg0, id);
				unmask();
			}
		};
		componentsService.getConfiguration(portalNameDto.getId(), id, callback);
	}

	/**
	 * Shows the component dependences details window.<br/>
	 * @param dependencySetTemplate
	 * @param componentId
	 */
	private void showDependencies(DependencySetTemplateDTO dependencySetTemplate, String componentId) {
		OverrideDependencesWindow dependencesDetailPanel = PmsFactory.getInstance().getOverrideDependencesWindow();
		dependencesDetailPanel.init(dependencySetTemplate, portalNameDto.getId(), componentId);
		dependencesDetailPanel.show();
	}

	/**
	 * Shows the component configuration details window.<br/>
	 * @param configurationTemplate
	 * @param componentId
	 */
	private void showConfiguration(ConfigurationTemplateDTO configurationTemplate, String componentId) {
		OverrideConfigurationWindow configurationDetailPanel = PmsFactory.getInstance()
			.getOverrideConfigurationWindow();
		configurationDetailPanel.init(configurationTemplate, portalNameDto.getId(), componentId);
		configurationDetailPanel.show();
	}

	/**
	 * Removes from the own Components grid store the ModelData whose <b>ID</b> property is equal to the passed DTO id.
	 * Adds to the own Components grid store a new ModelData that wraps the passed DTO values.<br/> Called after a
	 * Component was updated.
	 * 
	 * @param dto
	 */
	private void onComponentUpdate(InheritedComponentInstanceSelDTO dto) {
		InheritedComponentInstanceSelModelData oldModel = store.findModel(ModuleInstanceSelModelData.PROPERTY_ID, dto
			.getComponent().getId());
		InheritedComponentInstanceSelModelData newModel = new InheritedComponentInstanceSelModelData(dto);
		store.remove(oldModel);
		store.add(newModel);
	}

	private void enableDisableButtons(InheritedComponentInstanceSelModelData model) {
		Boolean configuration = model.getDTO().getConfiguration();
		Boolean dependencies = model.getDTO().getDependencies();

		if (configuration == null) {
			ttiOverrideConfiguration.disable();
			ttiInheritConfiguration.disable();
		} else {
			ttiOverrideConfiguration.setEnabled(true);
			ttiInheritConfiguration.setEnabled(configuration);
		}

		if (dependencies == null) {
			ttiOverrideDependencies.disable();
			ttiInheritDependencies.disable();
		} else {
			ttiOverrideDependencies.setEnabled(true);
			ttiInheritDependencies.setEnabled(dependencies);
		}
	}

	/**
	 * Calls service to retrieve the inherited configuration
	 * @param dto Component what to clear configuration
	 */
	private void tryClearConfiguration(InheritedComponentInstanceSelDTO dto) {
		mask(pmsMessages.mskClearOverrideConfiguration());
		AsyncCallback<InheritedComponentInstanceSelDTO> callback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {

			public void onSuccess(InheritedComponentInstanceSelDTO result) {
				unmask();
				util.info(pmsMessages.msgSuccessClearConfiguration());
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgErrorClearConfiguration());
			}
		};
		componentsService.clearConfiguration(portalNameDto.getId(), dto.getComponent().getId(), callback);
	}

	/**
	 * Calls service to retrieve the inherited configuration
	 * @param dto Component what to clear configuration
	 */
	private void tryClearDependences(InheritedComponentInstanceSelDTO dto) {
		mask(pmsMessages.mskClearOverrideConfiguration());
		AsyncCallback<InheritedComponentInstanceSelDTO> callback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {

			public void onSuccess(InheritedComponentInstanceSelDTO result) {
				unmask();
				util.info(pmsMessages.msgSuccessClearConfiguration());
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgErrorClearConfiguration());
			}
		};
		componentsService.clearDependencies(portalNameDto.getId(), dto.getComponent().getId(), callback);
	}

	/**
	 * @param componentsService the componentsService to set
	 */
	@Inject
	public void setComponentsService(IComponentsServiceAsync componentsService) {
		this.componentsService = componentsService;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param idCellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setIdCellRenderer(InformationCellRenderer idCellRenderer) {
		this.idCellRenderer = idCellRenderer;
	}

	/**
	 * @param gearCellRenderer the gearCellRenderer to set
	 */
	@Inject
	public void setGearCellRenderer(GearCellRenderer gearCellRenderer) {
		this.gearCellRenderer = gearCellRenderer;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @param storeSorter the storeSorter to set
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * @param buttonsSupport the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @param errorProcessor the errorProcessor to set
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param errorMessageResolver the errorMessageResolver to set
	 */
	@Inject
	public void setErrorMessageResolver(ComponentsErrorMessageResolver errorMessageResolver) {
		this.errorMessageResolver = errorMessageResolver;
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
}
