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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.configurations;

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
//import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.renderer.GearCellRenderer;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO.EstadoHerencia;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
//mport com.isotrol.impe3.pms.api.portalConfig.PortalConfigurationInstanceSelDTO;


import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;

import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.InheritedComponentInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalConfigurationInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ComponentsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.ComponentsImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.InheritPortalConfigurationWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.InheritedComponentsExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.OverrideConfigurationWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.PortalConfigurationWindow;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;

public class PortalConfigurationsManagement extends PmsContentPanel  {
	
	/**
	 * Width in pixels for column <b>Module</b><br/>
	 */
	private static final int COLUMN_MODULE_WIDTH = 450;

	/**
	 * Width in pixels for column <b>Name</b><br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 300;

	/**
	 * Width in pixels for column <b>Configuration</b><br/>
	 */
	private static final int COLUMN_CONFIGURATION_WIDTH =100;

	/**
	 * Width in pixels for column <b>Dependencies</b><br/>
	 */
	private static final int COLUMN_DEPENDENCIES_WIDTH = 150;

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
	private ListStore<PortalConfigurationInstanceSelModelData> store = null;

	/** Components grid */
	private Grid<PortalConfigurationInstanceSelModelData> grid = null;

	/**
	 * Filter for grid<br/>
	 */
	private CustomizableStoreFilter<PortalConfigurationInstanceSelModelData> filter = null;

	private Button ttiOverrideConfiguration = null;
	private Button ttiEditConfiguration = null;
	private Button ttiEditFatherConfiguration = null;
	private Button ttiInheritConfiguration = null;

	/*
	 * Injected deps
	 */
	/**
	 * Components service proxy.<br/>
	 */
	private IPortalsServiceAsync portalService = null;

	/**
	 * Generic service messages.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	
	private InformationCellRenderer idCellRenderer = null;


	/**
	 * <b>ID</b> cell renderer in the {@link #gComponents grid}.<br/>
	 */
	//private InformationCellRenderer idCellRenderer = null;

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
	
	
	public void init(PortalNameDTO model) {
		this.portalNameDto = model;

		initThis();
		initComponent();
		addListeners();
		tryGetConfigurations();
	}
	
	private void initThis() {
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);
	}
	
	private void initComponent() {
		addGrid();
		addToolBars();
	}
	
	/**
	 * Creates and adds the list where adding the components
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();


		ColumnConfig column = new ColumnConfig();
		column.setId(PortalConfigurationInstanceSelModelData.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PortalConfigurationInstanceSelModelData.PROPERTY_DESCRIPTION);
		column.setHeaderText(pmsMessages.columnHeaderDescription());
		column.setWidth(COLUMN_MODULE_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PortalConfigurationInstanceSelModelData.PROPERTY_VALIDITY);
		column.setHeaderText(pmsMessages.columnHeaderValidity());
		column.setWidth(COLUMN_CONFIGURATION_WIDTH);
		//configs.add(column);
		column.setRenderer(new GridCellRenderer<PortalConfigurationInstanceSelModelData>() {

			public Object render(PortalConfigurationInstanceSelModelData model, String property, ColumnData config,
				int rowIndex, int colIndex, ListStore<PortalConfigurationInstanceSelModelData> store,
				Grid<PortalConfigurationInstanceSelModelData> grid) {

				String icon = null;
				String title = null;
				Boolean configuration = model.getDTO().isValidity();
				if (configuration == null) {
					return "";
				} else if (configuration) {
					icon = Constants.OK_IMAGE;
					title = pmsMessages.titlePortalConfigurationValid();
				} else {
					icon = Constants.ERROR_IMAGE;
					title = pmsMessages.titlePortalConfigurationInValid();
				}
				return TEMPLATE_ICON_CELL.replaceAll(PATTERN_ICON, icon).replaceAll(PATTERN_TITLE, title);
			}
		});
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PortalConfigurationInstanceSelModelData.PROPERTY_HERENCY);
		column.setHeaderText(pmsMessages.columnHeaderHerency());
		column.setWidth(COLUMN_DEPENDENCIES_WIDTH);
		
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<PortalConfigurationInstanceSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(PortalConfigurationInstanceSelModelData.PROPERTY_NAME);

		grid = new Grid<PortalConfigurationInstanceSelModelData>(
			(ListStore<PortalConfigurationInstanceSelModelData>) store, cm);
		grid.setSelectionModel(new GridSelectionModel<PortalConfigurationInstanceSelModelData>());
		grid.setAutoExpandColumn(PortalConfigurationInstanceSelModelData.PROPERTY_NAME);
		grid.setLoadMask(true);

		GridView gridView = grid.getView();
		gridView.setForceFit(true);

		add(grid);
		
	}
	
	private void addToolBars() {
		ToolBar toolBar = new ToolBar();
		ToolBar bottomToolBar = new ToolBar();
		
		ttiEditConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelEditConfiguration(),
				pmsMessages.ttEditConfiguration(), guiCommonStyles.iEdit(), toolBar,
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final PortalConfigurationInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
						tryGetConfiguration(selected.getDTO().getId(), selected.getDTO().getBeanName());
						
					}
				});
			ttiEditConfiguration.setEnabled(false);
			toolBar.add(new SeparatorToolItem());

		// override configuration button
		ttiOverrideConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelOverrideConfiguration(),
			pmsMessages.ttOverrideConfiguration(), guiCommonStyles.iEdit(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					final PortalConfigurationInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
					tryGetConfiguration(selected.getDTO().getId(), selected.getDTO().getBeanName());
				}
			});
		ttiOverrideConfiguration.setEnabled(false);
		toolBar.add(new SeparatorToolItem());

		// inherit configuration button
		ttiEditFatherConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelEditFatherConfiguration(), pmsMessages
			.ttEditFatherConfiguration(), guiCommonStyles.iEdit(), toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final PortalConfigurationInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
				tryGetInheritedConfiguration(selected.getDTO().getId(), selected.getDTO().getBeanName());
			}
		});
		ttiEditFatherConfiguration.setEnabled(false);
		toolBar.add(new SeparatorToolItem());
		
			ttiInheritConfiguration = buttonsSupport.addGenericButton(pmsMessages.labelInheritConfiguration(), pmsMessages
				.ttInheritConfiguration(), guiCommonStyles.iDelete(), toolBar, new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					final PortalConfigurationInstanceSelModelData selected = grid.getSelectionModel().getSelectedItem();
					if (selected != null) {
						Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent be) {
								Button clicked = be.getButtonClicked();
								if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
									tryClearConfiguration(selected.getDTO().getBeanName());
									
								}
							}
						};
						MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmClearPortalConfiguration(),
							lConfirm).setModal(true);
					}
				}
			});
			ttiInheritConfiguration.disable();
			//toolBar.add(new SeparatorToolItem());
			toolBar.add(new FillToolItem());
			filter = new CustomizableStoreFilter<PortalConfigurationInstanceSelModelData>(Arrays.asList(new String[] {
					PortalConfigurationInstanceSelModelData.PROPERTY_NAME, PortalConfigurationInstanceSelModelData.PROPERTY_DESCRIPTION,
					PortalConfigurationInstanceSelModelData.PROPERTY_HERENCY, PortalConfigurationInstanceSelModelData.PROPERTY_VALIDITY}));
			filter.setHideLabel(false);
			filter.setFieldLabel(messages.labelFilter());
			filter.bind(store);
			toolBar.add(filter);

			buttonsSupport.addRefreshButton(toolBar, new SelectionListener<IconButtonEvent>() {
				@Override
				public void componentSelected(IconButtonEvent ce) {
					tryGetConfigurations();
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
	

	private void addListeners() {

		final PortalsController serviceController = (PortalsController) portalService;
		
		 //* service controller listener:
		 
		final ChangeListener changeListener = new ChangeListener() {
			public void modelChanged(ChangeEvent ce) {
				PmsChangeEvent event = (PmsChangeEvent) ce;
				switch (event.getType()) {
					case PmsChangeEvent.UPDATE:
						tryGetConfigurations();
						break;
					case PmsChangeEvent.IMPORT:
						tryGetConfigurations();
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
		 
		grid.addListener(Events.RowClick, new Listener<GridEvent<PortalConfigurationInstanceSelModelData>>() {
			public void handleEvent(GridEvent<PortalConfigurationInstanceSelModelData> ge) {
				PortalConfigurationInstanceSelModelData model = grid.getSelectionModel().getSelectedItem();
				if (model != null) {
					enableDisableButtons(model);
				}
			}
		});
	}

	private void enableDisableButtons(PortalConfigurationInstanceSelModelData model) {
		EstadoHerencia configuration = model.getDTO().getInherited();
	

	if (configuration == null) {
		ttiOverrideConfiguration.disable();
		ttiEditConfiguration.disable();;
		ttiEditFatherConfiguration.disable();
		ttiInheritConfiguration.disable();
	} else if(configuration==EstadoHerencia.PROPIO) {
		ttiEditConfiguration.setEnabled(true);
		ttiEditFatherConfiguration.disable();
		ttiInheritConfiguration.disable();
		ttiOverrideConfiguration.disable();
	} else if(configuration==EstadoHerencia.HEREDADO) {
		ttiEditConfiguration.disable();
		ttiEditFatherConfiguration.setEnabled(true);;
		ttiInheritConfiguration.disable();
		ttiOverrideConfiguration.setEnabled(true);
	}else if(configuration==EstadoHerencia.SOBREESCRITO){
		ttiEditConfiguration.setEnabled(true);
		ttiEditFatherConfiguration.disable();;
		ttiInheritConfiguration.setEnabled(true);
		ttiOverrideConfiguration.disable();

	}
	}
	
	private void tryClearConfiguration(String bean) {
		mask(pmsMessages.mskClearOverrideConfiguration());
		AsyncCallback<ConfigurationTemplateDTO> callback = new AsyncCallback<ConfigurationTemplateDTO>() {

			public void onSuccess(ConfigurationTemplateDTO result) {
				unmask();
				util.info(pmsMessages.msgSuccessClearConfiguration());
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgErrorClearConfiguration());
			}
		};
		
		portalService.clearConfiguration(portalNameDto.getId(),bean, callback);
	}
	
	private void tryGetConfiguration(final String id, final String bean) {
		mask(pmsMessages.mskComponent());

		AsyncCallback<ConfigurationTemplateDTO> callback = new AsyncCallback<ConfigurationTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponent());
			}

			public void onSuccess(ConfigurationTemplateDTO arg0) {
				showConfiguration(arg0,bean,false);
				unmask();
			}
		};
		portalService.getPortalConfiguration(portalNameDto.getId(),bean, callback);
	}
	
	private void tryGetInheritedConfiguration(final String id, final String bean) {
		mask(pmsMessages.mskComponent());

		AsyncCallback<ConfigurationTemplateDTO> callback = new AsyncCallback<ConfigurationTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponent());
			}

			public void onSuccess(ConfigurationTemplateDTO arg0) {
				showInheritedConfiguration(arg0,bean,true);
				unmask();
			}
		};
		portalService.getPortalConfiguration(portalNameDto.getId(),bean, callback);
	}
	
	private void showConfiguration(ConfigurationTemplateDTO configurationTemplate, String bean,boolean inherited) {
		PortalConfigurationWindow configurationDetailPanel = PmsFactory.getInstance().getPortalConfigurationWindow();
		configurationDetailPanel.init(configurationTemplate, portalNameDto.getId(), bean,inherited);
		configurationDetailPanel.show();
	}
	private void showInheritedConfiguration(ConfigurationTemplateDTO configurationTemplate, String bean,boolean inherited) {
		InheritPortalConfigurationWindow configurationDetailPanel = PmsFactory.getInstance().getInheritPortalConfigurationWindow();
		configurationDetailPanel.init(configurationTemplate, portalNameDto.getId(), bean,inherited);
		configurationDetailPanel.show();
	}
	
	/**
	 * Retrieves the components instances registered for the bound portal.<br/>
	 */
	private void tryGetConfigurations() {
		util.mask(pmsMessages.mskConfigurations());
		AsyncCallback<List<PortalConfigurationSelDTO>> callback = new AsyncCallback<List<PortalConfigurationSelDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponents());
			}


			@Override
			public void onSuccess(List<PortalConfigurationSelDTO> arg0) {
				store.removeAll();
				store.clearFilters();
				filter.setValue(null);

				List<PortalConfigurationInstanceSelModelData> configurationsModelData = new LinkedList<PortalConfigurationInstanceSelModelData>();
				for (PortalConfigurationSelDTO dto : arg0) {
					configurationsModelData.add(new PortalConfigurationInstanceSelModelData(dto));
				}
				store.add(configurationsModelData);

				util.unmask();
				
				
			}
		};
		portalService.getPortalConfigurations(portalNameDto.getId(), callback);
	

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

	/**
	 * @param componentsService the componentsService to set
	 */
	@Inject
	public void setPortalService(IPortalsServiceAsync portalService) {
		this.portalService = portalService;
	}

}
