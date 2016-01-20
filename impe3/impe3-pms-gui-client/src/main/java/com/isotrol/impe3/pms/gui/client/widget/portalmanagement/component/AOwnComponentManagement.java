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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
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
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
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
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.ComponentsController;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractModuleModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ComponentModuleModelData;
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
 * Widget that manages the portal's own components
 * @author Manuel Ruiz
 * 
 */
public abstract class AOwnComponentManagement extends PmsContentPanel {

	/*
	 * Components widths
	 */
	/**
	 * Width in pixels for column <b>Module</b><br/>
	 */
	private static final int COLUMN_MODULE_WIDTH = 200;

	/**
	 * Width in pixels for column <b>Name</b><br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 100;

	/**
	 * Component type selector inner height<br/> (just body height, without frame details like header & margins.)
	 */
	private static final int SELECTOR_HEIGHT = 500;

	/** components store */
	private ListStore<ModuleInstanceSelModelData> store = null;

	/**
	 * "Delete" menu item.<br/>
	 */
	private Button ttiDelete = null;
	/**
	 * "Edit" menu item.<br/>
	 */
	private Button ttiEdit = null;

	/**
	 * Filter for grid<br/>
	 */
	private CustomizableStoreFilter<ModuleInstanceSelModelData> filter = null;

	/** components grid */
	private Grid<ModuleInstanceSelModelData> grid = null;

	/**
	 * Portal data bound to the widget instance.<br/>
	 */
	private PortalNameDTO portalNameDto = null;

	/**
	 * Component type selector (displayed when creating a new instance.)<br/>
	 */
	private Window wComponentsSelector = null;

	/**
	 * Components grid in components selector.<br/>
	 */
	private Grid<ComponentModuleModelData> gComponents = null;

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
	 * Common styles for PMS and Users apps.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Pms styles.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper object<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Error messages resolver for ComponentsService.<br/>
	 */
	private ComponentsErrorMessageResolver errorMessageResolver = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	protected abstract void callGetComponents(String portalId, AsyncCallback<List<ModuleInstanceSelDTO>> callback);

	/**
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	@Override
	protected void beforeRender() {

		assert this.portalNameDto != null : "portalNameDto must be set before";

		initThis();
		initComponent();
		addListeners();
		tryGetComponents();
	}

	/**
	 * Sets the portal
	 * @param model the portal name dto
	 */
	public void setPortalNameDTO(PortalNameDTO model) {
		this.portalNameDto = model;
	}

	/**
	 * Configures this
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);
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
			.columnHeaderId(), Constants.COLUMN_ICON_WIDTH);
		column.setSortable(false);
		column.setRenderer(idCellRenderer);
		configs.add(column);

		column = new ColumnConfig(Constants.PROPERTY_KEY, Constants.EMPTY_STRING, Constants.COLUMN_ICON_WIDTH);
		column.setRenderer(gearCellRenderer);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(Constants.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(ModuleInstanceSelModelData.PROPERTY_MODULE);
		column.setHeaderText(pmsMessages.columnHeaderModule());
		column.setWidth(COLUMN_MODULE_WIDTH);
		configs.add(column);
		addSpecificColumns(configs);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<ModuleInstanceSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(ModuleInstanceSelModelData.PROPERTY_NAME);

		grid = new Grid<ModuleInstanceSelModelData>((ListStore<ModuleInstanceSelModelData>) store, cm);
		grid.setSelectionModel(new GridSelectionModel<ModuleInstanceSelModelData>());
		grid.setAutoExpandColumn(ModuleInstanceSelModelData.PROPERTY_MODULE);
		grid.setLoadMask(true);

		GridView gridView = grid.getView();
		gridView.setForceFit(true);

		add(grid);
	}

	private void addToolBars() {
		ToolBar toolBar = new ToolBar();
		ToolBar bottomToolBar = new ToolBar();

		ttiDelete = new Button(messages.labelDelete());
		ttiDelete.setIconStyle(styles.iDelete());
		toolBar.add(ttiDelete);
		ttiDelete.setEnabled(false);

		toolBar.add(new SeparatorToolItem());

		ttiEdit = new Button(messages.labelEdit());
		ttiEdit.setIconStyle(styles.iEdit());
		toolBar.add(ttiEdit);
		ttiEdit.setEnabled(false);

		toolBar.add(new FillToolItem());

		filter = new CustomizableStoreFilter<ModuleInstanceSelModelData>(Arrays.asList(new String[] {
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

		addSpecificToolItems(toolBar);

		// Export button
		Button ttiExport = buttonsSupport.createGenericButton(getPmsMessages().labelExport(), pmsStyles.exportIcon(),
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ComponentsExportWindow comWindow = PmsFactory.getInstance().getComponentsExportWindow();
					comWindow.setPortalId(portalNameDto.getId());
					comWindow.show();
				}
			});
		bottomToolBar.add(ttiExport);

		bottomToolBar.add(new SeparatorToolItem());

		// Import button
		Button ttiImport = buttonsSupport.createGenericButton(getPmsMessages().labelImport(), pmsStyles.importIcon(),
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ComponentsImportWindow comWindow = PmsFactory.getInstance().getComponentsImportWindow();
					comWindow.setPortalId(portalNameDto.getId());
					comWindow.show();
				}
			});
		bottomToolBar.add(ttiImport);

		setTopComponent(toolBar);
		setBottomComponent(bottomToolBar);
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
	 * Retrieves the components instances registered for the bound portal.<br/>
	 */
	private void tryGetComponents() {
		getUtil().mask(getPmsMessages().mskComponents());

		AsyncCallback<List<ModuleInstanceSelDTO>> callback = new AsyncCallback<List<ModuleInstanceSelDTO>>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getErrorMessageResolver(),
					getPmsMessages().msgErrorRetrieveComponents());
			}

			public void onSuccess(List<ModuleInstanceSelDTO> arg0) {
				getStore().removeAll();
//				getStore().clearFilters();
//				getFilter().setValue(null);

				List<ModuleInstanceSelModelData> ownsModelData = new LinkedList<ModuleInstanceSelModelData>();
				if (arg0 != null) {
					for (ModuleInstanceSelDTO dto : arg0) {
						ownsModelData.add(new ModuleInstanceSelModelData(dto));
					}
				}
				getStore().add(ownsModelData);

				getUtil().unmask();
			}
		};

		callGetComponents(getPortalNameDto().getId(), callback);
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
					case PmsChangeEvent.ADD:
					case PmsChangeEvent.UPDATE:
					case PmsChangeEvent.IMPORT:
						tryGetComponents();
						break;
					case PmsChangeEvent.DELETE:
						onComponentRemove((String) event.getEventInfo());
						tryGetComponents();
						break;
					default: // event not expected: nothing to do here.
						break;
				}
			}
		};
		serviceController.addChangeListener(changeListener);

		addListener(Events.Detach, new Listener<BaseEvent>() {

			public void handleEvent(BaseEvent be) {
				serviceController.removeChangeListener(changeListener);
			}
		});

		/*
		 * Grid RowClick listener
		 */
		grid.addListener(Events.RowClick, new Listener<GridEvent<ModuleInstanceSelModelData>>() {
			public void handleEvent(GridEvent<ModuleInstanceSelModelData> ge) {
				ModuleInstanceSelModelData model = grid.getSelectionModel().getSelectedItem();
				boolean enabled = false;
				if (model != null) {
					enabled = true;
				}
				ttiEdit.setEnabled(enabled);
				ttiDelete.setEnabled(enabled);
			}
		});

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ModuleInstanceSelModelData>>() {
			/**
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(GridEvent<ModuleInstanceSelModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		ttiDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked.getItemId().equals(Dialog.YES)) { // confirmed
							ModuleInstanceSelModelData modelData = grid.getSelectionModel().getSelectedItem();
							if (modelData != null) {
								ModuleInstanceSelDTO dto = modelData.getDTO();
								tryDeleteInstance(dto.getId());
							}
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteComponent(), listener);
			}
		});

		ttiEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails();
			}
		});
	}

	/**
	 * Deletes the Component instance identified by its ID.<br/>
	 * @param id
	 */
	private void tryDeleteInstance(String id) {
		util.mask(pmsMessages.mskDeleteComponent());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorDeleteComponent());
			}

			public void onSuccess(Void arg0) {
				util.unmask();
				util.info(pmsMessages.msgSuccessDeleteComponent());
			}
		};

		componentsService.delete(portalNameDto.getId(), id, callback);
	}

	/**
	 * Removes from the own Components grid store the ModelDta whose <b>ID</b> property is equal to the passed
	 * param.<br/> Called after a Component was deleted.
	 * 
	 * @param id the <b>ID</b> of the removed component
	 */
	private void onComponentRemove(String id) {
		ModuleInstanceSelModelData deleted = store.findModel(ModuleInstanceSelModelData.PROPERTY_ID, id);
		store.remove(deleted);
	}

	/**
	 * Retrieves the selected Component instance from service, and then displays it in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		ModuleInstanceSelModelData modelData = grid.getSelectionModel().getSelectedItem();
		if (modelData != null) {
			ModuleInstanceSelDTO dto = modelData.getDTO();
			tryGetDetails(dto.getId());
		}
	}

	/**
	 * Requests the service for available components modules.<br/> On success, shows the components selector.
	 */
	protected void tryGetComponentModules() {
		AsyncCallback<List<ComponentModuleDTO>> callback = new AsyncCallback<List<ComponentModuleDTO>>() {
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponents());
			}

			public void onSuccess(List<ComponentModuleDTO> components) {
				showComponentsSelector(components);
			}
		};

		componentsService.getComponentModules(callback);
	}

	private void showComponentsSelector(List<ComponentModuleDTO> lComponents) {
		// always create, destroy on close:
		addComponentsSelector();

		ListStore<ComponentModuleModelData> store = gComponents.getStore();

		List<ComponentModuleModelData> list = new LinkedList<ComponentModuleModelData>();
		for (ComponentModuleDTO dto : lComponents) {
			list.add(new ComponentModuleModelData(dto));
		}

		store.removeAll();
		store.add(list);

		wComponentsSelector.show();
		wComponentsSelector.center();
	}

	/**
	 * Gets the details of the Component passed by ID.<br/> On success, opens the Component details window with the
	 * obtained data.
	 * @param id
	 */
	private void tryGetDetails(final String id) {
		util.mask(pmsMessages.mskComponent());

		AsyncCallback<ModuleInstanceTemplateDTO> callback = new AsyncCallback<ModuleInstanceTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponent());
			}

			public void onSuccess(ModuleInstanceTemplateDTO arg0) {
				showComponentDetails(arg0);
				util.unmask();
			}
		};

		componentsService.get(portalNameDto.getId(), id, callback);
	}

	/**
	 * Shows the component details window.<br/>
	 * @param template
	 */
	private void showComponentDetails(ModuleInstanceTemplateDTO template) {
		ComponentDetailPanel componentDetailPanel = PmsFactory.getInstance().getComponentDetailPanel();
		componentDetailPanel.init(portalNameDto.getId(), template);
		componentDetailPanel.show();
	}

	/**
	 * Creates the components selector window. Does not show it.<br/>
	 */
	private void addComponentsSelector() {
		wComponentsSelector = new Window();

		wComponentsSelector.setHeadingText(pmsMessages.headerComponentSelector());
		wComponentsSelector.setModal(true);
		wComponentsSelector.setResizable(false);
		wComponentsSelector.setWidth(Constants.SIXTY_FIVE_PERCENT);
		wComponentsSelector.setShadow(false);
		wComponentsSelector.setButtonAlign(HorizontalAlignment.LEFT);

		LayoutContainer pConnectorsSelector = new LayoutContainer(new FitLayout());
		pConnectorsSelector.setHeight(SELECTOR_HEIGHT);

		// the grid column model:
		ColumnConfig cName = new ColumnConfig();
		cName.setId(AbstractModuleModelData.PROPERTY_NAME);
		cName.setHeaderText(pmsMessages.columnHeaderName());
		cName.setWidth(350);

		ColumnConfig cDesc = new ColumnConfig();
		cDesc.setId(AbstractModuleModelData.PROPERTY_DESCRIPTION);
		cDesc.setHeaderText(pmsMessages.columnHeaderDescription());
		cDesc.setRenderer(new GridCellRenderer<ComponentModuleModelData>() {
			public Object render(ComponentModuleModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<ComponentModuleModelData> store, Grid<ComponentModuleModelData> grid) {
				String desc = model.get(property);
				if (desc != null && !desc.equals("")) {
					return "<span title='" + desc + "'>" + desc + "<span>";
				}
				return "";
			}
		});
		cDesc.setWidth(500);

		ColumnModel columnModel = new ColumnModel(Arrays.asList(new ColumnConfig[] {cName, cDesc}));

		// the grid store:
		ListStore<ComponentModuleModelData> store = new ListStore<ComponentModuleModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(AbstractModuleModelData.PROPERTY_NAME);

		gComponents = new Grid<ComponentModuleModelData>(store, columnModel);
		pConnectorsSelector.add(gComponents);

		GridSelectionModel<ComponentModuleModelData> sm = new GridSelectionModel<ComponentModuleModelData>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		gComponents.setSelectionModel(sm);

		gComponents.setLoadMask(true);
		gComponents.getView().setForceFit(true);
		gComponents.setAutoExpandColumn(AbstractModuleModelData.PROPERTY_DESCRIPTION);

		wComponentsSelector.add(pConnectorsSelector);

		// top toolbar:
		ToolBar toolBar = new ToolBar();

		StoreFilterField<ComponentModuleModelData> selectorFilter = new CustomizableStoreFilter<ComponentModuleModelData>(
			Arrays.asList(new String[] {AbstractModuleModelData.PROPERTY_NAME,
				AbstractModuleModelData.PROPERTY_DESCRIPTION}));
		selectorFilter.setHideLabel(false);
		selectorFilter.setFieldLabel(messages.labelFilter());
		selectorFilter.bind(store);
		toolBar.add(selectorFilter);

		wComponentsSelector.setTopComponent(toolBar);

		// bottom ButtonBar:
		wComponentsSelector.addButton(buttonsSupport.createAcceptButton(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ComponentModuleModelData selectedItem = gComponents.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					tryGetNewComponentTemplate(selectedItem.getDTO().getId());
					wComponentsSelector.hide();
				}
			}
		}));
	}

	protected void addSpecificColumns(List<ColumnConfig> configs) {
	}

	/**
	 * Requests the service for a new component template of the passed kind.<br/>
	 * @param key
	 */
	private void tryGetNewComponentTemplate(String key) {
		AsyncCallback<ModuleInstanceTemplateDTO> callback = new AsyncCallback<ModuleInstanceTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				errorProcessor
					.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveComponentTemplate());
			}

			public void onSuccess(ModuleInstanceTemplateDTO arg0) {
				showComponentDetails(arg0);
			}
		};

		componentsService.newTemplate(key, callback);
	}

	/**
	 * @param componentsService the componentsService to set
	 */
	@Inject
	public void setComponentsService(IComponentsServiceAsync componentsService) {
		this.componentsService = componentsService;
	}

	/**
	 * @return the componentsService
	 */
	public IComponentsServiceAsync getComponentsService() {
		return componentsService;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @return the messages
	 */
	public GuiCommonMessages getMessages() {
		return messages;
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
	 * @return the pmsMessages
	 */
	public PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the buttonsSupport
	 */
	public Buttons getButtonsSupport() {
		return buttonsSupport;
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
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the errorProcessor
	 */
	public ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * Injects the components service error message resolver.
	 * @param errorMessageResolver the errorMessageResolver to set
	 */
	@Inject
	public void setErrorMessageResolver(ComponentsErrorMessageResolver errorMessageResolver) {
		this.errorMessageResolver = errorMessageResolver;
	}

	/**
	 * @return the errorMessageResolver
	 */
	public ComponentsErrorMessageResolver getErrorMessageResolver() {
		return errorMessageResolver;
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
	 * @return the util
	 */
	public Util getUtil() {
		return util;
	}

	/**
	 * @return the styles
	 */
	public GuiCommonStyles getStyles() {
		return styles;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the store
	 */
	public ListStore<ModuleInstanceSelModelData> getStore() {
		return store;
	}

	/**
	 * @param storeSorter the storeSorter to set
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * @return the filter
	 */
	public CustomizableStoreFilter<ModuleInstanceSelModelData> getFilter() {
		return filter;
	}

	/**
	 * @return the portalNameDto
	 */
	public PortalNameDTO getPortalNameDto() {
		return portalNameDto;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
}
