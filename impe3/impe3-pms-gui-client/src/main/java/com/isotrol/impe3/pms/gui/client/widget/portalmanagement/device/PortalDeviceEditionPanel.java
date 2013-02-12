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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.api.DeviceNameUse;
import com.isotrol.impe3.gui.common.data.SimpleModelData;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeTreeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalDTO;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.DeviceInPortalModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.store.PortalDeviceTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Popup window that manages Portal Device.
 * 
 * @author Manuel Ruiz
 */
public class PortalDeviceEditionPanel extends TypicalWindow implements IDetailPanel {

	/**
	 * The fields that can change the "dirty" and "valid" states on this component.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * Current portal id
	 */
	private String portalId = null;

	/**
	 * Store for {@link #grid} and {@link #filter}.<br/>
	 */
	private PortalDeviceTreeStore store = null;

	/**
	 * Master view Content Types grid.<br/>
	 */
	private EditorTreeGrid<DeviceInPortalModelData> treeGrid = null;

	private PortalDevicesTemplateDTO currentPortalDevicesTemplate;

	private boolean inherited = false;

	/*
	 * Injected deps
	 */
	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;

	/**
	 * Messages app-specific service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Messages general service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Buttons support service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Helper service that contains shared objects.<br/>
	 */
	private Util util = null;

	/**
	 * PMS styles bundle.<br />
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Portals asyn service
	 */
	private IPortalsServiceAsync portalsService = null;

	private PmsSettings pmsSettings = null;

	/**
	 * Error Message Resolver for Portals service.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Constructor
	 */
	public PortalDeviceEditionPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are injected.
	 * @param inherited
	 * @param deviceInPortalDTO bound Device
	 */
	protected void init(String portalId, boolean inherited) {
		this.portalId = portalId;
		this.inherited = inherited;
		initThis();
		initComponents();
		configGridForFiringEventsOnUpdate(treeGrid);
	}

	/**
	 * Inits the component properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setWidth(Constants.SIXTY_FIVE_PERCENT);
		setHeight(500);
		setModal(true);
		setHeaderVisible(true);
		setHeading("Dispositivos");
		setClosable(false);
		setScrollMode(Scroll.AUTO);
	}

	/**
	 * Inits the inner components of this panel.<br/>
	 */
	private void initComponents() {
		addTreeGrid();
		tryGetPortalDevices();
		addButtonBar();
	}

	/**
	 * Creates and configures the button bar.<br/>
	 */
	private void addButtonBar() {
		fields = Arrays.asList(new Component[] {treeGrid});

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button button = we.getButtonClicked();
						if (button.getItemId().equals(Dialog.YES)) { // pressed
							// OK
							trySetPortalDevices();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSavePortalDevice(), listener);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, listener, new LinkedList<Component>(
			fields), pmsListeningStrategy);
		addButton(bAccept);

		final Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Adds device tree grid to the panel.<br/>
	 */
	private void addTreeGrid() {
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckColumnConfig checkColumn = new CheckColumnConfig();
		checkColumn.setId(DeviceInPortalModelData.PROPERTY_ACTIVE);
		checkColumn.setWidth(50);
		checkColumn.setHeader(pmsMessages.columnHeaderActive());
		checkColumn.setAlignment(HorizontalAlignment.CENTER);
		CellEditor checkActiveEditor = new CellEditor(new CheckBox());
		checkColumn.setEditor(checkActiveEditor);
		configs.add(checkColumn);

		CheckColumnConfig checkDefaultColumn = new CheckColumnConfig() {
			@Override
			protected void onMouseDown(GridEvent<ModelData> ge) {
				super.onMouseDown(ge);

				// only one device can be cheked as "default"
				if (ge.getColIndex() == 1) {
					ListStore<ModelData> st = ge.getGrid().getStore();
					for (int i = 0; i < ge.getGrid().getStore().getCount(); i++) {
						if (i != ge.getRowIndex()) {
							DeviceInPortalModelData model = (DeviceInPortalModelData) st.getAt(i);
							Record record = st.getRecord(model);
							record.set(DeviceInPortalModelData.PROPERTY_DEFAULT, false);
						}
					}

					// and it must be check as "active"
					DeviceInPortalModelData model = (DeviceInPortalModelData) st.getAt(ge.getRowIndex());
					if (!(Boolean) model.get(DeviceInPortalModelData.PROPERTY_ACTIVE)
						&& (Boolean) model.get(DeviceInPortalModelData.PROPERTY_DEFAULT)) {
						Record record = st.getRecord(model);
						record.set(DeviceInPortalModelData.PROPERTY_ACTIVE, true);
					}
				}
			}
		};
		checkDefaultColumn.setId(DeviceInPortalModelData.PROPERTY_DEFAULT);
		checkDefaultColumn.setWidth(50);
		checkDefaultColumn.setHeader(pmsMessages.columnHeaderDefault());
		checkDefaultColumn.setAlignment(HorizontalAlignment.CENTER);
		CellEditor checkDefaultEditor = new CellEditor(new CheckBox());
		checkDefaultColumn.setEditor(checkDefaultEditor);
		configs.add(checkDefaultColumn);

		ColumnConfig config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_NAME);
		config.setWidth(100);
		config.setRenderer(new HtmlEncodeTreeGridCellRenderer());
		config.setHeader(pmsMessages.columnHeaderDevice());
		configs.add(config);

		config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_PATH);
		config.setWidth(200);
		config.setHeader(pmsMessages.columnHeaderName());
		config.setRenderer(new HtmlEncodeGridCellRenderer());
		TextField<String> textEditor = new TextField<String>();
		config.setEditor(new CellEditor(textEditor));
		configs.add(config);

		// column "device use name" use a combo cell renderer
		config = new ColumnConfig();
		config.setId(DeviceInPortalModelData.PROPERTY_USE_NAME);
		config.setWidth(200);
		config.setHeader(pmsMessages.columnHeaderNameUse());
		final ComboBox<SimpleModelData> cbUseName = new ComboBox<SimpleModelData>();
		cbUseName.setTriggerAction(TriggerAction.ALL);
		cbUseName.setForceSelection(true);
		cbUseName.setEditable(false);
		cbUseName.setDisplayField(SimpleModelData.PROPERTY_DISPLAY_NAME);
		final ListStore<SimpleModelData> scbUseName = new ListStore<SimpleModelData>();
		cbUseName.setStore(scbUseName);
		// TODO dynamic
		// for (DeviceNameUse type : DeviceNameUse.values()) {
		// scbUseName.add(new SimpleModelData(type.toString(), pmsSettings.getMap(methodName).get(type.toString())));
		// }
		scbUseName.add(new SimpleModelData("No usado", DeviceNameUse.NONE.toString()));
		scbUseName.add(new SimpleModelData("Primer Segmento", DeviceNameUse.FIRST_SEGMENT.toString()));
		scbUseName.add(new SimpleModelData("Último Segmento", DeviceNameUse.LAST_SEGMENT.toString()));
		scbUseName.add(new SimpleModelData("Extensión", DeviceNameUse.EXTENSION.toString()));
		config.setEditor(new CellEditor(cbUseName) {
			/**
			 * (non-Javadoc)
			 * @see com.extjs.gxt.ui.client.widget.Editor#preProcessValue(java.lang.Object)
			 */
			@Override
			public Object preProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				DeviceNameUse usename = (DeviceNameUse) value;
				return scbUseName.findModel(usename.toString());
			}

			@Override
			public Object postProcessValue(Object value) {
				if (value == null) {
					return value;
				}
				SimpleModelData model = (SimpleModelData) value;
				DeviceNameUse usename = DeviceNameUse.valueOf((String) model.getId());
				return usename;
			}
		});
		config.setRenderer(new GridCellRenderer<DeviceInPortalModelData>() {

			public Object render(DeviceInPortalModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<DeviceInPortalModelData> store, Grid<DeviceInPortalModelData> grid) {
				if (model.getDTO().getUse() != null) {
					return PortalDevicesManagement.useNameMap.get(model.getDTO().getUse());
				} else {
					return null;
				}
			}
		});
		configs.add(config);

		ColumnModel cm = new ColumnModel(configs);

		store = new PortalDeviceTreeStore();

		treeGrid = new EditorTreeGrid<DeviceInPortalModelData>((PortalDeviceTreeStore) store, cm);
		treeGrid.setSelectionModel(new GridSelectionModel<DeviceInPortalModelData>());
		treeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		treeGrid.setAutoExpandColumn(DeviceInPortalModelData.PROPERTY_PATH);
		treeGrid.getStyle().setLeafIcon(IconHelper.createStyle(pmsStyles.iconTreeFolder()));
		treeGrid.setLoadMask(true);
		treeGrid.getView().setForceFit(true);
		treeGrid.addPlugin(checkDefaultColumn);
		treeGrid.addPlugin(checkColumn);

		add(treeGrid);
	}

	/**
	 * Retrieves the current portal devices and populates the tree grid.<br/>
	 */
	private void tryGetPortalDevices() {
		util.mask(pmsMessages.mskDevices());

		AsyncCallback<PortalDevicesTemplateDTO> callback = new AsyncCallback<PortalDevicesTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				util.error(pmsMessages.msgErrorRetrieveDevices());
			}

			public void onSuccess(PortalDevicesTemplateDTO arg0) {
				util.unmask();
				currentPortalDevicesTemplate = arg0;
				populateTreeGrid();
			}
		};

		portalsService.getPortalDevices(portalId, callback);
	}

	private void populateTreeGrid() {

		store.removeAll();

		for (DeviceInPortalTreeDTO child : currentPortalDevicesTemplate.getDevices()) {
			store.add(child, true);
		}
	}

	private void trySetPortalDevices() {

		List<DeviceInPortalTreeDTO> dtos = new ArrayList<DeviceInPortalTreeDTO>();
		for (DeviceInPortalModelData model : store.getRootItems()) {
			DeviceInPortalTreeDTO dto = new DeviceInPortalTreeDTO();
			dto.setNode(modelToDto(model));

			dto.setChildren(addChildrenToDeviceNode(model));

			dtos.add(dto);
		}
		currentPortalDevicesTemplate.setDevices(dtos);
		currentPortalDevicesTemplate.setInherited(inherited);
		PortalDevicesDTO devices = currentPortalDevicesTemplate.toPortalDevicesDTO();

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				hide();
				util.info(pmsMessages.msgSuccessSetPortalDevices());
			}

			public void onFailure(Throwable caught) {
				errorProcessor.processError(caught, emrPortals, pmsMessages.msgErrorSetPortalDevices());
			}
		};
		portalsService.setPortalDevices(devices, callback);
	}

	private DeviceInPortalDTO modelToDto(DeviceInPortalModelData model) {
		DeviceInPortalDTO dto = new DeviceInPortalDTO();
		dto.setActive((Boolean) model.get(DeviceInPortalModelData.PROPERTY_ACTIVE));
		dto.setDefaultDevice((Boolean) model.get(DeviceInPortalModelData.PROPERTY_DEFAULT));
		dto.setDevice(model.getDTO().getDevice());
		dto.setName((String) model.get(DeviceInPortalModelData.PROPERTY_PATH));
		dto.setUse((DeviceNameUse) model.get(DeviceInPortalModelData.PROPERTY_USE_NAME));

		return dto;
	}

	private List<DeviceInPortalTreeDTO> addChildrenToDeviceNode(DeviceInPortalModelData model) {
		List<DeviceInPortalTreeDTO> childrenDtos = new ArrayList<DeviceInPortalTreeDTO>();
		for (DeviceInPortalModelData m : store.getChildren(model)) {
			DeviceInPortalTreeDTO dto = new DeviceInPortalTreeDTO();
			dto.setNode(modelToDto(m));
			dto.setChildren(addChildrenToDeviceNode(m));
			childrenDtos.add(dto);
		}
		return childrenDtos;
	}

	/**
	 * Common configuration for all grids.<br/>
	 * 
	 * @param grid
	 */
	private void configGridForFiringEventsOnUpdate(final EditorTreeGrid<DeviceInPortalModelData> grid) {
		grid.getStore().addStoreListener(new StoreListener<DeviceInPortalModelData>() {
			@Override
			public void storeAdd(StoreEvent<DeviceInPortalModelData> se) {
				Util.fireChangeEvent(grid);
			}

			@Override
			public void storeRemove(StoreEvent<DeviceInPortalModelData> se) {
				Util.fireChangeEvent(grid);
			}

			@Override
			public void storeUpdate(StoreEvent<DeviceInPortalModelData> se) {
				Util.fireChangeEvent(grid);
			}
		});
	}

	/**
	 * @return <code>true</code>, if at least one of the panel fields is dirty.
	 * @see Field#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Component> it = fields.iterator();
		while (it.hasNext() && !dirty) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				dirty = dirty || ((Field<?>) component).isDirty();
			} else {
				dirty = dirty || gridIsDirty();
			}
		}
		return dirty;
	}

	private boolean gridIsDirty() {
		return !store.getModifiedRecords().isEmpty();
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Component> it = fields.iterator();
		while (valid && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				valid = valid && ((Field<?>) component).isValid();
			} else {
				valid = valid && gridIsValid();
			}
		}
		return valid;
	}

	/**
	 * true if there is a model with property "default" equals to <code>true</code> and every model active has a name
	 * not null and this not exists
	 * @return true if grid is valid
	 */
	private boolean gridIsValid() {
		// find a model with property "default" equals to true
		DeviceInPortalModelData model = store.findModel(DeviceInPortalModelData.PROPERTY_DEFAULT, true);

		List<DeviceInPortalModelData> models = store.findModels(DeviceInPortalModelData.PROPERTY_ACTIVE, true);
		Set<String> names = new HashSet<String>();
		for (DeviceInPortalModelData m : models) {
			// find models actives and checks if property name is null
			String path = m.get(DeviceInPortalModelData.PROPERTY_PATH);
			if (path == null) {
				return false;
			}
			// model names can't be duplicated
			if (!names.add(path)) {
				return false;
			}

		}

		return model != null;
	}

	public boolean isEdition() {
		return true;
	}

	/**
	 * @return the pmsMessages service
	 */
	protected PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the messages service.
	 */
	protected GuiCommonMessages getMessages() {
		return messages;
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
	 * Injects the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the buttons support object.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the statics container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = (IComponentListeningStrategy) pmsListeningStrategy;
	}

	/**
	 * @param portalsService the portalsService to set
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param pmsSettings the pmsSettings to set
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
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
	 * @param errorProcessor the error processor.
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
}
