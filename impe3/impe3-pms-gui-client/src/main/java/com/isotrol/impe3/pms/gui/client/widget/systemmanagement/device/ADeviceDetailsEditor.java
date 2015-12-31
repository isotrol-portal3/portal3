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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.gui.common.data.SimpleModelData;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PropertyModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Popup window that manages a Device details.
 * 
 * @author Manuel Ruiz
 */
public abstract class ADeviceDetailsEditor extends TypicalWindow implements IDetailPanel {

	/**
	 * The device "name" field<br/>
	 */
	private TextField<String> tfName = null;

	/**
	 * The device "type" field.<br/>
	 */
	private ComboBox<SimpleModelData> cbType = null;

	/**
	 * The device "description" field<br/>
	 */
	private TextArea taDescription = null;

	/**
	 * The device "user agent" field<br/>
	 */
	private TextField<String> tfUserAgent = null;

	/**
	 * The device "user agent is regular expression" field<br/>
	 */
	private CheckBox cbUserAgentRE = null;

	/**
	 * "Layout width" field<br/>
	 */
	private NumberField tfLayoutWidth = null;

	/**
	 * The fields that can change the "dirty" and "valid" states on this component.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * Main layout container. Only direct child of this class panel.<br/>
	 */
	private LayoutContainer container = null;

	/**
	 * Data bound to current widget.<br/>
	 */
	private DeviceDTO deviceDto = null;

	/**
	 * Properties grid.<br/>
	 */
	private Grid<PropertyModelData> grid = null;

	/**
	 * Set to <code>true</code>, when a field in grid has been changed, added or removed<br/>
	 */
	private boolean propertiesGridDirty = false;

	private Button deleteItem = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;
	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;
	/**
	 * Proxy to Devices async service.<br/>
	 */
	private IDevicesServiceAsync devicesService = null;

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
	 * Forms support service.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * Helper service that contains shared objects.<br/>
	 */
	private Util util = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Generic styles bundle.<br />
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Constructor<br/>
	 */
	public ADeviceDetailsEditor() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are injected.
	 * @param dev bound Device
	 */
	protected void init(DeviceDTO dev) {
		this.deviceDto = dev;

		initThis();
		initComponents();
	}

	/**
	 * Inits the component properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setWidth(Constants.FORM_WINDOW_WIDTH);
		setAutoHeight(true);
		setModal(true);
		setHeaderVisible(true);
		setHeadingText(getHeadingText());
		setClosable(false);
		setScrollMode(Scroll.NONE);

		container = new LayoutContainer(formSupport.getStandardLayout(false));
		container.addStyleName(getStyles().margin10px());
		container.setBorders(false);
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Inits the inner components of this panel.<br/>
	 */
	private void initComponents() {

		addFields();

		// must be called before addButtonBar();
		displayBoundDataValues();

		addButtonBar();
	}

	/**
	 * Creates and configures the button bar.<br/>
	 */
	private void addButtonBar() {
		if (deviceDto.isDefaultDevice()) {
			fields = Arrays.asList(new Component[] {tfName, cbType});
		} else {
			fields = Arrays.asList(new Component[] {tfName, cbType, taDescription, tfUserAgent, cbUserAgentRE, grid,
				tfLayoutWidth});
		}

		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, getAcceptButtonListener(),
			new LinkedList<Component>(fields), pmsListeningStrategy);
		addButton(bAccept);

		final Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Adds the fields to the panel.<br/>
	 */
	private void addFields() {

		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setAutoValidate(true);
		tfName.setValidator(nonEmptyStringValidator);
		// perform validation on render:
		tfName.addListener(Events.Render, validatorListener);
		if (deviceDto.isDefaultDevice()) {
			tfName.setReadOnly(true);
		}
		container.add(tfName);

		cbType = new ComboBox<SimpleModelData>();
		cbType.setFieldLabel(pmsMessages.labelType());
		cbType.setDisplayField(SimpleModelData.PROPERTY_DISPLAY_NAME);
		cbType.setTriggerAction(TriggerAction.ALL);
		cbType.setAllowBlank(false);
		cbType.setForceSelection(true);
		cbType.setEditable(false);
		cbType.addListener(Events.Render, validatorListener);
		// store population:
		ListStore<SimpleModelData> daStore = new ListStore<SimpleModelData>();
		cbType.setStore(daStore);
		for (DeviceType type : DeviceType.values()) {
			if (!(deviceDto.isDefaultDevice() && type.equals(DeviceType.ATOM))) {
				daStore.add(new SimpleModelData(type.toString(), type.toString()));
			}
		}

		if (!deviceDto.isDefaultDevice()) {
			// if type is layout, layout width is required
			cbType.addSelectionChangedListener(new SelectionChangedListener<SimpleModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<SimpleModelData> se) {
					SimpleModelData selected = se.getSelectedItem();
					String key = selected.get(SimpleModelData.PROPERTY_ID);
					if (DeviceType.valueOf(key).isLayout()) {
						tfLayoutWidth.setAllowBlank(false);
						if (tfLayoutWidth.getValue() == null) {
							tfLayoutWidth.setValue(DeviceType.valueOf(key).getWidth());
							tfLayoutWidth.setOriginalValue(DeviceType.valueOf(key).getWidth());
						}
					} else {
						tfLayoutWidth.setAllowBlank(true);
					}
				}
			});
		}
		container.add(cbType);

		// fields added if device is not the default device
		if (!deviceDto.isDefaultDevice()) {
			// field "layou width"
			tfLayoutWidth = new NumberField();
			tfLayoutWidth.setPropertyEditorType(Integer.class);
			tfLayoutWidth.setFieldLabel(pmsMessages.labelLayoutWidth());
			tfLayoutWidth.setAutoValidate(true);
			tfLayoutWidth.setValidateOnBlur(true);
			if (deviceDto.isLayout()) {
				tfLayoutWidth.setAllowBlank(false);
			}
			container.add(tfLayoutWidth);

			taDescription = new TextArea();
			taDescription.setFieldLabel(pmsMessages.labelDescription());
			container.add(taDescription);

			tfUserAgent = new TextField<String>();
			tfUserAgent.setFieldLabel(pmsMessages.labelUserAgent());
			container.add(tfUserAgent);

			cbUserAgentRE = new CheckBox();
			cbUserAgentRE.setFieldLabel(pmsMessages.labelUserAgentRE());
			cbUserAgentRE.addInputStyleName(styles.checkBoxAlignLeft());
			container.add(cbUserAgentRE);

			addPropertiesGrid();
		}
	}

	private void addPropertiesGrid() {
		ContentPanel containerGrid = new ContentPanel(new FitLayout());
		containerGrid.setHeadingText(pmsMessages.headerPropertiesManagement());
		containerGrid.setStyleAttribute("margin", "0 0 10px");
		containerGrid.setHeight(250);

		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<PropertyModelData> sm = new CheckBoxSelectionModel<PropertyModelData>();
		cc.add(sm.getColumn());
		sm.addSelectionChangedListener(new SelectionChangedListener<PropertyModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<PropertyModelData> se) {
				if (se.getSelectedItem() != null) {
					deleteItem.enable();
				}
			}
		});

		ColumnConfig ccName = new ColumnConfig(PropertyModelData.PROPERTY_NAME, pmsMessages.columnHeaderName(), 100);
		TextField<String> tfNameEditor = new TextField<String>();
		tfNameEditor.setAllowBlank(false);
		tfNameEditor.setAutoValidate(false);
		ccName.setRenderer(new HtmlEncodeGridCellRenderer());
		ccName.setEditor(new CellEditor(tfNameEditor));
		cc.add(ccName);

		ColumnConfig ccValue = new ColumnConfig(PropertyModelData.PROPERTY_VALUE, pmsMessages.columnHeaderValue(), 150);
		TextField<String> tfPathEditor = new TextField<String>();
		tfPathEditor.setAllowBlank(false);
		tfPathEditor.setAutoValidate(false);
		ccValue.setRenderer(new HtmlEncodeGridCellRenderer());
		ccValue.setEditor(new CellEditor(tfPathEditor));
		cc.add(ccValue);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<PropertyModelData> store = new ListStore<PropertyModelData>();
		StoreListener<PropertyModelData> storeChangeNotifier = new StoreListener<PropertyModelData>() {
			@Override
			public void storeAdd(StoreEvent<PropertyModelData> se) {
				propertiesGridDirty = true;
				Util.fireChangeEvent(grid);
			}

			@Override
			public void storeRemove(StoreEvent<PropertyModelData> se) {
				propertiesGridDirty = true;
				Util.fireChangeEvent(grid);
			}

			@Override
			public void storeUpdate(StoreEvent<PropertyModelData> se) {
				propertiesGridDirty = true;
				Util.fireChangeEvent(grid);
			}
		};
		store.addStoreListener(storeChangeNotifier);

		grid = new EditorGrid<PropertyModelData>(store, cmOwn);
		grid.setAutoExpandColumn(PropertyModelData.PROPERTY_NAME);
		// gOwn.setStyleAttribute(BORDER_TOP_KEY, BORDER_TOP_VALUE);
		grid.setSelectionModel(sm);
		grid.addPlugin(sm);
		GridView gView = grid.getView();
		gView.setForceFit(true);

		containerGrid.add(grid);
		addPropertiesToolBar(containerGrid);
		container.add(containerGrid);
	}

	/**
	 * Adds the properties toolbar to the properties panel.<br/>
	 * @param containerGrid
	 */
	private void addPropertiesToolBar(ContentPanel containerGrid) {
		// create and add the tool bar
		ToolBar toolBar = new ToolBar();
		SelectionListener<ButtonEvent> lAdd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				PropertyDTO newProperty = new PropertyDTO();
				newProperty.setName(getPmsMessages().defaultPropertyName());
				newProperty.setValue(getPmsMessages().defaultPropertyValue());
				grid.getStore().add(new PropertyModelData(newProperty));
			}
		};
		buttonsSupport.addAddButton(toolBar, lAdd, null);
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<PropertyModelData> selectedItems = grid.getSelectionModel().getSelectedItems();
				if (selectedItems != null && !selectedItems.isEmpty()) {
					for (PropertyModelData item : selectedItems) {
						grid.getStore().remove(item);
					}
					deleteItem.disable();
				}
			}
		};
		deleteItem = buttonsSupport.addDeleteButton(toolBar, lDelete, null);
		deleteItem.disable();

		containerGrid.setTopComponent(toolBar);
	}

	/**
	 * Returns the listener for "Accept" button.<br/> Should be overridden by subclasses with correct specific
	 * logic.<br/>
	 * 
	 * @return
	 */
	protected abstract SelectionListener<ButtonEvent> getAcceptButtonListener();

	/**
	 * Should be overridden by subclasses with correct specific logic.<br/>
	 * 
	 * @return String containing the displayed panel heading text.
	 */
	protected abstract String getHeadingText();

	/**
	 * Displays on GUI the values of the bound DTO.<br/>
	 */
	private void displayBoundDataValues() {
		tfName.setValue(deviceDto.getName());
		tfName.updateOriginalValue(deviceDto.getName());

		DeviceType type = deviceDto.getType();
		if (type != null) {
			SimpleModelData typeModel = new SimpleModelData(type.toString(), type.toString());
			cbType.setValue(typeModel);
		}

		if (!deviceDto.isDefaultDevice()) {
			tfLayoutWidth.setValue(deviceDto.getWidth());
			tfLayoutWidth.updateOriginalValue(deviceDto.getWidth());

			taDescription.setValue(deviceDto.getDescription());

			tfUserAgent.setValue(deviceDto.getUserAgent());
			tfUserAgent.updateOriginalValue(deviceDto.getUserAgent());

			cbUserAgentRE.setValue(deviceDto.isUserAgentRE());
			cbUserAgentRE.updateOriginalValue(deviceDto.isUserAgentRE());

			// populate grid
			grid.getStore().setFiresEvents(false);
			grid.getStore().removeAll();
			if (deviceDto.getProperties() != null) {
				List<PropertyModelData> models = new ArrayList<PropertyModelData>();
				for (PropertyDTO propertyDto : deviceDto.getProperties()) {
					models.add(new PropertyModelData(propertyDto));
				}
				grid.getStore().add(models);
			}
			grid.getStore().setFiresEvents(true);
		}
	}

	protected DeviceDTO updateDeviceDto(DeviceDTO device) {
		device.setName(getTfName().getValue());
		device.setType(DeviceType.valueOf((String) getCbType().getValue().getId()));

		if (!deviceDto.isDefaultDevice()) {
			device.setDescription(getTaDescription().getValue());
			device.setUserAgent(getTfUserAgent().getValue());
			device.setUserAgentRE(getCbUserAgentRE().getValue());
			device.setWidth((Integer) tfLayoutWidth.getValue());

			// save properties
			ListStore<PropertyModelData> propertiesModels = grid.getStore();
			List<PropertyDTO> propertiesDto = new ArrayList<PropertyDTO>();
			for (int i = 0; i < propertiesModels.getCount(); i++) {
				propertiesDto.add(propertiesModels.getAt(i).getDTO());
			}
			device.setProperties(propertiesDto);
		}

		return device;
	}

	/**
	 * <br/>
	 * @return the "name" text field.
	 */
	protected final TextField<String> getTfName() {
		return tfName;
	}

	/**
	 * @return <code>true</code>, if at least one of the panel fields is dirty.
	 * @see Field#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Component> it = fields.iterator();
		while (!dirty && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				dirty = dirty || ((Field<?>) component).isDirty();
			} else if (component instanceof Grid<?>) {
				dirty = dirty || propertiesGridIsDirty();
			}
		}
		return dirty;
	}

	private boolean propertiesGridIsDirty() {
		return propertiesGridDirty || !grid.getStore().getModifiedRecords().isEmpty();
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
			}
		}
		return valid;
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
	 * @return the Devices async service proxy.
	 */
	protected IDevicesServiceAsync getDevicesService() {
		return devicesService;
	}

	/**
	 * Injects the Device service proxy.
	 * @param devicesService
	 */
	@Inject
	public void setDevicesService(IDevicesServiceAsync devicesService) {
		this.devicesService = devicesService;
	}

	/**
	 * @return the static objects container
	 */
	protected Util getUtilities() {
		return util;
	}

	/**
	 * @return the bound Device.
	 */
	protected DeviceDTO getDevice() {
		return deviceDto;
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
	 * Injects the form support service.
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = (IComponentListeningStrategy) pmsListeningStrategy;
	}

	/**
	 * @param nonEmptyStringValidator the nonEmptyStringValidator to set
	 */
	@Inject
	public void setNonEmptyStringValidator(NonEmptyStringValidator nonEmptyStringValidator) {
		this.nonEmptyStringValidator = nonEmptyStringValidator;
	}

	/**
	 * Injects the field validator listener.
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener = validatorListener;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the error message processor
	 */
	@Inject
	public ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
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
	 * @return the taDescription
	 */
	public TextArea getTaDescription() {
		return taDescription;
	}

	/**
	 * @return the cbType
	 */
	public ComboBox<SimpleModelData> getCbType() {
		return cbType;
	}

	/**
	 * @return the tfUserAgent
	 */
	public TextField<String> getTfUserAgent() {
		return tfUserAgent;
	}

	/**
	 * @return the cbUserAgentRE
	 */
	public CheckBox getCbUserAgentRE() {
		return cbUserAgentRE;
	}
}
